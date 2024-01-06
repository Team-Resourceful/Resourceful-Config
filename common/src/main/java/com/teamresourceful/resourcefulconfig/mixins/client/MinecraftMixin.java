package com.teamresourceful.resourcefulconfig.mixins.client;

import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Nullable public Screen screen;

    @Inject(
            method = "setScreen",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/screens/Screen;removed()V",
                shift = At.Shift.AFTER
            )
    )
    private void rconfig$onRemoved(Screen screen, CallbackInfo ci) {
        if (this.screen instanceof ConfigScreen configScreen) {
            configScreen.removed(screen);
        }
    }
}
