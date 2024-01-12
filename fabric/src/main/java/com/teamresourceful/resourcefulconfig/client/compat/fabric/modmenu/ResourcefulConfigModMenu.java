package com.teamresourceful.resourcefulconfig.client.compat.fabric.modmenu;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import java.util.HashMap;
import java.util.Map;

public class ResourcefulConfigModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return new MultiConfigFactory(null);
    }

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> map = new HashMap<>();
        for (var entry : Configurations.INSTANCE.modToConfigs().entrySet()) {
            String modid = entry.getKey();
            if (entry.getValue().size() == 1) {
                ResourcefulConfig config = Configurations.INSTANCE.configs().get(entry.getValue().get(0));
                if (config == null) continue;
                map.put(modid, new SingleConfigFactory(config));
            } else {
                map.put(modid, new MultiConfigFactory(modid));
            }
        }
        return map;
    }
}
