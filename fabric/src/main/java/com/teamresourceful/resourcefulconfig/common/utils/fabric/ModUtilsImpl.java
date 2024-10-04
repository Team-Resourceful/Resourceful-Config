package com.teamresourceful.resourcefulconfig.common.utils.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ModUtilsImpl {
    public static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static boolean isDev() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
