package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;

import java.text.DecimalFormat;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class NumberOptionWidget<T extends Number> extends EditBox implements ResetableWidget {

    public static final Pattern INTEGER_FILTER = Pattern.compile("-?[0-9]*");
    public static final Pattern DECIMAL_FILTER = Pattern.compile("-?[0-9]*(\\.[0-9]*)?");

    private static final int FOCUSED_EXTRA_WIDTH = 40;
    private static final int WIDTH = 80;
    private static final int FOCUSED_WIDTH = WIDTH + FOCUSED_EXTRA_WIDTH;

    private final Supplier<T> getter;
    private final Function<T, Boolean> setter;
    private final Function<String, T> parser;

    public NumberOptionWidget(Supplier<T> getter, Function<T, Boolean> setter, Function<String, T> parser, Pattern filter) {
        super(Minecraft.getInstance().font, WIDTH, 16, CommonComponents.EMPTY);
        setMaxLength(Short.MAX_VALUE);
        setBordered(false);
        setCanLoseFocus(true);

        this.getter = getter;
        this.setter = setter;
        this.parser = parser;

        DecimalFormat format = new DecimalFormat();
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(340);

        setValue(format.format(getter.get()));
        setFilter(s -> s.isEmpty() || filter.matcher(s).matches());
        setResponder();
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        updateIfFocused();

        graphics.blitSprite(ModSprites.BUTTON, getX(), getY(), this.width, this.height);

        graphics.enableScissor(getX() + 4, getY() + 4, getX() + this.width - 4, getY() + this.height - 4);

        var pose = graphics.pose();
        pose.pushPose();
        pose.translate(4, 4, 0);
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        pose.popPose();

        graphics.disableScissor();
    }

    public void updateIfFocused() {
        if (this.width != FOCUSED_WIDTH && isFocused()) {
            setWidth(FOCUSED_WIDTH);
            setX(getX() - FOCUSED_EXTRA_WIDTH);
        } else if (this.width != WIDTH && !isFocused()) {
            setWidth(WIDTH);
            setX(getX() + FOCUSED_EXTRA_WIDTH);
        }
    }

    @Override
    public void reset() {
        setResponder(s -> {
        });
        setValue(this.getter.get().toString());
        setResponder();
    }

    public void setResponder() {
        setResponder(s -> {
            try {
                T value = this.parser.apply(s);
                if (this.setter.apply(value)) {
                    this.setTextColor(0xE0E0E0);
                } else {
                    this.setTextColor(0xFF0000);
                }
            } catch (NumberFormatException ignored) {
                this.setTextColor(0xFF0000);
            }
        });
    }
}
