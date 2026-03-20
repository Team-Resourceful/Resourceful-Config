package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigListEntry;
import com.teamresourceful.resourcefulconfig.client.ListScreen;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import org.jetbrains.annotations.NotNull;

public class ListOptionWidget extends BaseWidget {

    private static final int WIDTH = 80;
    private static final int SIZE = 12;
    private static final int SPACING = 4;
    private static final int PADDING = 2;

    private final ResourcefulConfigListEntry entry;

    public ListOptionWidget(ResourcefulConfigListEntry entry) {
        super(WIDTH, 16);
        this.entry = entry;
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.ofButton(this.isHovered()), getX(), getY(), getWidth(), getHeight());

        int contentWidth = font.width(UIConstants.EDIT) + SPACING + SIZE;

        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                ModSprites.EDIT,
                getX() + (getWidth() - contentWidth) / 2, getY() + PADDING,
                SIZE, SIZE
        );
        graphics.text(
                font, UIConstants.EDIT,
                getX() + (getWidth() - contentWidth) / 2 + SIZE + SPACING,
                getY() + (getHeight() - font.lineHeight) / 2 + 1,
                UIConstants.TEXT_TITLE
        );

        this.applyCursor(graphics);
    }

    @Override
    public void onClick(@NotNull MouseButtonEvent event, boolean bl) {
        Minecraft.getInstance().setScreen(new ListScreen(Minecraft.getInstance().screen, entry));
    }
}