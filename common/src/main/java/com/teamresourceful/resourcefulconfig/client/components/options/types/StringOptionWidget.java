package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringOptionWidget extends EditBox implements ResetableWidget {

    private static final int FOCUSED_EXTRA_WIDTH = 40;
    private static final int WIDTH = 80;
    private static final int FOCUSED_WIDTH = WIDTH + FOCUSED_EXTRA_WIDTH;

    private final Supplier<String> getter;
    private final Consumer<String> setter;

    public StringOptionWidget(Supplier<String> getter, Consumer<String> setter) {
        super(Minecraft.getInstance().font, WIDTH, 16, CommonComponents.EMPTY);
        setMaxLength(Short.MAX_VALUE);
        setBordered(false);
        setCanLoseFocus(true);
        setValue(getter.get());
        setResponder(setter);

        this.getter = getter;
        this.setter = setter;
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
        setResponder(s -> {});
        setValue(this.getter.get());
        setResponder(this.setter);
    }
}
