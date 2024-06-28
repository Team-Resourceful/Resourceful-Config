package com.teamresourceful.resourcefulconfig.api.types.options;

import com.teamresourceful.resourcefulconfig.api.annotations.Comment;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import net.minecraft.Optionull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to store the data for a {@link com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry}.
 * <br>
 * These additional options are found by using the {@link com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption} annotations.
 */
public record EntryData(
    TranslatableValue title,
    TranslatableValue comment,

    Map<Option<?, ?>, Object> options
) {

    public static Builder builder() {
        return new Builder();
    }

    public static EntryData of(Field field, Class<?> type) {
        return of(field::getAnnotation, type);
    }

    public static EntryData of(AnnotationGetter getter, Class<?> type) {
        ConfigEntry entry = getter.get(ConfigEntry.class);

        Builder builder = builder()
            .translation(
                entry.id(),
                entry.translation()
            )
            .comment(
                Optionull.mapOrDefault(getter.get(Comment.class), Comment::value, ""),
                Optionull.mapOrDefault(getter.get(Comment.class), Comment::translation, "")
            )
            .options(Option.gatherOptions(getter, type));

        return builder.build();
    }

    public boolean inRange(double value) {
        ConfigOption.Range range = getOption(Option.RANGE);
        return value >= range.min() && value <= range.max();
    }

    public boolean hasOption(Option<?, ?> option) {
        return this.options.containsKey(option);
    }

    @SuppressWarnings({"unchecked"})
    public <T extends Annotation, D> D getOption(Option<T, D> option) {
        return (D) options.get(option);
    }


    public <T extends Annotation, D> D getOrDefaultOption(Option<T, D> option, D defaultValue) {
        return hasOption(option) ? getOption(option) : defaultValue;
    }

    public static class Builder {
        private TranslatableValue title = TranslatableValue.EMPTY;
        private TranslatableValue comment = TranslatableValue.EMPTY;

        private Map<Option<?, ?>, Object> options = new HashMap<>();

        public Builder translation(String value, String translation) {
            this.title = new TranslatableValue(value, translation);
            return this;
        }

        public Builder comment(String value, String translation) {
            this.comment = new TranslatableValue(value, translation);
            return this;
        }

        public Builder options(Map<Option<?, ?>, Object> options) {
            this.options = options;
            return this;
        }

        public EntryData build() {
            return new EntryData(title, comment, options);
        }
    }
}
