package com.teamresourceful.resourcefulconfig.forge;

import com.teamresourceful.resourcefulconfig.web.server.WebServer;
import net.minecraftforge.fml.common.Mod;

@Mod("resourcefulconfig")
public class ResourcefulconfigForge {

    public ResourcefulconfigForge() {
        WebServer.start();
    }
}