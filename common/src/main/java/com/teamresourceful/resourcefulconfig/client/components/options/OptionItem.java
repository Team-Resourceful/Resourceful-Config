package com.teamresourceful.resourcefulconfig.client.components.options;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.base.ContainerWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.EqualSpacingLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.Component;

import java.util.List;

public class OptionItem extends ContainerWidget implements ListWidget.Item {

    private static final int PADDING = 10;

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
}
