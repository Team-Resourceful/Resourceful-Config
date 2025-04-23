package com.teamresourceful.resourcefulconfig.mixins.common;

import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerList.class)
public interface PlayerListAccessor {

    @Accessor("maxPlayers")
    @Final
    @Mutable
    void setMaxPlayers(int maxPlayers);
}
