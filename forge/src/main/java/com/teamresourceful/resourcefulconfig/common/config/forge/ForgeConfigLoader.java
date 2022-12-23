package com.teamresourceful.resourcefulconfig.common.config.forge;

import com.electronwill.nightconfig.core.AbstractConfig;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mojang.logging.LogUtils;
import com.teamresourceful.resourcefulconfig.common.config.ConfigLoader;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigEvent;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ForgeConfigLoader implements ConfigLoader {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<ForgeConfigSpec, ForgeResourcefulConfig> configCache = new ConcurrentHashMap<>();
    private final boolean forceLoad;

    public ForgeConfigLoader(boolean forceLoad) {
        this.forceLoad = forceLoad;
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onConfigLoad);
        bus.addListener(this::onConfigReloaded);
    }

    @Override
    public ResourcefulConfig registerConfig(Class<?> configClass) {
        try {
            ModLoadingContext context = ModLoadingContext.get();
            ForgeResourcefulConfig config = ForgeConfigParser.parseConfig(configClass);
            configCache.put(config.getSpec(), config);
            ModContainer container = context.getActiveContainer();
            ResourcefulModConfig modConfig = new ResourcefulModConfig(ModConfig.Type.COMMON, config.getSpec(), container, config.getFileName() + ".toml");
            container.addConfig(modConfig);
            if (this.forceLoad) {
                forceLoad(modConfig, container);
            }
            return config;
        }catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Failed to register config for class: {}", configClass.getName());
        }
        return null;
    }

    private static void forceLoad(ResourcefulModConfig config, ModContainer container) {
        try {
            Path path = FMLPaths.CONFIGDIR.get().resolve(config.getFileName());
            CommentedFileConfig data = CommentedFileConfig.builder(path)
                .sync()
                .preserveInsertionOrder()
                .autosave()
                .onFileNotFound((file, format) -> setupConfigFile(config, file, format))
                .writingMode(WritingMode.REPLACE)
                .build();
            data.load();
            config.setTempConfig(data);
            container.dispatchConfigEvent(IConfigEvent.loading(config));
        }catch (Exception ignored) {}
    }

    private static boolean setupConfigFile(final ModConfig modConfig, final Path file, final ConfigFormat<?> conf) throws IOException {
        Files.createDirectories(file.getParent());
        Path p = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(modConfig.getFileName());
        if (Files.exists(p)) {
            LOGGER.info("Loading default config file from path {}", p);
            Files.copy(p, file);
        } else {
            Files.createFile(file);
            conf.initEmptyFile(file);
        }
        return true;
    }

    public void onConfigReloaded(ModConfigEvent.Reloading event) {
        IConfigSpec<ForgeConfigSpec> spec = event.getConfig().getSpec();
        ForgeConfigSpec forgeSpec = spec.self();
        ForgeResourcefulConfig config = configCache.get(forgeSpec);
        if (config != null) {
            loadConfig(config, event.getConfig().getConfigData());
        }
    }

    public void onConfigLoad(ModConfigEvent.Loading event) {
        IConfigSpec<ForgeConfigSpec> spec = event.getConfig().getSpec();
        ForgeConfigSpec forgeSpec = spec.self();
        ForgeResourcefulConfig config = configCache.get(forgeSpec);
        if (config != null) {
            loadConfig(config, event.getConfig().getConfigData());
        }
    }

    private void loadConfig(ResourcefulConfig config, UnmodifiableConfig spec) {
        final Map<String, Object> values = spec.valueMap();
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (values) {
            values.forEach((id, value) -> {
                if (value instanceof AbstractConfig subConfig) {
                    config.getSubConfig(id).ifPresent(cat -> loadConfig(cat, subConfig));
                } else {
                    config.getEntry(id).ifPresent(entry -> {
                        if (!setValue(value, entry)) {
                            LOGGER.error("Failed to set value for entry: {}", id);
                        }
                    });
                }
            });
        }
    }

    private boolean setValue(Object o, ResourcefulConfigEntry data) {
        if (o instanceof List<?> list) {
            return data.setArray(list.toArray());
        } else if (o instanceof String string) {
            return switch (data.type()) {
                case STRING -> data.setString(string);
                case ENUM -> data.setEnum(ParsingUtils.getEnum(data.field().getType(), string));
                default -> false;
            };
        } else if (o instanceof Number number) {
            try {
                return switch (data.type()) {
                    case BYTE -> data.setByte(number.byteValue());
                    case SHORT -> data.setShort(number.shortValue());
                    case INTEGER -> data.setInt(number.intValue());
                    case LONG -> data.setLong(number.longValue());

                    case FLOAT -> data.setFloat(number.floatValue());
                    case DOUBLE -> data.setDouble(number.doubleValue());
                    default -> false;
                };
            } catch (final ArithmeticException e) {
                return false;
            }
        } else if (o instanceof Enum<?> enumValue) {
            return data.setEnum(enumValue);
        } else if (o instanceof Boolean booleanValue) {
            return data.setBoolean(booleanValue);
        }
        return true;
    }

    private static final class ResourcefulModConfig extends ModConfig {

        private CommentedConfig tempConfig;

        public ResourcefulModConfig(Type type, IConfigSpec<?> spec, ModContainer container, String fileName) {
            super(type, spec, container, fileName);
        }

        /**
         * Sets the temporary config for this mod config. Should only be used by the force config loader.
         */
        public void setTempConfig(final CommentedConfig configData) {
            this.tempConfig = configData;
            this.getSpec().acceptConfig(configData);
        }

        @Override
        public CommentedConfig getConfigData() {
            final CommentedConfig config = super.getConfigData();
            return config == null ? tempConfig : config;
        }

        @Override
        public void save() {
            ((CommentedFileConfig)getConfigData()).save();
        }

        @Override
        public Path getFullPath() {
            return ((CommentedFileConfig)getConfigData()).getNioPath();
        }
    }
}
