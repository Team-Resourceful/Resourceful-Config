package com.teamresourceful.resourcefulconfig.common.utils;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ModLoaderServiceFabricImpl implements ModLoaderService {
    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public boolean isDev() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
}
