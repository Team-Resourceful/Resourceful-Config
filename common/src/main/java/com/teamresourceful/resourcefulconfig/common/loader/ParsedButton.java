package com.teamresourceful.resourcefulconfig.common.loader;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigButton;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigButton;
import com.teamresourceful.resourcefulconfig.api.types.options.Position;

import java.lang.reflect.Method;

public record ParsedButton(
        String target,
        Position position,
        Method method,
        String text
) implements ResourcefulConfigButton {

    public static ParsedButton of(Method method) {
        ConfigButton button = method.getAnnotation(ConfigButton.class);
        return new ParsedButton(button.target(), button.position(), method, button.text());
    }

    @Override
    public boolean invoke() {
        try {
            method.invoke(null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
