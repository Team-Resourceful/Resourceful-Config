package com.teamresourceful.resourcefulconfig.api.types.info;

import net.minecraft.network.chat.Component;

public interface ListEntrySummaryProvider {

    Component getTitle(int index);

    default Component getDescription(int index) {
        return Component.empty();
    }
}