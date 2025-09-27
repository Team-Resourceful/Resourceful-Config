package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigElementRenderer;
import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen;
import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigUI;
import com.teamresourceful.resourcefulconfig.api.client.options.ResourcefulConfigOptionUI;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.api.types.options.data.DraggableOptionEntry;
import com.teamresourceful.resourcefulconfig.client.ConfigsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.net.URI;
import java.util.List;

public class DemoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (!Demo.DEMO) return;

        ResourcefulConfigUI.registerElementRenderer(ResourceLocation.parse("demo:demo"), Renderer::new);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> {
            dispatcher.register(ClientCommandManager.literal("rconfigdemo").executes(context -> {
                Minecraft.getInstance().schedule(() -> Minecraft.getInstance().setScreen(
                        ResourcefulConfigScreen.get(null, Demo.configurator, DemoConfig.class)
                ));
                return 1;
            }));

            dispatcher.register(ClientCommandManager.literal("rconfigdemos").executes(context -> {
                Minecraft.getInstance().schedule(() -> Minecraft.getInstance().setScreen(
                        new ConfigsScreen(null, null)
                ));
                return 1;
            }));
        });
    }

    private record Renderer(ResourcefulConfigElement element) implements ResourcefulConfigElementRenderer {

        @Override
        public Component title() {
            return Component.literal("test");
        }

        @Override
        public Component description() {
            return Component.literal("test 2").withStyle(Style.EMPTY
                    .withClickEvent(new ClickEvent.OpenUrl(URI.create("https://teamresourceful.com")))
                    .withHoverEvent(new HoverEvent.ShowText(Component.literal(":3")))
            );
        }

        @Override
        public List<AbstractWidget> widgets() {
            return List.of(
                    ResourcefulConfigOptionUI.draggable(
                            Component.literal("dsadsa"),
                            List.of(
                                    new DraggableOptionEntry<>("meow", true),
                                    new DraggableOptionEntry<>("woof", false),
                                    new DraggableOptionEntry<>("quack", false),
                                    new DraggableOptionEntry<>("baa", false)
                            ),
                            () -> {
                                if (element instanceof ResourcefulConfigEntryElement entry && entry.entry() instanceof ResourcefulConfigValueEntry value) {
                                    return List.of(value.getString().split(","));
                                }
                                return List.of();
                            },
                            v -> {
                                if (element instanceof ResourcefulConfigEntryElement entry && entry.entry() instanceof ResourcefulConfigValueEntry value) {
                                    value.setString(String.join(",", v));
                                }
                            }
                    )
            );
        }
    }
}
