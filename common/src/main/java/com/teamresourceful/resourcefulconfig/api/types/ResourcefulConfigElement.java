package com.teamresourceful.resourcefulconfig.api.types;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface ResourcefulConfigElement {

    @Nullable
    default Identifier renderer() {
        return null;
    }

    boolean search(Predicate<String> predicate);

    default boolean isHidden() {
        return false;
    }
}
