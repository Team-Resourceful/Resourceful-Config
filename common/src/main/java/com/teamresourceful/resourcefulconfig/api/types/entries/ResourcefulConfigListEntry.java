package com.teamresourceful.resourcefulconfig.api.types.entries;

import net.minecraft.network.chat.Component;

public interface ResourcefulConfigListEntry extends ResourcefulConfigEntry {

    Class<?> objectType();

    int size();

    ResourcefulConfigEntry get(int index);

    void move(int from, int to);

    void remove(int index);

    void clear();

    void add(int index);

    Component getTitle(int index);

    Component getDescription(int index);

    default void add() {
        this.add(this.size());
    }

    default int wrap(int index) {
        if (this.size() == 0) return 0;
        return Math.floorMod(index, this.size());
    }
}