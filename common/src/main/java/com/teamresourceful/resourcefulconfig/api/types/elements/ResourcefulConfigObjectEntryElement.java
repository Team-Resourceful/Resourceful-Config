package com.teamresourceful.resourcefulconfig.api.types.elements;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigObjectEntry;

import java.util.function.Predicate;

public interface ResourcefulConfigObjectEntryElement extends ResourcefulConfigEntryElement {

    @Override
    ResourcefulConfigObjectEntry entry();

    @Override
    default boolean search(Predicate<String> predicate) {
        if (ResourcefulConfigEntryElement.super.search(predicate)) return true;
        for (var element : entry().elements()) {
            if (element.search(predicate)) {
                return true;
            }
        }
        return false;
    }
}
