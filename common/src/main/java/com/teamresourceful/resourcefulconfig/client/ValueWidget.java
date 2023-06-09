package com.teamresourceful.resourcefulconfig.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ValueWidget extends ContainerObjectSelectionList.Entry<ValueWidget> implements TooltipAccessor {

    @Override
    public @NotNull List<? extends NarratableEntry> narratables() {
        return List.of();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {

    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return List.of();
    }

    @Override
    public List<FormattedCharSequence> getTooltip() {
        return List.of();
    }
}
