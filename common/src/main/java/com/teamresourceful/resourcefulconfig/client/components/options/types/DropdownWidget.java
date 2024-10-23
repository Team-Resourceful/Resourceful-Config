package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.api.types.info.Translatable;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import com.teamresourceful.resourcefulconfig.client.screens.base.OverlayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DropdownWidget extends BaseWidget {

    private static final int WIDTH = 80;

    private final Enum<?>[] options;
    private final Supplier<Enum<?>> getter;
    private final Consumer<Enum<?>> setter;

    private Component title = Component.empty();

    public DropdownWidget(int width, Enum<?>[] options, Supplier<Enum<?>> getter, Consumer<Enum<?>> setter) {
        super(width, 16);
        this.options = options;
        this.getter = getter;
        this.setter = setter;
    }

    public DropdownWidget(Enum<?>[] options, Supplier<Enum<?>> getter, Consumer<Enum<?>> setter) {
        this(WIDTH, options, getter, setter);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> DropdownWidget of(T[] options, Supplier<T> getter, Consumer<T> setter) {
        return new DropdownWidget(options, getter::get, t -> setter.accept((T) t));
    }

    public DropdownWidget setTitle(Component title) {
        this.title = title;
        return this;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(RenderType::guiTextured, ModSprites.ofButton(this.isHovered()), getX(), getY(), getWidth(), getHeight());
        renderScrollingString(
                graphics, this.font, Translatable.toComponent(this.getter.get(), this.title),
                getX() + 4, getY() + 4,
                getX() + getWidth() - 16, getY() + getHeight() - 4,
                UIConstants.TEXT_PARAGRAPH
        );
        graphics.blitSprite(RenderType::guiTextured, ModSprites.CHEVRON_DOWN, getX() + getWidth() - 12, getY() + 4, 8, 8);
    }

    @Override
    public void onClick(double d, double e) {
        Minecraft.getInstance().setScreen(new DropdownOverlay(this));
    }

    private static class DropdownOverlay extends OverlayScreen {

        private final DropdownWidget widget;

        protected DropdownOverlay(DropdownWidget widget) {
            super(Minecraft.getInstance().screen);
            this.widget = widget;
        }

        @Override
        protected void init() {
            var list = addRenderableWidget(DropdownList.of(widget));
            for (Enum<?> option : widget.options) {
                list.add(new DropdownItem(option, (value) -> {
                    widget.setter.accept(value);
                    this.onClose();
                }));
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (getChildAt(mouseX, mouseY).isEmpty()) {
                this.onClose();
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    private static class DropdownList extends ListWidget {

        public DropdownList(int x, int y, int height) {
            super(x + 1, y, WIDTH - 2, height);
        }

        public static DropdownList of(DropdownWidget widget) {
            int windowHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            int widgetY = widget.getY() + widget.getHeight();
            int listHeight = Math.min(widget.options.length * 12, 12 * 8) + 1;
            if (widgetY + listHeight > windowHeight) {
                widgetY = widget.getY() - listHeight - 1;
            }
            return new DropdownList(widget.getX(), widgetY, listHeight);
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            graphics.blitSprite(RenderType::guiTextured, ModSprites.ACCENT, getX() - 1, getY() - 1, getWidth() + 2, getHeight() + 2);
            graphics.blitSprite(RenderType::guiTextured, ModSprites.BUTTON, getX(), getY(), getWidth(), getHeight());
            super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        }
    }

    private static class DropdownItem extends BaseWidget implements ListWidget.Item {

        private final Enum<?> option;
        private final Consumer<Enum<?>> setter;

        public DropdownItem(Enum<?> option, Consumer<Enum<?>> setter) {
            super(WIDTH, 12);
            this.option = option;
            this.setter = setter;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            graphics.blitSprite(RenderType::guiTextured, ModSprites.ofButton(this.isHovered()), getX() + 1, getY(), getWidth() - 1, getHeight());
            int color = this.isHovered() ? UIConstants.TEXT_TITLE : UIConstants.TEXT_PARAGRAPH;
            renderScrollingString(
                    graphics, this.font, Translatable.toComponent(this.option),
                    getX() + 4, getY() + 1,
                    getX() + getWidth() - 4, getY() + getHeight() - 1,
                    color
            );
        }

        @Override
        public void onClick(double mouseX, double e) {
            this.setter.accept(this.option);
        }

        @Override
        public void setItemWidth(int width) {
            this.setWidth(width);
        }
    }
}
