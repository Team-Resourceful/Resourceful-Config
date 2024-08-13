package com.teamresourceful.resourcefulconfig.common.fabric;

import com.teamresourceful.resourcefulconfig.common.compat.minecraft.DedicatedServerInfo;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class ResourcefulConfigFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(DedicatedServerInfo::setServer);
    }
}
