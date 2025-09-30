package com.teamresourceful.resourcefulconfig.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfig.client.components.categories.CategoriesListWidget;
import com.teamresourceful.resourcefulconfig.client.components.categories.CategoryItem;
import com.teamresourceful.resourcefulconfig.client.components.header.HeaderWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.Options;
import com.teamresourceful.resourcefulconfig.client.components.options.OptionsListWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.types.StringOptionWidget;
import com.teamresourceful.resourcefulconfig.client.screens.base.CloseableScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
public class ConfigScreen extends Screen implements CloseableScreen {

    private final Screen parent;
    private final ResourcefulConfig config;
    private final ConfigScreenContext context;

    private OptionsListWidget optionsList = null;
    private CategoriesListWidget categoriesList = null;

    private StringOptionWidget searchWidget = null;

    public ConfigScreen(Screen parent, ResourcefulConfig config) {
        this(parent, config, new ConfigScreenContext());
    }

    public ConfigScreen(Screen parent, ResourcefulConfig config, ConfigScreenContext context) {
        super(CommonComponents.EMPTY);
        this.parent = parent;
        this.config = config;
        this.context = context;
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

        var header = layout.addChild(new HeaderWidget(
                this.width - UIConstants.PAGE_PADDING * 2,
                this.config,
                this.context,
                () -> {
                    this.updateOptions();
                    this.updateCategories();
                }
        ));
        this.searchWidget = header.getSearchWidget();

        contentHeight -= header.getHeight() + UIConstants.PAGE_PADDING;

        LinearLayout body = layout.addChild(LinearLayout.horizontal().spacing(UIConstants.PAGE_PADDING));

        var categoriesEmpty = this.config.categories().isEmpty() || this.config.categories().values().stream()
                .allMatch(it -> it.info().isHidden());

        if (!categoriesEmpty) {
            int categoryWidth = contentWidth / 4;
            this.categoriesList = body.addChild(new CategoriesListWidget(categoryWidth, contentHeight));
            updateCategories();
            optionsWidth = contentWidth - categoryWidth - UIConstants.PAGE_PADDING;
        }

        this.optionsList = body.addChild(new OptionsListWidget(optionsWidth, contentHeight));
        updateOptions();

        layout.arrangeElements();
        layout.setPosition(UIConstants.PAGE_PADDING, UIConstants.PAGE_PADDING);
        layout.visitWidgets(this::addRenderableWidget);
    }

    public void updateOptions() {
        this.optionsList.clear();
        List<ResourcefulConfigElement> elements = new ArrayList<>();
        for (ResourcefulConfigElement element : this.config.elements()) {
            if (!this.context.fulfillsSearch(element)) continue;
            elements.add(element);
        }
        Options.populateOptions(this.optionsList, elements);
    }

    public void updateCategories() {
        if (this.categoriesList == null) return;
        this.categoriesList.clear();
        for (ResourcefulConfig value : this.config.categories().values()) {
            if (!this.context.fulfillsSearch(value)) continue;
            if (value.info().isHidden()) continue;
            this.categoriesList.add(new CategoryItem(this, value, this.context));
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.fill(0, 0, this.width, this.height, UIConstants.BACKGROUND);
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean bl) {
        if (this.getChildAt(event.x(), event.y()).isEmpty()) {
            setFocused(null);
            return false;
        }
        return super.mouseClicked(event, bl);
    }

    @Override
    public boolean keyPressed(@NotNull KeyEvent event) {
        if (Screen.hasControlDown() && event.input() == InputConstants.KEY_F && this.searchWidget != null) {
            this.setFocused(this.searchWidget);
            this.searchWidget.setFocused(true);
            return true;
        }
        if (super.keyPressed(event)) {
            return true;
        }
        if (event.input() == InputConstants.KEY_ESCAPE) {
            this.onClose();
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void onClosed(@Nullable Screen screen) {
        boolean shouldSave = screen == null || (screen == this.parent && !(this.parent instanceof ConfigScreen));
        if (shouldSave) {
            this.config.save();
        }
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.parent);
    }
}
