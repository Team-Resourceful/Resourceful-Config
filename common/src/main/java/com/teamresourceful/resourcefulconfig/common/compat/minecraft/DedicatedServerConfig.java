package com.teamresourceful.resourcefulconfig.common.compat.minecraft;

import com.teamresourceful.resourcefulconfig.api.patching.ConfigPatchEvent;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import com.teamresourceful.resourcefulconfig.common.loader.elements.ParsedEntryElement;
import com.teamresourceful.resourcefulconfig.mixins.common.PlayerListAccessor;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

public class DedicatedServerConfig implements ResourcefulConfig {

    public static final DedicatedServerConfig INSTANCE = new DedicatedServerConfig();
    private static final List<ResourcefulConfigElement> ELEMENTS = Util.make(new ArrayList<>(), entries -> {

        entries.add(new ParsedEntryElement("pvp", new DedicatedServerEntry("pvp", true, MinecraftServer::setPvpAllowed)));
        entries.add(new ParsedEntryElement("allow-flight", new DedicatedServerEntry("allow-flight", false, MinecraftServer::setFlightAllowed)));

        entries.add(new ParsedEntryElement("motd", new DedicatedServerEntry("motd", "A Minecraft Server", MinecraftServer::setMotd)));

        entries.add(new ParsedEntryElement("white-list", new DedicatedServerEntry("white-list", false, (server, value) -> {
            server.getPlayerList().setUsingWhiteList(value);
            server.kickUnlistedPlayers(server.createCommandSourceStack());
        })));
        entries.add(new ParsedEntryElement("enforce-whitelist", new DedicatedServerEntry("enforce-whitelist", false, (server, value) -> {
            server.setEnforceWhitelist(value);
            server.kickUnlistedPlayers(server.createCommandSourceStack());
        })));

        entries.add(new ParsedEntryElement("max-players", new DedicatedServerEntry("max-players", 20, (server, value) ->
                ((PlayerListAccessor)server.getPlayerList()).setMaxPlayers(value))
        ));
    });
    private static final LinkedHashMap<String, ResourcefulConfig> CATEGORIES = new LinkedHashMap<>();

    @Override
    public @NotNull List<ResourcefulConfigElement> elements() {
        return ELEMENTS;
    }

    @Override
    public @NotNull LinkedHashMap<String, ResourcefulConfig> categories() {
        return CATEGORIES;
    }

    @Override
    public @NotNull ResourcefulConfigInfo info() {
        return DedicatedServerInfo.INSTANCE;
    }

    @Override
    public String id() {
        return "minecraft/dedicated_server";
    }

    @Override
    public void save() {

    }

    @Override
    public void load(Consumer<ConfigPatchEvent> handler) {

    }
}
