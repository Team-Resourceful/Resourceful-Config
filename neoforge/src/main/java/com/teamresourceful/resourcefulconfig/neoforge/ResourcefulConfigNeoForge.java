package com.teamresourceful.resourcefulconfig.neoforge;

import com.teamresourceful.resourcefulconfig.common.compat.CompatabilityLayers;
import com.teamresourceful.resourcefulconfig.common.compat.minecraft.DedicatedServerInfo;
import com.teamresourceful.resourcefulconfig.web.server.WebServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod("resourcefulconfig")
public class ResourcefulConfigNeoForge {

    public ResourcefulConfigNeoForge(IEventBus bus, FMLModContainer container) {
        WebServer.start();
        if (FMLLoader.getDist().isDedicatedServer()) {
            CompatabilityLayers.initServer();
        }
        if (FMLLoader.getDist().isClient()) {
            ResourcefulConfigNeoForgeClient.onClientInit(container);
        }
        bus.addListener(ResourcefulConfigNeoForge::onCommonSetup);
        NeoForge.EVENT_BUS.addListener(ResourcefulConfigNeoForge::onServerStarted);
    }

    public static void onCommonSetup(FMLLoadCompleteEvent event) {
        if (FMLLoader.getDist().isClient()) {
            ResourcefulConfigNeoForgeClient.onClientComplete();
        }
    }

    public static void onServerStarted(ServerStartedEvent event) {
        DedicatedServerInfo.setServer(event.getServer());
    }
}