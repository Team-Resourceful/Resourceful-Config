package com.teamresourceful.resourcefulconfig.client;

import com.teamresourceful.resourcefulconfig.common.annotations.ConfigSeparator;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigButton;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

import java.util.*;

public class ConfigValuesWidget extends ContainerObjectSelectionList<ValueWidget> {

    public ConfigValuesWidget(Minecraft minecraft, int width, int height, int y0, int y1, int entryHeight) {
        super(minecraft, width, height, y0, y1, entryHeight);

        this.centerListVertically = false;
    }

    public void addSmall(Map<String, ? extends ResourcefulConfigEntry> entries, List<? extends ResourcefulConfigButton> buttons) {
        Map<String, List<ResourcefulConfigButton>> mappedButtons = new HashMap<>();
        for (ResourcefulConfigButton button : buttons) {
            String after = button.after();
            if (after.isBlank()) continue;
            mappedButtons.computeIfAbsent(after, s -> new ArrayList<>()).add(button);
        }


        for (var entry : entries.entrySet()) {
            ResourcefulConfigEntry value = entry.getValue();
            ConfigSeparator separator = value.field().getAnnotation(ConfigSeparator.class);
            if (separator != null) {
                this.addEntry(new SeparatorValueWidget(this.x0, this.x1, separator));
            }
            this.addEntry(new ConfigValueWidget(this.x0, this.x1, value));

            if (mappedButtons.containsKey(entry.getKey())) {
                for (ResourcefulConfigButton button : mappedButtons.get(entry.getKey())) {
                    this.addEntry(new ConfigButtonWidget(this.x0, this.x1, button));
                }
            }
        }

        for (var button : buttons) {
            if (button.after().isBlank()) {
                this.addEntry(new ConfigButtonWidget(this.x0, this.x1, button));
            }
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
                if (entry.getReset().isMouseOver(d, e)) {
                    return Optional.of(() -> List.of(Language.getInstance().getVisualOrder(Component.translatable("controls.reset"))));
                }
            } else if (value.isMouseOver(d, e)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
