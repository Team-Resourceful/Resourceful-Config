package com.teamresourceful.resourcefulconfig.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;

import java.util.List;

public class CategoriesWidget extends ContainerObjectSelectionList<CategoriesWidget.Entry> {

    public CategoriesWidget(Minecraft minecraft, int width, int height, int y0, int y1, int entryHeight) {
        super(minecraft, width, height, y0, y1, entryHeight);

        this.centerListVertically = false;
    }

    public void addSmall(ConfigScreen parent, List<? extends ResourcefulConfig> entries) {
        for (ResourcefulConfig category : entries) {
            this.addEntry(new Entry(this.x0 + 20, this.width, parent, category));
        }
    }

    public int getRowWidth() {
        return this.width;
    }

    protected int getScrollbarPosition() {
        return this.x1 - 6;
    }


    @Environment(EnvType.CLIENT)
    protected static class Entry extends ContainerObjectSelectionList.Entry<CategoriesWidget.Entry> {
        private final AbstractWidget children;

        private Entry(int i, int width, ConfigScreen parent, ResourcefulConfig category) {
            this.children = new Button(i, 0, width - 40, 20, category.getDisplayName(),
                    (button) -> Minecraft.getInstance().setScreen(new ConfigScreen(parent, category))
            );
        }

        public void render(@NotNull PoseStack stack, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
            this.children.y = j;
            this.children.render(stack, n, o, f);
        }

        public List<? extends GuiEventListener> children() {
            return List.of(this.children);
        }

        public List<? extends NarratableEntry> narratables() {
            return List.of(this.children);
        }
    }
}
