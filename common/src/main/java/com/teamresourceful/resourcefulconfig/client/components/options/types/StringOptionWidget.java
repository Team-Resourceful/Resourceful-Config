package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.options.text.TextBox;
import com.teamresourceful.resourcefulconfig.client.utils.ListenableState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;

import java.util.function.Function;
import java.util.function.Supplier;

public class StringOptionWidget extends TextBox {

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
        super(WIDTH, 16, ListenableState.of(getter.get()));
        setMaxLength(Short.MAX_VALUE);

        this.getter = getter;
        this.setter = setter;
        this.canExpand = canExpand;
        this.state.registerListener(it -> {
            if (this.setter.apply(it)) {
                setTextColor(0xFFE0E0E0);
            } else {
                setTextColor(0xFFFF0000);
            }
        });
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        updateIfFocused();
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.BUTTON, getX(), getY(), this.width, this.height);
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
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
}
