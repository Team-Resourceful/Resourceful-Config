package com.teamresourceful.resourcefulconfig.common.loader.elements;

import com.teamresourceful.resourcefulconfig.api.annotations.Comment;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigButton;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigButton;
import net.minecraft.Optionull;

import java.lang.reflect.Field;

public record ParsedButtonElement(
        String title,
        String description,
        Runnable runnable,
        String text
) implements ResourcefulConfigButton {

    public static ParsedButtonElement of(Field field) {
        ConfigButton button = field.getAnnotation(ConfigButton.class);
        String description = Optionull.mapOrDefault(field.getAnnotation(Comment.class), Comment::value, "");
        try {
            Runnable runnable = (Runnable) field.get(null);
            return new ParsedButtonElement(button.title(), description, runnable, button.text());
        }catch (IllegalAccessException e) {
            // This should never happen as we check if its public and static before getting the value.
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean invoke() {
        try {
            runnable.run();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
