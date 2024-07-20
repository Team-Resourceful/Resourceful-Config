package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class Demo implements ModInitializer {

    public static final boolean DEMO = FabricLoader.getInstance().isDevelopmentEnvironment() || Boolean.getBoolean("rconfig.demo");

    public static final Configurator configurator = new Configurator("resourcefulconfig");

    @Override
    public void onInitialize() {
        if (!DEMO) return;
        System.out.println("Resourceful Config Demo is enabled!");

        configurator.register(DemoConfig.class, event -> event.register(1, json -> {
            json.add("demoString", json.get("oldString"));
            json.remove("oldString");
            return json;
        }));
    }
}
