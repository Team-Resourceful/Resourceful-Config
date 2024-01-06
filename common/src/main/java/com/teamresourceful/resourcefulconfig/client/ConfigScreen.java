package com.teamresourceful.resourcefulconfig.client;

import com.teamresourceful.resourcefulconfig.api.config.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.client.components.categories.CategoriesListWidget;
import com.teamresourceful.resourcefulconfig.client.components.categories.CategoryItem;
import com.teamresourceful.resourcefulconfig.client.components.header.HeaderWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.Options;
import com.teamresourceful.resourcefulconfig.client.components.options.OptionsListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.Nullable;

public class ConfigScreen extends Screen {

    private final Screen parent;
    private final ResourcefulConfig config;
    private OptionsListWidget optionsList;

    public ConfigScreen(Screen parent, ResourcefulConfig config) {
        super(CommonComponents.EMPTY);
        this.parent = parent;
        this.config = config;
    }

    @Override
    protected void rebuildWidgets() {
        OptionsListWidget oldList = this.optionsList;
        super.rebuildWidgets();
        this.optionsList.update(oldList);
    }

    @Override
    protected void init() {
        int contentWidth = this.width - UIConstants.PAGE_PADDING * 2;
        int contentHeight = this.height - UIConstants.PAGE_PADDING * 2;

        int optionsWidth = contentWidth;


        LinearLayout layout = LinearLayout
                .vertical()
                .spacing(UIConstants.PAGE_PADDING);

        var header = layout.addChild(new HeaderWidget(0, 0, this.width - UIConstants.PAGE_PADDING * 2, getFilename(), this.config));

        contentHeight -= header.getHeight() + UIConstants.PAGE_PADDING;

        LinearLayout body = layout.addChild(LinearLayout.horizontal().spacing(UIConstants.PAGE_PADDING));

        if (!this.config.categories().isEmpty()) {
            int categoryWidth = contentWidth / 4;
            var categoryList = body.addChild(new CategoriesListWidget(categoryWidth, contentHeight));
            for (ResourcefulConfig value : this.config.categories().values()) {
                categoryList.add(new CategoryItem(this, value));
            }
            optionsWidth = contentWidth - categoryWidth - UIConstants.PAGE_PADDING;
        }

        this.optionsList = body.addChild(new OptionsListWidget(optionsWidth, contentHeight));
        Options.populateOptions(this.optionsList, this.config.entries());

        layout.arrangeElements();
        layout.setPosition(UIConstants.PAGE_PADDING, UIConstants.PAGE_PADDING);
        layout.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.fill(0, 0, this.width, this.height, UIConstants.BACKGROUND);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.getChildAt(mouseX, mouseY).isEmpty()) {
            setFocused(null);
            return false;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public String getFilename() {
        if (!this.config.hasFile() && this.parent instanceof ConfigScreen screen) {
            return screen.getFilename();
        } else if (!this.config.hasFile()) {
            return "..";
        } else {
            return this.config.id();
        }
    }

    /**
     * @param screen The screen that is replacing this one.
     */
    public void removed(@Nullable Screen screen) {
        boolean shouldSave = screen == null || (screen == this.parent && !(this.parent instanceof ConfigScreen));
        if (shouldSave) {
            this.config.save();
        }
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.parent);
    }

    public Screen getParent() {
        return parent;
    }
}
