package com.teamresourceful.resourcefulconfig.client.components.options;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.base.ContainerWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.MultiLineTextWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.EqualSpacingLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OptionItem extends ContainerWidget implements ListWidget.Item {

    protected static final int PADDING = 10;

    private final Component title;
    private final Component description;
    private final List<AbstractWidget> widgets;

    public OptionItem(ResourcefulConfigEntry entry, List<AbstractWidget> widgets) {
        this(
            entry.options().title().toComponent(),
            entry.options().comment().toComponent(),
            widgets
        );
    }

    public OptionItem(Component title, Component description, List<AbstractWidget> widgets) {
        super(0, 0, 0, 0);
        this.title = title.copy().withColor(UIConstants.TEXT_TITLE);
        this.description = description.copy().withColor(UIConstants.TEXT_PARAGRAPH);
        this.widgets = widgets;

        init();
    }

    public void init() {
        clear();

        Font font = Minecraft.getInstance().font;
        int half = (int) (this.width * 0.5f);

        EqualSpacingLayout layout = new EqualSpacingLayout(this.width - PADDING * 2, 0, EqualSpacingLayout.Orientation.HORIZONTAL);

        LinearLayout titleDesc = LinearLayout
                .vertical()
                .spacing(UIConstants.SPACING);

        titleDesc.addChild(
                new StringWidget(half, 9, this.title, font)
                        .alignLeft()
        );

        titleDesc.addChild(
                new MultiLineTextWidget(this.description, font)
                        .setCentered(false)
                        .setMaxWidth(half)
                        .configureStyleHandling(true, OptionItem::handleStyle)
        );

        LinearLayout options = LinearLayout
                .horizontal()
                .spacing(UIConstants.SPACING);

        for (AbstractWidget widget : widgets) {
            options.addChild(widget);
        }

        layout.addChild(titleDesc);
        layout.addChild(options, settings -> settings.alignVerticallyMiddle().alignHorizontallyRight());
        layout.arrangeElements();
        layout.setPosition(this.getX() + PADDING, this.getY() + PADDING);
        layout.visitWidgets(this::addRenderableWidget);
        this.height = layout.getHeight() + PADDING * 2;
    }

    @Override
    protected void positionUpdated() {
        init();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.getChildAt(mouseX, mouseY).isEmpty()) {
            setFocused(null);
            return false;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void setItemWidth(int width) {
        boolean changed = this.width != width;
        this.setWidth(width);
        if (!changed) return;
        init();
    }

    private static void handleStyle(@NotNull Style style) {
        var event = style.getClickEvent();
        var mc = Minecraft.getInstance();
        if (event != null) {
            switch (event) {
                case ClickEvent.CopyToClipboard clipboard -> mc.keyboardHandler.setClipboard(clipboard.value());
                case ClickEvent.OpenUrl link -> {
                    Screen screen = Minecraft.getInstance().screen;
                    if (screen == null) return;
                    ConfirmLinkScreen.confirmLinkNow(screen, link.uri());
                }
                default -> {}
            }
        }
    }
}
