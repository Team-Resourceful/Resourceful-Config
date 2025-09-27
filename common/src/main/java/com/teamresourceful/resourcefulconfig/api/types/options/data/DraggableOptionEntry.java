package com.teamresourceful.resourcefulconfig.api.types.options.data;

public record DraggableOptionEntry<T>(
        T value,
        boolean duplicatable
) {
}
