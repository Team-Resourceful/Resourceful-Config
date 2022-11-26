package com.teamresourceful.resourcefulconfig.common.config.forge;

import com.electronwill.nightconfig.core.AbstractConfig;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.teamresourceful.resourcefulconfig.common.config.ConfigLoader;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ForgeConfigLoader implements ConfigLoader {

    private final Map<ForgeConfigSpec, ForgeResourcefulConfig> configCache = new HashMap<>();

    public ForgeConfigLoader() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onConfigLoad);
        bus.addListener(this::onConfigReloaded);
    }

    public ResourcefulConfig registerConfig(Class<?> configClass) {
        try {
            ForgeResourcefulConfig config = ForgeConfigParser.parseConfig(configClass);
            configCache.put(config.getSpec(), config);
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, config.getSpec(), config.getFileName() + ".toml");
            return config;
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to create config for " + configClass.getName());
        }
        return null;
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
        spec.valueMap().forEach((id, value) -> {
            if (value instanceof AbstractConfig subConfig) {
                config.getSubConfig(id).ifPresent(cat -> loadConfig(cat, subConfig));
            } else {
                config.getEntry(id).ifPresent(entry -> {
                    if (!setValue(value, entry)) {
                        System.out.println("Failed to set value for " + id);
                    }
                });
            }
        });
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
        } else if (o instanceof Short shortValue) {
            return data.setShort(shortValue);
        } else if (o instanceof Byte byteValue) {
            return data.setByte(byteValue);
        } else if (o instanceof Float floatValue) {
            return data.setFloat(floatValue);
        } else if (o instanceof Integer integerValue) {
            return data.setInt(integerValue);
        } else if (o instanceof Long longValue) {
            return data.setLong(longValue);
        } else if (o instanceof Double doubleValue) {
            return data.setDouble(doubleValue);
        } else if (o instanceof Enum<?> enumValue) {
            return data.setEnum(enumValue);
        } else if (o instanceof Boolean booleanValue) {
            return data.setBoolean(booleanValue);
        }
        return true;
    }
}
