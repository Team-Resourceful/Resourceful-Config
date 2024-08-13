package com.teamresourceful.resourcefulconfig.common.compat.minecraft;

import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigColor;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigColorValue;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink;
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

public class DedicatedServerInfo implements ResourcefulConfigInfo {

    public static final DedicatedServerInfo INSTANCE = new DedicatedServerInfo();
    private static MinecraftServer server = null;

    public static void setServer(MinecraftServer server) {
        DedicatedServerInfo.server = server;
    }

    public static DedicatedServer getServer() {
        return server instanceof DedicatedServer dedicated ? dedicated : null;
    }

    @Override
    public TranslatableValue title() {
        return new TranslatableValue(
                "Dedicated Server",
                "rconfig.server.title"
        );
    }

    @Override
    public TranslatableValue description() {
        return new TranslatableValue(
                "Properties that can be can update real-time.",
                "rconfig.server.desc"
        );
    }

    @Override
    public String icon() {
        return "creeper";
    }

    @Override
    public ResourcefulConfigColor color() {
        return ResourcefulConfigColorValue.create("#FFFFFF");
    }

    @Override
    public ResourcefulConfigLink[] links() {
        return new ResourcefulConfigLink[] {
                ResourcefulConfigLink.create(
                        "https://minecraft.wiki/w/Server.properties",
                        "book-open",
                        new TranslatableValue(
                                "Wiki",
                                "rconfig.server.wiki"
                        )
                )
        };
    }

    @Override
    public boolean isHidden() {
        return !(server instanceof DedicatedServer);
    }
}
