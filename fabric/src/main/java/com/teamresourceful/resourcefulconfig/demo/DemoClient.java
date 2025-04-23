package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigElementRenderer;
import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen;
import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigUI;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.client.ConfigsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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
            return Component.literal("test 2");
        }

        @Override
        public List<AbstractWidget> widgets() {
            return List.of(
                    Button.builder(
                            Component.literal("dsadsa"),
                            b -> {
                                if (element instanceof ResourcefulConfigEntryElement entry && entry.entry() instanceof ResourcefulConfigValueEntry value) {
                                    value.setInt(value.getInt() + 1);
                                }
                            }
                    ).size(300, 20).build()
            );
        }
    }
}
