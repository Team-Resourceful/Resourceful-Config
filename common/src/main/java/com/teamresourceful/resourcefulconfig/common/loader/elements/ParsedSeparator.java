package com.teamresourceful.resourcefulconfig.common.loader.elements;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigSeparatorElement;
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue;

import java.lang.reflect.Field;

public record ParsedSeparator(
        TranslatableValue title,
        TranslatableValue description
) implements ResourcefulConfigSeparatorElement {

    public static ParsedSeparator of(Field field) {
        ConfigOption.Separator separator = field.getAnnotation(ConfigOption.Separator.class);
        return new ParsedSeparator(
                new TranslatableValue("", separator.value()),
                new TranslatableValue("", separator.description())
        );
    }
}
