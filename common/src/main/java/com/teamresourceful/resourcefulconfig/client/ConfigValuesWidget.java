package com.teamresourceful.resourcefulconfig.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.TooltipAccessor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import com.teamresourceful.resourcefulconfig.client.options.Options;
import com.teamresourceful.resourcefulconfig.common.annotations.Comment;
import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;

import java.util.List;
import java.util.Optional;

public class ConfigValuesWidget extends ContainerObjectSelectionList<ConfigValuesWidget.Entry> {

    public ConfigValuesWidget(Minecraft minecraft, int width, int height, int y0, int y1, int entryHeight) {
        super(minecraft, width, height, y0, y1, entryHeight);

        this.centerListVertically = false;
    }

    public void addSmall(List<? extends ResourcefulConfigEntry> entries) {
        for (ResourcefulConfigEntry entry : entries) {
            this.addEntry(new Entry(this.x0, this.x1, entry));
        }
    }

    public int getRowWidth() {
        return this.width;
    }

    protected int getScrollbarPosition() {
        return this.x1 - 6;
    }

    public Optional<TooltipAccessor> getMouseOver(double d, double e) {
        for (Entry value : this.children()) {
            if (value.isMouseOver(d, e) && !value.children.isMouseOver(d, e) && !value.reset.isMouseOver(d, e)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }


    @Environment(EnvType.CLIENT)
    protected static class Entry extends ContainerObjectSelectionList.Entry<ConfigValuesWidget.Entry> implements TooltipAccessor {

        private final int left;

        private final ResourcefulConfigEntry entry;
        private final Component label;
        private AbstractWidget children;
        private final AbstractWidget reset;

        private final Component tooltip;

        private Entry(int left, int right, ResourcefulConfigEntry entry) {
            this.entry = entry;
            this.left = left;
            var annotation = entry.field().getAnnotation(ConfigEntry.class);
            this.label = annotation == null ? CommonComponents.EMPTY : Component.translatable(annotation.translation());
            this.children = Options.create(right - 110, 0, 100, entry);
            this.reset = new Button(right - 140, 0, 20, 20, Component.literal("R"), (button) -> {
                this.entry.reset();
                this.children = Options.create(right - 110, 0, 100, entry);
            });
            this.reset.active = this.children.active;

            var comment = entry.field().getAnnotation(Comment.class);
            this.tooltip = comment == null ? CommonComponents.EMPTY : comment.translation().isEmpty() ? Component.literal(comment.value()) : Component.translatable(comment.translation());
        }

        public void render(@NotNull PoseStack stack, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {

            Font font = Minecraft.getInstance().font;
            font.draw(stack, this.label, left + 10, (float) (j + 5), 16777215);

            this.reset.y = j;
            this.reset.render(stack, n, o, f);

            this.children.y = j;
            this.children.render(stack, n, o, f);
        }

        public List<? extends GuiEventListener> children() {
            return List.of(this.children, this.reset);
        }

        public List<? extends NarratableEntry> narratables() {
            return List.of(this.children, this.reset);
        }

        @Override
        public List<FormattedCharSequence> getTooltip() {
            return Minecraft.getInstance().font.split(this.tooltip, 200);
        }
    }
}
