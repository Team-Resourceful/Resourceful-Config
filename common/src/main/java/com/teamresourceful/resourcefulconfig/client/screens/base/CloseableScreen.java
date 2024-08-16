package com.teamresourceful.resourcefulconfig.client.screens.base;

import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public interface CloseableScreen {

    void onClosed(@Nullable Screen replacement);
}
