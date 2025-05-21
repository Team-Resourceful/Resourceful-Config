package com.teamresourceful.resourcefulconfig.common.loader.elements;

import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigObjectEntryElement;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigObjectEntry;

public record ParsedObjectEntryElement(
        String id,
        ResourcefulConfigObjectEntry entry
) implements ResourcefulConfigObjectEntryElement {

}
