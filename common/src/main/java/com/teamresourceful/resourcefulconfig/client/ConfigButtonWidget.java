package com.teamresourceful.resourcefulconfig.client;

import com.google.common.collect.Lists;
import com.teamresourceful.resourcefulconfig.common.annotations.Comment;
import com.teamresourceful.resourcefulconfig.common.annotations.ConfigButton;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfigButtonWidget extends ValueWidget {

    private final int left;

    private final Component label;
    private final AbstractWidget children;

    private final Component tooltip;

    public ConfigButtonWidget(int left, int right, ResourcefulConfigButton button) {
        this.left = left;
        var annotation = button.getAnnotation(ConfigButton.class);
        this.label = annotation == null ? CommonComponents.EMPTY : Component.translatable(annotation.translation());
        Component text = annotation == null ? CommonComponents.EMPTY : Component.translatable(annotation.text());
        this.children = Button.builder(text, widget -> button.invoke()).bounds(right - 110, 0, 100, 20).build();

        var comment = button.method().getAnnotation(Comment.class);
        this.tooltip = comment == null ? CommonComponents.EMPTY : comment.translation().isEmpty() ? Component.literal(comment.value()) : Component.translatable(comment.translation());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {

        Font font = Minecraft.getInstance().font;
        graphics.drawString(font, this.label, left + 10, j + 5, 16777215, false);

        this.children.setY(j);
        this.children.render(graphics, n, o, f);
    }

    public @NotNull List<? extends GuiEventListener> children() {
        return Lists.newArrayList(this.children);
    }

    public @NotNull List<? extends NarratableEntry> narratables() {
        return Lists.newArrayList(this.children);
    }

    @Override
    public List<FormattedCharSequence> getTooltip() {
        if (this.children.isHovered()) return List.of();
        return Minecraft.getInstance().font.split(this.tooltip, 200);
    }
}
