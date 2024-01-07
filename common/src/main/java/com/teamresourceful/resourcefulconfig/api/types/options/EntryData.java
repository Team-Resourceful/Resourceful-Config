package com.teamresourceful.resourcefulconfig.api.types.options;

import com.teamresourceful.resourcefulconfig.api.annotations.Comment;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import net.minecraft.Optionull;

import java.lang.reflect.Field;

/**
 * This class is used to store the data for a {@link com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry}.
 * <br>
 * These additional options are found by using the {@link com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption} annotations.
 */
public record EntryData(
    TranslatableValue title,
    TranslatableValue comment,

    boolean isMultiline,

    boolean hasSlider,
    boolean hasRange,
    double min,
    double max,

    String separator,
    String separatorDescription
) {

    public static Builder builder() {
        return new Builder();
    }

    public static EntryData of(Field field) {
        ConfigEntry entry = field.getAnnotation(ConfigEntry.class);

        Builder builder = builder()
            .translation(
                entry.id(),
                entry.translation()
            )
            .comment(
                Optionull.mapOrDefault(field.getAnnotation(Comment.class), Comment::value, ""),
                Optionull.mapOrDefault(field.getAnnotation(Comment.class), Comment::translation, "")
            )
            .isMultiline(field.isAnnotationPresent(ConfigOption.Multiline.class))
            .hasSlider(field.isAnnotationPresent(ConfigOption.Slider.class));

        if (field.isAnnotationPresent(ConfigOption.Range.class)) {
            ConfigOption.Range range = field.getAnnotation(ConfigOption.Range.class);
            builder.range(range.min(), range.max());
        }

        if (field.isAnnotationPresent(ConfigOption.Separator.class)) {
            ConfigOption.Separator sep = field.getAnnotation(ConfigOption.Separator.class);
            builder.separator(sep.value(), sep.description());
        }

        return builder.build();
    }

    public boolean inRange(double value) {
        return value >= min && value <= max;
    }

    public static class Builder {
        private TranslatableValue title = TranslatableValue.EMPTY;
        private TranslatableValue comment = TranslatableValue.EMPTY;

        private boolean isMultiline = false;

        private boolean hasSlider = false;
        private boolean hasRange = false;
        private double min = 0;
        private double max = 0;

        private String separator = null;
        private String separatorDescription = null;

        public Builder translation(String value, String translation) {
            this.title = new TranslatableValue(value, translation);
            return this;
        }

        public Builder comment(String value, String translation) {
            this.comment = new TranslatableValue(value, translation);
            return this;
        }

        public Builder isMultiline(boolean value) {
            this.isMultiline = value;
            return this;
        }

        public Builder hasSlider(boolean value) {
            this.hasSlider = value;
            return this;
        }

        public Builder range(double min, double max) {
            this.hasRange = true;
            this.min = min;
            this.max = max;
            return this;
        }

        public Builder separator(String title, String description) {
            this.separator = title;
            this.separatorDescription = description;
            return this;
        }

        public EntryData build() {
            return new EntryData(title, comment, isMultiline, hasSlider, hasRange, min, max, separator, separatorDescription);
        }
    }
}
