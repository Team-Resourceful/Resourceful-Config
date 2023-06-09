package com.teamresourceful.resourcefulconfig.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public final class ClientUtils {

    private ClientUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void setTooltip(List<FormattedCharSequence> tooltip) {
        if (Minecraft.getInstance().screen != null) {
            Minecraft.getInstance().screen.setTooltipForNextRenderPass(tooltip);
        }
    }
}
