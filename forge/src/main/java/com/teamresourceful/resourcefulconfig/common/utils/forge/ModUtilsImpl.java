package com.teamresourceful.resourcefulconfig.common.utils.forge;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ModUtilsImpl {
    public static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }
}