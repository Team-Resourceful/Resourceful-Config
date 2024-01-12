package com.teamresourceful.resourcefulconfig.common.info;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigColor;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink;
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue;

import java.util.Objects;

public record ParsedInfo(
        TranslatableValue title,
        TranslatableValue description,
        String icon,
        ResourcefulConfigColor color,
        ResourcefulConfigLink[] links,
        boolean isHidden
) implements ResourcefulConfigInfo {

    public static final ResourcefulConfigInfo EMPTY = ParsedInfo.of("", ParsedColor.DEFAULT, false);

    public static ResourcefulConfigInfo of(Class<?> clazz, String id) {
        ResourcefulConfigColor color = color(clazz);

        ConfigInfo info = clazz.getAnnotation(ConfigInfo.class);
        boolean hidden = clazz.getAnnotation(ConfigOption.Hidden.class) != null;
        if (info == null) {
            ResourcefulConfigInfo providerInfo = ConfigInfoLoader.load(clazz, id);
            return Objects.requireNonNullElseGet(providerInfo, () -> of(id, color, hidden));
        }
        TranslatableValue title = new TranslatableValue(info.title(), info.titleTranslation());
        TranslatableValue description = new TranslatableValue(info.description(), info.descriptionTranslation());
        String icon = info.icon();
        ResourcefulConfigLink[] links = new ResourcefulConfigLink[info.links().length];
        for (int i = 0; i < info.links().length; i++) {
            links[i] = ParsedLink.of(info.links()[i]);
        }
        return new ParsedInfo(title, description, icon, color, links, hidden);
    }

    private static ResourcefulConfigColor color(Class<?> clazz) {
        ResourcefulConfigColor color = ParsedColor.of(clazz);
        if (color == null) color = ParsedGradient.of(clazz);
        if (color == null) color = ParsedColor.DEFAULT;
        return color;
    }

    private static ResourcefulConfigInfo of(String id, ResourcefulConfigColor color, boolean hidden) {
        return new ParsedInfo(new TranslatableValue(id), TranslatableValue.EMPTY, "box", color, new ResourcefulConfigLink[0], hidden);
    }

}
