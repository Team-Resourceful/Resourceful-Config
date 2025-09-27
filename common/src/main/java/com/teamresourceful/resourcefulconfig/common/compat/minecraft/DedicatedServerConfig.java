package com.teamresourceful.resourcefulconfig.common.compat.minecraft;

import com.teamresourceful.resourcefulconfig.api.patching.ConfigPatchEvent;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import com.teamresourceful.resourcefulconfig.common.loader.elements.ParsedEntryElement;
import net.minecraft.Util;
import net.minecraft.server.dedicated.DedicatedServer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

public class DedicatedServerConfig implements ResourcefulConfig {

    public static final DedicatedServerConfig INSTANCE = new DedicatedServerConfig();
    private static final List<ResourcefulConfigElement> ELEMENTS = Util.make(new ArrayList<>(), entries -> {

        entries.add(new ParsedEntryElement("allow-flight", DedicatedServerConfigEntry.of("allow-flight", false, DedicatedServer::allowFlight, DedicatedServer::setAllowFlight)));
        entries.add(new ParsedEntryElement("motd", DedicatedServerConfigEntry.of("motd", "A Minecraft Server", DedicatedServer::getMotd, DedicatedServer::setMotd)));

        entries.add(new ParsedEntryElement("white-list", DedicatedServerConfigEntry.of("white-list", false, DedicatedServer::isUsingWhitelist, (server, value) -> {
            server.setUsingWhitelist(value);
            server.kickUnlistedPlayers();
        })));
        entries.add(new ParsedEntryElement("enforce-whitelist", DedicatedServerConfigEntry.of("enforce-whitelist", false, DedicatedServer::isEnforceWhitelist, (server, value) -> {
            server.setEnforceWhitelist(value);
            server.kickUnlistedPlayers();
        })));

        entries.add(new ParsedEntryElement("max-players", DedicatedServerConfigEntry.of("max-players", 20, DedicatedServer::getMaxPlayers, DedicatedServer::setMaxPlayers)));
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
