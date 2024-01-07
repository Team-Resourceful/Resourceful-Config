package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen;
import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public class Demo implements ClientModInitializer {

    public static final boolean DEMO = FabricLoader.getInstance().isDevelopmentEnvironment() || Boolean.getBoolean("rconfig.demo");


    @Override
    public void onInitializeClient() {
        if (!DEMO) return;
        System.out.println("Resourceful Config Demo is enabled!");

        Configurator configurator = new Configurator();
        configurator.register(DemoConfig.class);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) ->
            dispatcher.register(ClientCommandManager.literal("rconfigdemo").executes(context -> {
                Minecraft.getInstance().tell(() -> Minecraft.getInstance().setScreen(
                    ResourcefulConfigScreen.get(null, configurator, DemoConfig.class)
                ));
                return 1;
            }))
        );
    }
}
