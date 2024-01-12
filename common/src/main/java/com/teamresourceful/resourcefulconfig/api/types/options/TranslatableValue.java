package com.teamresourceful.resourcefulconfig.api.types.options;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.BiConsumer;

/**
 * This class is used to store a value and its optional translation.
 */
public record TranslatableValue(
    String value,
    String translation
) {

    public static final TranslatableValue EMPTY = new TranslatableValue("", "");

    public TranslatableValue(String value) {
        this(value, "");
    }

    public void ifPresent(BiConsumer<String, String> value) {
        if (!this.value.isBlank()) {
            value.accept(this.value, this.translation);
        }
    }

    public MutableComponent toComponent() {
        return Component.translatableWithFallback(translation(), value());
    }

    public String toLocalizedString() {
        return Language.getInstance().getOrDefault(translation(), value());
    }

    public boolean hasTranslation() {
        return !translation().isBlank();
    }
}
