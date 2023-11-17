package com.teamresourceful.resourcefulconfig.forge;

import com.teamresourceful.resourcefulconfig.web.server.WebServer;
import net.neoforged.fml.common.Mod;

@Mod("resourcefulconfig")
public class ResourcefulconfigForge {

    public ResourcefulconfigForge() {
        WebServer.start();
    }
}