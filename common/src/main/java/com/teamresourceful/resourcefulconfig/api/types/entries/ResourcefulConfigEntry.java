package com.teamresourceful.resourcefulconfig.api.types.entries;

import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;

public interface ResourcefulConfigEntry {

    EntryType type();

    EntryData options();

    void reset();
}
