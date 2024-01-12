package com.teamresourceful.resourcefulconfig.forge;

import com.teamresourceful.resourcefulconfig.web.server.WebServer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod("resourcefulconfig")
public class ResourcefulconfigForge {

    public ResourcefulconfigForge() {
        WebServer.start();
        if (FMLLoader.getDist().isClient()) {
            ResourcefulconfigForgeClient.onClientInit(ModLoadingContext.get().getActiveContainer());
        }
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ResourcefulconfigForge::onCommonSetup);
    }

    public static void onCommonSetup(FMLLoadCompleteEvent event) {
        if (FMLLoader.getDist().isClient()) {
            ResourcefulconfigForgeClient.onClientComplete();
        }
    }
}