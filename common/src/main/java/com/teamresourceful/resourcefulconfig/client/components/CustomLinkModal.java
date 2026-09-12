package com.teamresourceful.resourcefulconfig.client.components;

import com.mojang.blaze3d.Blaze3D;
import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigUI;
import com.teamresourceful.resourcefulconfig.client.components.base.MultiLineTextWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.net.URI;

public class CustomLinkModal {

    public static void open(URI link) {
        open(link.toString());
    }

    public static void open(String link) {
        ResourcefulConfigUI.openModal(
                Component.literal("Open Link"),
                (x, y, width, height) -> {
                    var layout = LinearLayout.vertical();
                    var font = Minecraft.getInstance().font;

                    layout.addChild(
                            new StringWidget(
                                    Component.literal("Do you want to open this link?").withColor(0xFFFFFF),
                                    font
                            ),
                            layout.newCellSettings().alignHorizontallyCenter()
                    );
                    layout.addChild(SpacerElement.height(5));
                    layout.addChild(
                            new MultiLineTextWidget(
                                    Component.literal(link).withColor(0x5555FF),
                                    font
                            ),
                            layout.newCellSettings().alignHorizontallyCenter()
                    );
                    layout.addChild(SpacerElement.height(15));

                    var padding = width * 0.15f;
                    var buttons = LinearLayout.horizontal().spacing(5);
                    var buttonWidth = (width / 2f) - 5 - padding;

                    buttons.addChild(ResourcefulConfigUI.button(
                            0, 0, (int) buttonWidth, 20,
                            CommonComponents.GUI_OPEN_IN_BROWSER,
                            () -> {
                                Blaze3D.openUri(URI.create(link));
                                CustomLinkModal.close();
                            }
                    ));

                    buttons.addChild(ResourcefulConfigUI.button(
                            0, 0, (int) buttonWidth, 20,
                            CommonComponents.GUI_COPY_TO_CLIPBOARD,
                            () -> {
                                Minecraft.getInstance().keyboardHandler.setClipboard(link);
                                CustomLinkModal.close();
                            }
                    ));

                    layout.addChild(buttons);

                    return ResourcefulConfigUI.container(x, y, width, height, layout);
                }
        );
    }

    private static void close() {
        var screen = Minecraft.getInstance().gui.screen();
        if (screen == null) return;
        screen.onClose();
    }
}
