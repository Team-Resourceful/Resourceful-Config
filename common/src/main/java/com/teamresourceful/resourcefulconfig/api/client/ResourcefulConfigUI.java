package com.teamresourceful.resourcefulconfig.api.client;

import com.teamresourceful.resourcefulconfig.client.components.base.ContainerWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.CustomButton;
import com.teamresourceful.resourcefulconfig.client.components.base.SpriteButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ResourcefulConfigUI {

    /**
     * Opens a modal screen with the given title and constructor.
     *
     * @param title       The title of the modal screen.
     * @param constructor The constructor for the modal screen. This should be a lambda that returns a widget that contains the modal screen's content.
     * @apiNote The widget for this modal should contain all other widgets and should be used for rendering of inside the modal.
     * See {@link #container} for an example.
     */
    public static void openModal(Component title, ModalWidgetConstructor constructor) {
        new GenericModalOverlay(title, constructor).open();
    }

    /**
     * Create a widget that contains all other widgets.
     *
     * @param x      The x position of the widget.
     * @param y      The y position of the widget.
     * @param width  The width of the widget.
     * @param height The height of the widget.
     */
    public static AbstractWidget container(int x, int y, int width, int height, Layout layout) {
        return new ContainerWidget(x, y, width, height) {

            {
                positionUpdated();
            }

            @Override
            protected void positionUpdated() {
                this.clear();
                layout.setPosition(getX(), getY());
                layout.arrangeElements();
                layout.visitWidgets(this::addRenderableWidget);
            }
        };
    }

    /**
     * Create a button with the given text and onClick action.
     *
     * @param x       The x position of the button.
     * @param y       The y position of the button.
     * @param width   The width of the button.
     * @param height  The height of the button.
     * @param text    The text of the button.
     * @param onClick The action to run when the button is clicked.
     * @return The created button.
     */
    public static AbstractWidget button(int x, int y, int width, int height, Component text, Runnable onClick) {
        var button = new CustomButton(width, height, text, onClick);
        button.setPosition(x, y);
        return button;
    }

    /**
     * Create a button with the given sprite and onClick action.
     *
     * @param x       The x position of the button.
     * @param y       The y position of the button.
     * @param width   The width of the button.
     * @param height  The height of the button.
     * @param sprite  The sprite of the button.
     * @param tooltip The tooltip of the button.
     * @param onClick The action to run when the button is clicked.
     * @return The created button.
     */
    public static AbstractWidget button(int x, int y, int width, int height, ResourceLocation sprite, @Nullable Component tooltip, Runnable onClick) {
        var button = SpriteButton.builder(width, height)
                .padding(2)
                .sprite(sprite)
                .tooltip(tooltip)
                .onPress(onClick)
                .build();
        button.setPosition(x, y);
        return button;
    }

}
