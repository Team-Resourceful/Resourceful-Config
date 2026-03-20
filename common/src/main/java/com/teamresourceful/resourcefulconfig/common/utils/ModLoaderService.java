package com.teamresourceful.resourcefulconfig.common.utils;

import java.nio.file.Path;

@PlatformService
public interface ModLoaderService {

    Path getConfigPath();
    boolean isDev();
    boolean isModLoaded(String id);

    static ModLoaderService create() {
        throw new AssertionError("Platform service not implemented");
    }
}
