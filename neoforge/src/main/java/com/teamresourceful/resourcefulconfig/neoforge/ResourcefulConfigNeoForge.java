package com.teamresourceful.resourcefulconfig.neoforge;

import com.teamresourceful.resourcefulconfig.web.server.WebServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLLoader;

@Mod("resourcefulconfig")
public class ResourcefulConfigNeoForge {

    public ResourcefulConfigNeoForge(IEventBus bus, FMLModContainer container) {
        WebServer.start();
        ResourcefulConfigNeoForgeClient.onClientInit(container);
        bus.addListener(ResourcefulConfigNeoForge::onCommonSetup);
    }

    public static void onCommonSetup(FMLLoadCompleteEvent event) {
        if (FMLLoader.getDist().isClient()) {
            ResourcefulConfigNeoForgeClient.onClientComplete();
        }
    }
}