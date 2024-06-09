package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.api.types.info.Translatable;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import com.teamresourceful.resourcefulconfig.client.screens.base.OverlayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SelectWidget extends BaseWidget {

    private static final int WIDTH = 80;
    private static final int FOCUSED_LIST = 80;

    private final Component heading;
    private final Enum<?>[] options;
    private final Supplier<Enum<?>[]> getter;
    private final Consumer<Enum<?>[]> setter;

    public SelectWidget(Component heading, Enum<?>[] options, Supplier<Enum<?>[]> getter, Consumer<Enum<?>[]> setter) {
        super(WIDTH, 16);
        this.heading = heading;
        this.options = options;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(ModSprites.ofButton(this.isHovered()), getX(), getY(), getWidth(), getHeight());
        renderScrollingString(
                graphics, this.font, this.heading,
                getX() + 4, getY() + 4,
                getX() + getWidth() - 16, getY() + getHeight() - 4,
                UIConstants.TEXT_PARAGRAPH
        );
        graphics.blitSprite(ModSprites.CHEVRON_DOWN, getX() + getWidth() - 12, getY() + 4, 8, 8);
    }

    @Override
    public void onClick(double d, double e) {
        Minecraft.getInstance().setScreen(new SelectOverlay(this));
    }

    private static class SelectOverlay extends OverlayScreen {

        private final SelectWidget widget;

        protected SelectOverlay(SelectWidget widget) {
            super(Minecraft.getInstance().screen);
            this.widget = widget;
        }

        @Override
        protected void init() {
            var list = addRenderableWidget(SelectList.of(widget));
            for (Enum<?> option : widget.options) {
                list.add(new SelectItem(
                    option,
                    () -> {
                        Set<Enum<?>> set = Set.of(widget.getter.get());
                        return set.contains(option);
                    },
                    (value) -> {
                        Set<Enum<?>> set = new HashSet<>(Set.of(widget.getter.get()));
                        if (set.contains(value)) {
                            set.remove(value);
                        } else {
                            set.add(value);
                        }
                        widget.setter.accept(set.toArray(new Enum<?>[0]));
                    }
                ));
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

    private static class SelectList extends ListWidget {

        public SelectList(int x, int y, int height) {
            super(x + 1, y, WIDTH - 2, height);
        }

        public static SelectList of(SelectWidget widget) {
            int windowHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            int widgetY = widget.getY() + widget.getHeight();
            int listHeight = Math.min(widget.options.length * 12, 12 * 8) + 1;
            if (widgetY + listHeight > windowHeight) {
                widgetY = widget.getY() - listHeight - 1;
            }
            return new SelectList(widget.getX(), widgetY, listHeight);
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            graphics.blitSprite(ModSprites.ACCENT, getX() - 1, getY() - 1, getWidth() + 2, getHeight() + 2);
            graphics.blitSprite(ModSprites.BUTTON, getX(), getY(), getWidth(), getHeight());
            super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        }
    }

    private static class SelectItem extends BaseWidget implements ListWidget.Item {

        private final Enum<?> option;
        private final BooleanSupplier selected;
        private final Consumer<Enum<?>> setter;

        public SelectItem(Enum<?> option, BooleanSupplier selected, Consumer<Enum<?>> setter) {
            super(WIDTH, 12);
            this.option = option;
            this.selected = selected;
            this.setter = setter;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            graphics.blitSprite(ModSprites.ofButton(this.isHovered()), getX() + 1, getY(), getWidth() - 1, getHeight());
            if (this.selected.getAsBoolean()) {
                graphics.blitSprite(ModSprites.CHECK, getX() + 4, getY() + 2, 8, 8);
            }
            int color = this.isHovered() ? UIConstants.TEXT_TITLE : UIConstants.TEXT_PARAGRAPH;

            renderScrollingString(
                    graphics, this.font, Translatable.toComponent(this.option),
                    getX() + 16, getY() + 1,
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
