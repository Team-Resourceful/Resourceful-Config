package com.teamresourceful.resourcefulconfig.client;

import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import org.jetbrains.annotations.NotNull;

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
            this.children = Button.builder(category.getDisplayName(), (button) -> Minecraft.getInstance().setScreen(new ConfigScreen(parent, category)))
                    .bounds(i, 0, width - 40, 20)
                    .build();
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
            this.children.setY(j);
            this.children.render(graphics, n, o, f);
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return List.of(this.children);
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return List.of(this.children);
        }
    }
}
