package com.teamresourceful.resourcefulconfig.client.compat.fabric.modmenu;

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import java.util.HashMap;
import java.util.Map;

public class ResourcefulConfigModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ResourcefulConfigScreen.getFactory(null)::apply;
    }

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> map = new HashMap<>();
        for (String mod : Configurations.INSTANCE.getModIds()) {
            map.put(mod, ResourcefulConfigScreen.getFactory(mod)::apply);
        }
        return map;
    }
}
