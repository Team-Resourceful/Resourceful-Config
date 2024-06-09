package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.utils.KeyCodeHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class KeybindOptionWidget extends BaseWidget {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 16;

    private final Supplier<Integer> getter;
    private final Consumer<Integer> setter;

    private boolean isEditing = false;

    public KeybindOptionWidget(Supplier<Integer> getter, Consumer<Integer> setter) {
        super(WIDTH, HEIGHT);

        this.getter = getter;
        this.setter = setter;
    }

    private Component getDisplay() {
        int key = getter.get();
        MutableComponent display = key == 0 ? Component.literal("None") : KeyCodeHelper.getKeyName(key).copy();
        if (this.isEditing) {
            boolean strikethrough = System.currentTimeMillis() / 500 % 2 == 0;
            return Component.literal("> ")
                    .withColor(UIConstants.TEXT_PARAGRAPH)
                    .append(display.withStyle(style -> style.withUnderlined(strikethrough).withColor(UIConstants.TEXT_TITLE)))
                    .append(Component.literal(" <"));
        }
        return display;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(ModSprites.BUTTON, getX(), getY(), getWidth(), getHeight());

        renderScrollingString(
            graphics, this.font, getDisplay(),
            getX() + 4, getY() + 2, getX() + getWidth() - 4, getY() + getHeight() - 2,
            UIConstants.TEXT_PARAGRAPH
        );
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.isEditing = true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isEditing) {
            this.setter.accept(-100 - button);
            this.isEditing = false;
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (this.isEditing) {
            this.setter.accept(i == InputConstants.KEY_ESCAPE ? 0 : i);
            this.isEditing = false;
            return true;
        }

        return super.keyPressed(i, j, k);
    }
}