package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.api.types.info.Translatable;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import com.teamresourceful.resourcefulconfig.client.screens.base.OverlayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SelectWidget<T> extends BaseWidget {

    private static final int MIN_WIDTH = 80;
    private static final int MAX_WIDTH = MIN_WIDTH * 2;

    private final Component heading;
    private final List<T> options;
    private final Supplier<List<T>> getter;
    private final Consumer<List<T>> setter;

    public SelectWidget(Component heading, List<T> options, Supplier<List<T>> getter, Consumer<List<T>> setter) {
        super(MIN_WIDTH, 16);
        this.heading = heading;
        this.options = options;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.ofButton(this.isHovered()), getX(), getY(), getWidth(), getHeight());
        renderScrollingString(
                graphics, this.font, this.heading,
                getX() + 4, getY() + 4,
                getX() + getWidth() - 16, getY() + getHeight() - 4,
                UIConstants.TEXT_PARAGRAPH
        );
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.CHEVRON_DOWN, getX() + getWidth() - 12, getY() + 4, 8, 8);
    }

    @Override
    public void onClick(double d, double e) {
        Minecraft.getInstance().setScreen(new SelectOverlay<>(this));
    }

    private static class SelectOverlay<T> extends OverlayScreen {

        private final SelectWidget<T> widget;

        protected SelectOverlay(SelectWidget<T> widget) {
            super(Minecraft.getInstance().screen);
            this.widget = widget;
        }

        @Override
        protected void init() {
            var list = addRenderableWidget(SelectList.of(widget));
            for (T option : widget.options) {
                list.add(new SelectItem(
                    option,
                    () -> {
                        Set<T> set = Set.copyOf(widget.getter.get());
                        return set.contains(option);
                    },
                    () -> {
                        Set<T> set = new HashSet<>(widget.getter.get());
                        if (set.contains(option)) {
                            set.remove(option);
                        } else {
                            set.add(option);
                        }
                        widget.setter.accept(List.copyOf(set));
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

        private final int ogX;

        public SelectList(int x, int y, int height) {
            super(x + 1, y, MIN_WIDTH - 2, height);

            this.ogX = x;
        }

        public static SelectList of(SelectWidget<?> widget) {
            int windowHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            int widgetY = widget.getY() + widget.getHeight();
            int listHeight = Math.min(widget.options.size() * 12, 12 * 8) + 1;
            if (widgetY + listHeight > windowHeight) {
                widgetY = widget.getY() - listHeight - 1;
            }
            return new SelectList(widget.getX(), widgetY, listHeight);
        }

        @Override
        public void add(Item item) {
            super.add(item);
            if (!(item instanceof SelectItem it)) return;
            var addition = this.items.size() * 12 > this.height ? 10 : 0;
            if (it.effectiveWidth() + addition <= this.width) return;

            this.setWidth(Math.min(it.effectiveWidth() + addition, SelectWidget.MAX_WIDTH));
            this.setX(this.ogX - (this.width - MIN_WIDTH) - 1);
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.ACCENT, getX() - 1, getY() - 1, getWidth() + 2, getHeight() + 2);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.BUTTON, getX(), getY(), getWidth(), getHeight());
            super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        }
    }

    private static class SelectItem extends BaseWidget implements ListWidget.Item {

        private final Object option;
        private final BooleanSupplier selected;
        private final Runnable onClick;

        public SelectItem(Object option, BooleanSupplier selected, Runnable onClick) {
            super(MIN_WIDTH, 12);
            this.option = option;
            this.selected = selected;
            this.onClick = onClick;
        }

        public int effectiveWidth() {
            return Mth.clamp(Minecraft.getInstance().font.width(Translatable.toComponent(this.option)) + 20, MIN_WIDTH, MAX_WIDTH);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.ofButton(this.isHovered()), getX() + 1, getY(), getWidth() - 1, getHeight());
            if (this.selected.getAsBoolean()) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.CHECK, getX() + 4, getY() + 2, 8, 8);
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
            this.onClick.run();
        }

        @Override
        public void setItemWidth(int width) {
            this.setWidth(width);
        }
    }
}
