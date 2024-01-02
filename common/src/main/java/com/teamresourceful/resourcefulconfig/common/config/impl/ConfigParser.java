package com.teamresourceful.resourcefulconfig.common.config.impl;

import com.teamresourceful.resourcefulconfig.common.annotations.*;
import com.teamresourceful.resourcefulconfig.web.info.ResourcefulWebConfig;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.teamresourceful.resourcefulconfig.common.config.ParsingUtils.assertEntry;
import static com.teamresourceful.resourcefulconfig.common.config.ParsingUtils.assertButton;
import static com.teamresourceful.resourcefulconfig.common.config.ParsingUtils.assertValidClass;

public final class ConfigParser {

    public static ResourcefulConfigImpl parseConfig(Class<?> config) throws Exception {
        Config data = config.getAnnotation(Config.class);
        if (data == null) {
            throw new Exception("Config class " + config.getName() + " is missing @Config annotation!");
        }
        return createConfig(parseData(config, Config.class, "resourcefulconfig.config"), data.value());
    }

    private static ResourcefulConfigImpl createConfig(TempConfig config, @Nullable String file) {
        Map<String, ResourcefulConfigImpl> subConfigs = new LinkedHashMap<>();
        config.configs.forEach((key, value) -> subConfigs.put(key, createConfig(value, null)));
        return new ResourcefulConfigImpl(config.web(), new LinkedHashMap<>(config.entries()), new ArrayList<>(config.buttons()), subConfigs, file, config.translation());
    }

    private static TempConfig parseData(Class<?> config, Class<? extends Annotation> annotation, String translation) {
        assertValidClass(config, annotation);
        TempConfig builtConfig = new TempConfig(config, translation);

        for (Field entry : config.getDeclaredFields()) {
            InlineCategory inlineCategory = entry.getAnnotation(InlineCategory.class);
            if (inlineCategory != null) {
                Category data = entry.getType().getAnnotation(Category.class);
                if (data != null) {
                    builtConfig.configs.put(data.id(), parseData(entry.getType(), Category.class, data.translation()));
                }
                continue;
            }
            ConfigEntry data = assertEntry(entry);
            if (data == null) continue;
            builtConfig.entries.put(data.id(), ResourcefulConfigEntryImpl.create(data, entry));
        }

        for (Method method : config.getDeclaredMethods()) {
            ConfigButton data = assertButton(method);
            if (data == null) continue;
            String after = builtConfig.entries.containsKey(data.after()) ? data.after() : "";
            builtConfig.buttons.add(new ResourcefulButtonEntryImpl(after, method));
        }

        for (Class<?> subConfig : config.getDeclaredClasses()) {
            Category data = subConfig.getAnnotation(Category.class);
            if (data != null) {
                builtConfig.configs.put(data.id(), parseData(subConfig, Category.class, data.translation()));
            }
        }

        return builtConfig;
    }

    private record TempConfig(
            ResourcefulWebConfig web,
            Map<String, ResourcefulConfigEntryImpl> entries,
            List<ResourcefulButtonEntryImpl> buttons,
            Map<String, TempConfig> configs,
            String translation
    ) {

        private TempConfig(Class<?> config, String translation) {
            this(ResourcefulWebConfig.of(config), new LinkedHashMap<>(), new ArrayList<>(), new LinkedHashMap<>(), translation);
        }
    }
}
