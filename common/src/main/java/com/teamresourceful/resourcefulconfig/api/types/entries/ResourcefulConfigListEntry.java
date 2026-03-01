package com.teamresourceful.resourcefulconfig.api.types.entries;

import net.minecraft.network.chat.Component;

public interface ResourcefulConfigListEntry extends ResourcefulConfigEntry {

    int size();

    Class<?> objectType();

    ResourcefulConfigEntry get(int index);

    void move(int from, int to);

    void remove(int index);

    void add(int index);

    Component getTitle(int index);

    Component getDescription(int index);

    default void add() {
        add(size());
    }

    default int wrap(int index) {
        if (size() == 0) return 0;
        return Math.floorMod(index, size());
    }
}