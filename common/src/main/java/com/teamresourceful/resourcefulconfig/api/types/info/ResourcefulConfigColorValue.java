package com.teamresourceful.resourcefulconfig.api.types.info;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public interface ResourcefulConfigColorValue extends ResourcefulConfigColor {

    String color();

    @Override
    default JsonElement toJson() {
        return new JsonPrimitive(this.color());
    }
}
