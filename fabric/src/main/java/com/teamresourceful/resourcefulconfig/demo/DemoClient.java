package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen;
import com.teamresourceful.resourcefulconfig.client.ConfigsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;

public class DemoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (!Demo.DEMO) return;

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
}
