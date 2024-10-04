package com.teamresourceful.resourcefulconfig.common.utils.neoforge;

import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ModUtilsImpl {
    public static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static boolean isDev() {
        return !FMLLoader.isProduction();
    }
}
