package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Either;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class KeybindOptionWidget extends BaseWidget {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 16;

    private final Supplier<Component> display;
    private final Consumer<Either<MouseButtonEvent, KeyEvent>> setter;

    private boolean isEditing = false;

    public KeybindOptionWidget(Supplier<Component> display, Consumer<Either<MouseButtonEvent, KeyEvent>> setter) {
        super(WIDTH, HEIGHT);

        this.display = display;
        this.setter = setter;
    }

    public static KeybindOptionWidget forLegacy(Supplier<Integer> getter, Consumer<Integer> setter) {
        return new KeybindOptionWidget(
            () -> {
                var key = getter.get();
                if (key < 0) {
                    return InputConstants.Type.MOUSE.getOrCreate(key + 100).getDisplayName();
                } else if (key == 0) {
                    return Component.literal("None");
                } else {
                    return InputConstants.Type.KEYBOARD.getOrCreate(key).getDisplayName();
                }
            },
            event -> {
                event.ifLeft(mouseEvent -> setter.accept(mouseEvent.input() - 100));
                event.ifRight(keyEvent -> setter.accept(keyEvent.isEscape() ? 0 : keyEvent.input()));
            }
        );
    }

    private Component getDisplay() {
        if (this.isEditing) {
            boolean strikethrough = System.currentTimeMillis() / 500 % 2 == 0;
            return Component.literal("> ")
                    .withColor(UIConstants.TEXT_PARAGRAPH)
                    .append(display.get().copy().withStyle(style -> style.withUnderlined(strikethrough)
                            .withColor(UIConstants.TEXT_TITLE)))
                    .append(Component.literal(" <"));
        }
        return display.get();
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
            this.setter.accept(Either.left(event));
            this.isEditing = false;
            return true;
        }
        return super.mouseClicked(event, bl);
    }

    @Override
    public boolean keyPressed(@NotNull KeyEvent event) {
        if (this.isEditing) {
            this.setter.accept(Either.right(event));
            this.isEditing = false;
            return true;
        }
        return super.keyPressed(event);
    }
}