package com.teamresourceful.resourcefulconfig.client;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigListEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigObjectEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.CustomButton;
import com.teamresourceful.resourcefulconfig.client.components.base.SpriteButton;
import com.teamresourceful.resourcefulconfig.client.components.options.OptionItem;
import com.teamresourceful.resourcefulconfig.client.components.options.Options;
import com.teamresourceful.resourcefulconfig.client.components.options.OptionsListWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.types.ObjectOptionWidget;
import com.teamresourceful.resourcefulconfig.client.screens.base.CloseableScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
public class ListScreen extends Screen implements CloseableScreen {

    private final Screen parent;
    private final ResourcefulConfigListEntry entry;

    public ListScreen(Screen parent, ResourcefulConfigListEntry entry) {
        super(CommonComponents.EMPTY);
        this.parent = parent;
        this.entry = entry;
    }

    @Override
    protected void init() {
        int contentWidth = this.width - UIConstants.PAGE_PADDING * 2;
        int headerHeight = 12 + UIConstants.PAGE_PADDING * 2;

        OptionsListWidget list = addRenderableWidget(
                new OptionsListWidget(contentWidth, this.height - headerHeight - UIConstants.PAGE_PADDING)
        );
        list.setPosition(UIConstants.PAGE_PADDING, headerHeight);

        for (int i = 0; i < entry.size(); i++) {
            final int index = i;
            ResourcefulConfigEntry itemEntry = entry.get(index);

            List<AbstractWidget> widgets = new ArrayList<>();

            if (itemEntry instanceof ResourcefulConfigObjectEntry objectEntry) {
                widgets.add(new ObjectOptionWidget(objectEntry));
            } else if (itemEntry instanceof ResourcefulConfigValueEntry valueEntry) {
                widgets.addAll(Options.entryWidgets(valueEntry));
            }

            widgets.add(SpriteButton.builder(12, 12)
                    .padding(2)
                    .sprite(ModSprites.CHEVRON_UP)
                    .tooltip(UIConstants.MOVE_UP)
                    .onPress(() -> {
                        if (index > 0) {
                            entry.move(index, index - 1);
                            rebuildWidgets();
                        }
                    })
                    .build());
            widgets.add(SpriteButton.builder(12, 12)
                    .padding(2)
                    .sprite(ModSprites.CHEVRON_DOWN)
                    .tooltip(UIConstants.MOVE_DOWN)
                    .onPress(() -> {
                        if (index < entry.size() - 1) {
                            entry.move(index, index + 1);
                            rebuildWidgets();
                        }
                    })
                    .build());
            widgets.add(SpriteButton.builder(12, 12)
                    .padding(2)
                    .sprite(ModSprites.RESET)
                    .tooltip(UIConstants.RESET)
                    .onPress(() -> {
                        entry.remove(index);
                        entry.add(index);
                        rebuildWidgets();
                    })
                    .build());
            widgets.add(SpriteButton.builder(12, 12)
                    .padding(2)
                    .sprite(ModSprites.DELETE)
                    .tooltip(UIConstants.REMOVE_ITEM)
                    .onPress(() -> {
                        entry.remove(index);
                        rebuildWidgets();
                    })
                    .build());

            list.add(new OptionItem(
                    entry.getTitle(index),
                    entry.getDescription(index),
                    widgets
            ));
        }

        addRenderableWidget(new CustomButton(
                96, 12,
                UIConstants.ADD_ITEM,
                () -> {
                    entry.add();
                    rebuildWidgets();
                }
        )).setPosition(UIConstants.PAGE_PADDING, UIConstants.PAGE_PADDING);

        addRenderableWidget(new CustomButton(
                96, 12,
                CommonComponents.GUI_DONE,
                this::onClose
        )).setPosition(this.width - 96 - UIConstants.PAGE_PADDING, UIConstants.PAGE_PADDING);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.fill(0, 0, this.width, this.height, UIConstants.BACKGROUND);
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean bl) {
        if (this.getChildAt(event.x(), event.y()).isEmpty()) {
            setFocused(null);
            return false;
        }
        return super.mouseClicked(event, bl);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
    }

    @Override
    public void onClosed(@Nullable Screen screen) {}
}