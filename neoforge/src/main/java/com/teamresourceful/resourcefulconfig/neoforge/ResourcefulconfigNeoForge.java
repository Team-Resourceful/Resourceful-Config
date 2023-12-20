package com.teamresourceful.resourcefulconfig.neoforge;

import com.teamresourceful.resourcefulconfig.web.server.WebServer;
import net.neoforged.fml.common.Mod;

@Mod("resourcefulconfig")
public class ResourcefulconfigNeoForge {

    public ResourcefulconfigNeoForge() {
        WebServer.start();
    }
}