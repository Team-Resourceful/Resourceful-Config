package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.utils.KeyCodeHelper;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

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
                    .append(display.withStyle(style -> style.withUnderlined(strikethrough)
                            .withColor(UIConstants.TEXT_TITLE)))
                    .append(Component.literal(" <"));
        }
        return display;
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.BUTTON, getX(), getY(), getWidth(), getHeight());

        int left = getX() + 4;
        int right = getX() + getWidth() - 4;
        graphics.textRendererForWidget(
                this,
                GuiGraphicsExtractor.HoveredTextEffects.NONE
        ).acceptScrolling(
                getDisplay().copy().withColor(UIConstants.TEXT_PARAGRAPH),
                (left + right) / 2,
                left,
                right,
                getY() + 2,
                getY() + getHeight() - 2
        );

        this.applyCursor(graphics);
    }

    @Override
    public void onClick(@NotNull MouseButtonEvent event, boolean bl) {
        this.isEditing = true;
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean bl) {
        if (this.isEditing) {
            this.setter.accept(-100 - event.input());
            this.isEditing = false;
            return true;
        }
        return super.mouseClicked(event, bl);
    }

    @Override
    public boolean keyPressed(@NotNull KeyEvent event) {
        if (this.isEditing) {
            this.setter.accept(event.input() == InputConstants.KEY_ESCAPE ? 0 : event.input());
            this.isEditing = false;
            return true;
        }
        return super.keyPressed(event);
    }
}