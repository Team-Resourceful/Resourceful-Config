package com.teamresourceful.resourcefulconfig.common.compat.minecraft;

import com.teamresourceful.resourcefulconfig.api.patching.ConfigPatchEvent;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigButton;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import com.teamresourceful.resourcefulconfig.mixins.common.PlayerListAccessor;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

public class DedicatedServerConfig implements ResourcefulConfig {

    public static final DedicatedServerConfig INSTANCE = new DedicatedServerConfig();
    private static final LinkedHashMap<String, ResourcefulConfigEntry> ENTRIES = Util.make(new LinkedHashMap<>(), entries -> {

        entries.put("pvp", new DedicatedServerEntry("pvp", true, MinecraftServer::setPvpAllowed));
        entries.put("allow-flight", new DedicatedServerEntry("allow-flight", false, MinecraftServer::setFlightAllowed));

        entries.put("motd", new DedicatedServerEntry("motd", "A Minecraft Server", MinecraftServer::setMotd));

        entries.put("white-list", new DedicatedServerEntry("white-list", false, (server, value) -> {
            server.getPlayerList().setUsingWhiteList(value);
            server.kickUnlistedPlayers(server.createCommandSourceStack());
        }));
        entries.put("enforce-whitelist", new DedicatedServerEntry("enforce-whitelist", false, (server, value) -> {
            server.setEnforceWhitelist(value);
            server.kickUnlistedPlayers(server.createCommandSourceStack());
        }));

        entries.put("max-players", new DedicatedServerEntry("max-players", 20, (server, value) ->
                ((PlayerListAccessor)server.getPlayerList()).setMaxPlayers(value))
        );
    });
    private static final LinkedHashMap<String, ResourcefulConfig> CATEGORIES = new LinkedHashMap<>();

    @Override
    public @NotNull LinkedHashMap<String, ResourcefulConfigEntry> entries() {
        return ENTRIES;
    }

    @Override
    public @NotNull LinkedHashMap<String, ResourcefulConfig> categories() {
        return CATEGORIES;
    }

    @Override
    public @NotNull List<ResourcefulConfigButton> buttons() {
        return List.of();
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

    @Override
    public boolean hasFile() {
        return false;
    }
}
