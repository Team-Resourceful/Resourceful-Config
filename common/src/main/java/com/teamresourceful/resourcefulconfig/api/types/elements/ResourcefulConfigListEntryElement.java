package com.teamresourceful.resourcefulconfig.api.types.elements;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigListEntry;

public interface ResourcefulConfigListEntryElement extends ResourcefulConfigEntryElement {
    @Override
    ResourcefulConfigListEntry entry();
}