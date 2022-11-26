package com.teamresourceful.resourcefulconfig.client.options;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;

import java.util.function.DoubleConsumer;
import java.util.function.LongConsumer;
import java.util.regex.Pattern;

public class NumberInputBox extends EditBox {

    private static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d*");
    private static final Pattern FLOAT_PATTERN = Pattern.compile("-?\\d*(.\\d+)?");

    public NumberInputBox(Font font, int x, int y, int width, int height, String value, boolean floating, LongConsumer longConsumer, DoubleConsumer doubleConsumer) {
        super(font, x, y, width, height, CommonComponents.EMPTY);
        if (floating) {
            setFilter(text -> FLOAT_PATTERN.matcher(text).matches());
        } else {
            setFilter(text -> INTEGER_PATTERN.matcher(text).matches());
        }
        setValue(value);
        setResponder(text -> {
            if (text.isEmpty()) {
                text = "0";
            }
            if (floating) {
                try {
                    doubleConsumer.accept(Double.parseDouble(text));
                }catch (NumberFormatException e) {
                    doubleConsumer.accept(0);
                }
            } else {
                try {
                    longConsumer.accept(Long.parseLong(text));
                }catch (NumberFormatException e) {
                    longConsumer.accept(0);
                }
            }
        });
    }


}
