package com.teamresourceful.resourcefulconfig.mixins.common;

import net.minecraft.server.dedicated.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Properties;

@Mixin(Settings.class)
public interface SettingsAccessor {

    @Invoker
    String invokeGetStringRaw(String string);

    @Invoker
    Properties invokeCloneProperties();
}
