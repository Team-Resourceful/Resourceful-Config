package com.teamresourceful.resourcefulconfig.client.components.header;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.base.ContainerWidget;
import net.minecraft.client.gui.layouts.LinearLayout;

public class HeaderWidget extends ContainerWidget {

    public HeaderWidget(int width, ResourcefulConfig config, Runnable onSearchUpdate) {
        super(0, 0, width, 0);

        LinearLayout layout = LinearLayout
                .horizontal()
                .spacing(UIConstants.PAGE_PADDING);

        int controlsWidth = width / 4;
        int contentWidth = width - controlsWidth - UIConstants.PAGE_PADDING;

        var controls = layout.addChild(new HeaderControlsWidget(controlsWidth, config, onSearchUpdate));
        var content = layout.addChild(new HeaderContentWidget(contentWidth, config));

        layout.arrangeElements();
        layout.setPosition(this.getX() + UIConstants.PAGE_PADDING, this.getY()  + UIConstants.PAGE_PADDING);
        layout.visitWidgets(this::addRenderableWidget);

        this.height = layout.getHeight();

        controls.setHeight(this.height);
        content.setHeight(this.height);
    }

}
