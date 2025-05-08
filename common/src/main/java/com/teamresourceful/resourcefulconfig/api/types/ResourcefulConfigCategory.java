package com.teamresourceful.resourcefulconfig.api.types;

import com.teamresourceful.resourcefulconfig.api.patching.ConfigPatchEvent;

import java.util.function.Consumer;

public interface ResourcefulConfigCategory extends ResourcefulConfig {

    ResourcefulConfig parent();

    @Override
    default void save() {
        parent().save();
    }

    @Override
    default void load(Consumer<ConfigPatchEvent> handler) {
        parent().load(handler);
    }
}
