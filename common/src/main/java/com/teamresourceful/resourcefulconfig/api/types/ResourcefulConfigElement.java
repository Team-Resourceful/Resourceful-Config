package com.teamresourceful.resourcefulconfig.api.types;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface ResourcefulConfigElement {

    @Nullable
    default ResourceLocation renderer() {
        return null;
    }

    boolean search(Predicate<String> predicate);
}
