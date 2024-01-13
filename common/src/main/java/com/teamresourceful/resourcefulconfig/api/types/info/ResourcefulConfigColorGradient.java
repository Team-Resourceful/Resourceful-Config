package com.teamresourceful.resourcefulconfig.api.types.info;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface ResourcefulConfigColorGradient extends ResourcefulConfigColor {

    String first();

    String second();

    String degree();

    @Override
    default JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("degree", this.degree());
        json.addProperty("first", this.first());
        json.addProperty("second", this.second());
        return json;
    }

    static ResourcefulConfigColorGradient create(
            String first,
            String second,
            String degree
    ) {
        return new ResourcefulConfigColorGradient() {

            @Override
            public String first() {
                return first;
            }

            @Override
            public String second() {
                return second;
            }

            @Override
            public String degree() {
                return degree;
            }
        };
    }
}
