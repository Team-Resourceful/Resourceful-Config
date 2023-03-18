package com.teamresourceful.resourcefulconfig.common.utils.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ModUtilsImpl {
    public static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }
}