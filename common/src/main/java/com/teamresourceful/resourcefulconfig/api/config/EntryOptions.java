package com.teamresourceful.resourcefulconfig.api.config;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;

import java.lang.reflect.Field;

/**
 * This class is used to store the options for a {@link com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry}.
 * <br>
 * These options are found by using the {@link com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption} annotations.
 */
public record EntryOptions(
    boolean isMultiline,

    boolean hasSlider,
    boolean hasRange,
    double min,
    double max,

    String separator,
    String separatorDescription
) {
    public static EntryOptions of(Field field) {
        boolean hasMultiline = field.isAnnotationPresent(ConfigOption.Multiline.class);
        boolean hasSlider = field.isAnnotationPresent(ConfigOption.Slider.class);
        boolean hasRange = field.isAnnotationPresent(ConfigOption.Range.class);
        boolean hasSeparator = field.isAnnotationPresent(ConfigOption.Separator.class);

        double min = 0;
        double max = 0;

        String separator = null;
        String separatorDescription = null;

        if (hasRange) {
            ConfigOption.Range range = field.getAnnotation(ConfigOption.Range.class);
            min = range.min();
            max = range.max();
        }

        if (hasSeparator) {
            ConfigOption.Separator sep = field.getAnnotation(ConfigOption.Separator.class);
            separator = sep.value();
            separatorDescription = sep.description();
        }


        return new EntryOptions(hasMultiline, hasSlider, hasRange, min, max, separator, separatorDescription);
    }

    public boolean inRange(double value) {
        return value >= min && value <= max;
    }
}
