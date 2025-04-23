package com.teamresourceful.resourcefulconfig.common.loader.elements;

import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;

public record ParsedEntryElement(
        String id,
        ResourcefulConfigEntry entry
) implements ResourcefulConfigEntryElement {

}
