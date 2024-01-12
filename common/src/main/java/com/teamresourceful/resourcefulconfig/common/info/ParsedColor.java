package com.teamresourceful.resourcefulconfig.common.info;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigColor;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigColorValue;

public record ParsedColor(
        String color
) implements ResourcefulConfigColorValue {

    public static final ParsedColor DEFAULT = new ParsedColor("#ffffff");

    public static ResourcefulConfigColor of(Class<?> clazz) {
        ConfigInfo.Color color = clazz.getAnnotation(ConfigInfo.Color.class);
        if (color != null) {
            return new ParsedColor(color.value());
        }
        return null;
    }
}
