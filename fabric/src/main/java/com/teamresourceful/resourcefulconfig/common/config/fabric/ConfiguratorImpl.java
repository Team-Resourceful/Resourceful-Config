package com.teamresourceful.resourcefulconfig.common.config.fabric;

import com.teamresourceful.resourcefulconfig.common.config.ConfigLoader;

public class ConfiguratorImpl {
    public static ConfigLoader getCreator(boolean forceLoad) {
        return new FabricConfigLoader();
    }
}
