package com.teamresourceful.resourcefulconfig.common.loader.elements;

import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigListEntryElement;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigListEntry;

public record ParsedListEntryElement(
        String id,
        ResourcefulConfigListEntry entry
) implements ResourcefulConfigListEntryElement {
}