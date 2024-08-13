package com.teamresourceful.resourcefulconfig.common.compat;

import com.teamresourceful.resourcefulconfig.common.compat.minecraft.DedicatedServerConfig;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;

public class CompatabilityLayers {

    public static void init() {
        Configurations.INSTANCE.addConfig(DedicatedServerConfig.INSTANCE, "minecraft");

    }
}
