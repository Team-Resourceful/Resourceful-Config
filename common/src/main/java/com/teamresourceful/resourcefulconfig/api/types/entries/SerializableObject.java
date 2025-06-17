package com.teamresourceful.resourcefulconfig.api.types.entries;

import com.google.gson.JsonElement;

public interface SerializableObject {

    JsonElement save();

    void load(JsonElement json);
}
