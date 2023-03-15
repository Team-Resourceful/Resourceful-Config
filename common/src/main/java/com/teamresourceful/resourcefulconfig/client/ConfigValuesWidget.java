package com.teamresourceful.resourcefulconfig.client;

import com.teamresourceful.resourcefulconfig.common.annotations.ConfigSeparator;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

public class ConfigValuesWidget extends ContainerObjectSelectionList<ValueWidget> {

    public ConfigValuesWidget(Minecraft minecraft, int width, int height, int y0, int y1, int entryHeight) {
        super(minecraft, width, height, y0, y1, entryHeight);

        this.centerListVertically = false;
    }

    public void addSmall(List<? extends ResourcefulConfigEntry> entries) {
        for (ResourcefulConfigEntry entry : entries) {
            ConfigSeparator separator = entry.field().getAnnotation(ConfigSeparator.class);
            if (separator != null) {
                this.addEntry(new SeparatorValueWidget(this.x0, this.x1, separator));
            }
            this.addEntry(new ConfigValueWidget(this.x0, this.x1, entry));
        }
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.x1 - 6;
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        for (ValueWidget child : children()) {
            if (child instanceof ConfigValueWidget value && value.getChildren().isFocused() && !value.getChildren().isMouseOver(d, e)) {
                value.getChildren().setFocused(false);
                value.setFocused(null);
            }
        }
        return super.mouseClicked(d, e, i);
    }

    public Optional<TooltipAccessor> getMouseOver(double d, double e) {
        for (ValueWidget value : this.children()) {
            if (value instanceof ConfigValueWidget entry) {
                if (entry.isMouseOver(d, e) && !entry.getChildren().isMouseOver(d, e) && !entry.getReset().isMouseOver(d, e)) {
                    return Optional.of(entry);
                }
                if (((ConfigValueWidget) value).getReset().isMouseOver(d, e)) {
                    return Optional.of(() -> List.of(Language.getInstance().getVisualOrder(Component.translatable("controls.reset"))));
                }
            } else if (value.isMouseOver(d, e)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
