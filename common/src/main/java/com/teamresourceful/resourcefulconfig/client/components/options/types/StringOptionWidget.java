package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;

import java.util.function.Function;
import java.util.function.Supplier;

public class StringOptionWidget extends EditBox implements ResetableWidget {

    private static final int FOCUSED_EXTRA_WIDTH = 40;
    private static final int WIDTH = 80;
    private static final int FOCUSED_WIDTH = WIDTH + FOCUSED_EXTRA_WIDTH;


    private final Supplier<String> getter;
    private final Function<String, Boolean> setter;
    private final boolean canExpand;

    public StringOptionWidget(Supplier<String> getter, Function<String, Boolean> setter) {
        this(getter, setter, true);
    }

    public StringOptionWidget(Supplier<String> getter, Function<String, Boolean> setter, boolean canExpand) {
        super(Minecraft.getInstance().font, WIDTH, 16, CommonComponents.EMPTY);
        setMaxLength(Short.MAX_VALUE);
        setBordered(false);
        setCanLoseFocus(true);

        this.getter = getter;
        this.setter = setter;
        this.canExpand = canExpand;

        setValue(getter.get());
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
        if (!isFocused()) setValue(getter.get());
        if (!canExpand) return;
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
        setResponder(s -> {});
        setValue(this.getter.get());
        setResponder();
    }

    public void setResponder() {
        setResponder(s -> {
            if (this.setter.apply(s)) {
                setTextColor(0xE0E0E0);
            } else {
                setTextColor(0xFF0000);
            }
        });
    }
}
