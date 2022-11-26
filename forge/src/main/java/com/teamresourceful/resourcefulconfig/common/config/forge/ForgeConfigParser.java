package com.teamresourceful.resourcefulconfig.common.config.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;
import com.teamresourceful.resourcefulconfig.common.annotations.Category;
import com.teamresourceful.resourcefulconfig.common.annotations.Comment;
import com.teamresourceful.resourcefulconfig.common.annotations.Config;
import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.teamresourceful.resourcefulconfig.common.config.ParsingUtils.assertEntry;
import static com.teamresourceful.resourcefulconfig.common.config.ParsingUtils.assertValidClass;

public final class ForgeConfigParser {

    public static ForgeResourcefulConfig parseConfig(Class<?> config) throws Exception {
        Config data = config.getAnnotation(Config.class);
        if (data == null) {
            throw new Exception("Config class " + config.getName() + " is missing @Config annotation!");
        }
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        return createConfig(parseData(config, "resourcefulconfig.config", Config.class, builder), data.value(), builder.build());
    }

    private static ForgeResourcefulConfig createConfig(TempConfig config, @Nullable String file, ForgeConfigSpec spec) {
        Map<String, ForgeResourcefulConfig> subConfigs = new LinkedHashMap<>();
        config.configs.forEach((key, value) -> subConfigs.put(key, createConfig(value, null, null)));
        return new ForgeResourcefulConfig(new LinkedHashMap<>(config.entries), subConfigs, file, config.translation, spec);
    }

    private static TempConfig parseData(Class<?> config, String translation, Class<? extends Annotation> annotation, ForgeConfigSpec.Builder builder) {
        assertValidClass(config, annotation);
        TempConfig builtConfig = new TempConfig(translation);

        for (Class<?> subConfig : config.getDeclaredClasses()) {
            Category data = subConfig.getAnnotation(Category.class);
            if (data != null) {
                builder.push(data.id());
                builtConfig.configs.put(data.id(), parseData(subConfig, data.translation(), Category.class, builder));
                builder.pop();
            }
        }

        for (Field entry : config.getDeclaredFields()) {
            ConfigEntry data = assertEntry(entry);
            if (data == null) continue;
            Comment comment = entry.getAnnotation(Comment.class);
            if (comment != null) {
                builder.comment(comment.value());
            }
            builtConfig.entries.put(data.id(), ForgeResourcefulConfigEntry.create(data, entry, builder));
        }

        return builtConfig;
    }

    private record TempConfig(Map<String, ForgeResourcefulConfigEntry> entries, Map<String, TempConfig> configs, String translation) {

        private TempConfig(String translation) {
            this(new LinkedHashMap<>(), new LinkedHashMap<>(), translation);
        }
    }
}
