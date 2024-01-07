package com.teamresourceful.resourcefulconfig.api.types.options;

import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;

/**
 * This class is used to store a value and its optional translation.
 */
public record TranslatableValue(
    String value,
    String translation
) {

    public static final TranslatableValue EMPTY = new TranslatableValue("", "");

    public void ifPresent(BiConsumer<String, String> value) {
        if (!this.value.isBlank()) {
            value.accept(this.value, this.translation);
        }
    }

    public Component toComponent() {
        return Component.translatableWithFallback(translation(), value());
    }
}
