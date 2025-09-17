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
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DropdownWidget<T> extends BaseWidget {

    private static final int MIN_WIDTH = 80;
    private static final int MAX_WIDTH = MIN_WIDTH * 2;

    private final Component title;
    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private List<T> options;


    public DropdownWidget(Component title, List<T> options, Supplier<T> getter, Consumer<T> setter) {
        super(MIN_WIDTH, 16);
        this.title = title;
        this.options = List.copyOf(options);
        this.getter = getter;
        this.setter = setter;
    }

    public DropdownWidget<T> setOptions(List<T> options) {
        this.options = options;
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
        Minecraft.getInstance().setScreen(new DropdownOverlay<>(this));
    }

    private static class DropdownOverlay<T> extends OverlayScreen {

        private final DropdownWidget<T> widget;

        protected DropdownOverlay(DropdownWidget<T> widget) {
            super(Minecraft.getInstance().screen);
            this.widget = widget;
        }

        @Override
        protected void init() {
            var list = addRenderableWidget(DropdownList.of(widget));
            for (T option : widget.options) {
                list.add(new DropdownItem(option, () -> {
                    widget.setter.accept(option);
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

        private final int ogX;

        public DropdownList(int x, int y, int height) {
            super(x + 1, y, MIN_WIDTH - 2, height);

            this.ogX = x;
        }

        public static <T> DropdownList of(DropdownWidget<T> widget) {
            int windowHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            int widgetY = widget.getY() + widget.getHeight();
            int listHeight = Math.min(widget.options.size() * 12, 12 * 8) + 1;
            if (widgetY + listHeight > windowHeight) {
                widgetY = widget.getY() - listHeight - 1;
            }
            return new DropdownList(widget.getX(), widgetY, listHeight);
        }

        @Override
        public void add(Item item) {
            super.add(item);
            if (!(item instanceof DropdownItem it)) return;
            var addition = this.items.size() * 12 > this.height ? 10 : 0;
            if (it.effectiveWidth() + addition <= this.width) return;

            this.setWidth(Math.min(it.effectiveWidth() + addition, DropdownWidget.MAX_WIDTH));
            this.setX(this.ogX - (this.width - MIN_WIDTH) - 1);
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            graphics.blitSprite(RenderType::guiTextured, ModSprites.ACCENT, getX() - 1, getY() - 1, getWidth() + 2, getHeight() + 2);
            graphics.blitSprite(RenderType::guiTextured, ModSprites.BUTTON, getX(), getY(), getWidth(), getHeight());
            super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        }
    }

    private static class DropdownItem extends BaseWidget implements ListWidget.Item {

        private final Object option;
        private final Runnable onClick;

        public DropdownItem(Object option, Runnable setter) {
            super(MIN_WIDTH, 12);
            this.option = option;
            this.onClick = setter;
        }

        public int effectiveWidth() {
            return Mth.clamp(Minecraft.getInstance().font.width(Translatable.toComponent(this.option)) + 8, MIN_WIDTH, MAX_WIDTH);
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
            this.onClick.run();
        }

        @Override
        public void setItemWidth(int width) {
            this.setWidth(width);
        }
    }
}
