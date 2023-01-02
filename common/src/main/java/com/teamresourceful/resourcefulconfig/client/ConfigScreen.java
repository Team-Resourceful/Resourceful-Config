package com.teamresourceful.resourcefulconfig.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefulconfig.common.config.Configurator;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ConfigScreen extends Screen {

    private final String fileName;

    @Nullable
    private final ConfigScreen parent;

    protected final ResourcefulConfig config;
    private ConfigValuesWidget list;
    private CategoriesWidget categories;

    public ConfigScreen(@Nullable ConfigScreen screen, ResourcefulConfig config) {
        super(CommonComponents.EMPTY);
        this.parent = screen;
        this.config = config;
        this.fileName = getFileName();
    }

    public static boolean open(Class<?> configClass, Configurator configurator) {
        ResourcefulConfig config = configurator.getConfig(configClass);
        if (config != null) {
            Minecraft.getInstance().setScreen(new ConfigScreen(null, config));
            return true;
        }
        return false;
    }

    protected void init() {
        this.list = new ConfigValuesWidget(this.minecraft, (int) (this.width * 0.64), this.height, 32, this.height - 32, 25);
        this.list.setLeftPos((int) (this.width * 0.36));
        this.list.addSmall(new ArrayList<>(this.config.getEntries().values()));

        this.categories = new CategoriesWidget(this.minecraft, (int) (this.width * 0.35), this.height, 32, this.height - 32, 25);
        this.categories.addSmall(this, new ArrayList<>(this.config.getSubConfigs().values()));

        this.addRenderableWidget(this.list);
        this.addRenderableWidget(this.categories);
        this.createFooter();
    }

    protected void createFooter() {
        this.addRenderableWidget(
            Button.builder(this.parent == null ? CommonComponents.GUI_DONE : CommonComponents.GUI_BACK, (ignored) -> {
                if (this.parent == null) {
                    this.onClose();
                } else {
                    this.minecraft.setScreen(this.parent);
                }
            })
            .bounds(20, this.height - 27, (int) (this.width * 0.35) - 40, 20)
            .build()
        );
    }

    public void render(@NotNull PoseStack stack, int i, int j, float f) {
        this.renderDirtBackground(0);
        this.list.render(stack, i, j, f);
        this.categories.render(stack, i, j, f);
        drawCenteredString(stack, this.font, Component.literal("Categories").withStyle(ChatFormatting.BOLD), (int) (this.width * 0.36 / 2), 11, 16777215);
        drawCenteredString(stack, this.font, Component.literal("Options").withStyle(ChatFormatting.BOLD), (int) (this.width * 0.36 + this.width * 0.64 / 2), 11, 16777215);
        Component fileNameComponent = Component.literal(this.fileName).append(ArchitecturyTarget.getCurrentTarget().equals("fabric") ? ".json" : ".toml");

        drawString(stack, this.font, fileNameComponent, this.width - 10 - this.font.width(fileNameComponent), this.height - 20, 5592405);
        super.render(stack, i, j, f);

        this.list.getMouseOver(i, j)
            .map(TooltipAccessor::getTooltip)
            .ifPresent(tooltip -> this.renderTooltip(stack, tooltip, i, j));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public String getFileName() {
        if (parent != null) {
            return parent.getFileName();
        }
        return config.getFileName();
    }

    public void saveConfig() {
        if (this.parent != null) {
            this.parent.saveConfig();
            return;
        }
        this.config.save();
    }

    @Override
    public void onClose() {
        saveConfig();
        super.onClose();
    }
}
