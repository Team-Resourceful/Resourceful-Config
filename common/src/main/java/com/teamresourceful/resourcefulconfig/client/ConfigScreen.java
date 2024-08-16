package com.teamresourceful.resourcefulconfig.client;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigButton;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.client.components.categories.CategoriesListWidget;
import com.teamresourceful.resourcefulconfig.client.components.categories.CategoryItem;
import com.teamresourceful.resourcefulconfig.client.components.header.HeaderWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.Options;
import com.teamresourceful.resourcefulconfig.client.components.options.OptionsListWidget;
import com.teamresourceful.resourcefulconfig.client.screens.base.CloseableScreen;
import com.teamresourceful.resourcefulconfig.client.utils.ConfigSearching;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ConfigScreen extends Screen implements CloseableScreen {

    private final Screen parent;
    private final ResourcefulConfig config;
    private final Function<String, List<String>> termCollector;

    private OptionsListWidget optionsList = null;
    private CategoriesListWidget categoriesList = null;

    public ConfigScreen(Screen parent, ResourcefulConfig config) {
        this(parent, config, s -> List.of());
    }

    public ConfigScreen(Screen parent, ResourcefulConfig config, Function<String, List<String>> termCollector) {
        super(CommonComponents.EMPTY);
        this.parent = parent;
        this.config = config;
        this.termCollector = termCollector;
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
                () -> {
                    this.updateOptions();
                    this.updateCategories();
                }
        ));

        contentHeight -= header.getHeight() + UIConstants.PAGE_PADDING;

        LinearLayout body = layout.addChild(LinearLayout.horizontal().spacing(UIConstants.PAGE_PADDING));

        if (!this.config.categories().isEmpty()) {
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
        Map<String, ResourcefulConfigEntry> entries = new LinkedHashMap<>();
        this.config.entries().forEach((key, value) -> {
            if (!ConfigSearching.fulfillsSearch(value, this.termCollector)) return;
            entries.put(key, value);
        });
        List<ResourcefulConfigButton> buttons = new ArrayList<>();
        this.config.buttons().forEach(button -> {
            if (!ConfigSearching.fulfillsSearch(button, this.termCollector)) return;
            buttons.add(button);
        });

        Options.populateOptions(this.optionsList, entries, buttons);
    }

    public void updateCategories() {
        if (this.categoriesList == null) return;
        this.categoriesList.clear();
        for (ResourcefulConfig value : this.config.categories().values()) {
            if (!ConfigSearching.fulfillsSearch(value, this.termCollector)) continue;
            this.categoriesList.add(new CategoryItem(this, value, this.termCollector));
        }
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

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (super.keyPressed(i, j, k)) {
            return true;
        }
        if (i == 256) {
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
