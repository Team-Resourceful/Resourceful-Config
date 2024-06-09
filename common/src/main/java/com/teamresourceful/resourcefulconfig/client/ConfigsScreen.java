package com.teamresourceful.resourcefulconfig.client;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.client.components.configs.ConfigHeaderItem;
import com.teamresourceful.resourcefulconfig.client.components.configs.ConfigItem;
import com.teamresourceful.resourcefulconfig.client.components.configs.ConfigsListWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.OptionsListWidget;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class ConfigsScreen extends Screen {

    private final Screen parent;
    private ConfigsListWidget configs;
    private @Nullable String modid;

    public ConfigsScreen(Screen parent, @Nullable String modid) {
        this(parent);
        this.modid = modid;
    }

    public ConfigsScreen(Screen parent) {
        super(CommonComponents.EMPTY);
        this.parent = parent;
    }

    @Override
    protected void rebuildWidgets() {
        ConfigsListWidget oldList = this.configs;
        super.rebuildWidgets();
        this.configs.update(oldList);
    }

    @Override
    protected void init() {
        int contentWidth = this.width - UIConstants.PAGE_PADDING * 2;
        int contentHeight = this.height - UIConstants.PAGE_PADDING * 2;

        this.configs = addRenderableWidget(new ConfigsListWidget(contentWidth, contentHeight));
        this.configs.setPosition(UIConstants.PAGE_PADDING, UIConstants.PAGE_PADDING);
        this.configs.add(new ConfigHeaderItem());

        if (this.modid == null) {
            for (ResourcefulConfig value : Configurations.INSTANCE.configs().values()) {
                this.configs.add(new ConfigItem(value));
            }
        } else {
            Set<String> configs = Configurations.INSTANCE.modToConfigs().getOrDefault(this.modid, Set.of());
            for (String config : configs) {
                this.configs.add(new ConfigItem(Configurations.INSTANCE.configs().get(config)));
            }
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
    public void onClose() {
        Minecraft.getInstance().setScreen(this.parent);
    }
}
