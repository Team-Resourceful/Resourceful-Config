package com.teamresourceful.resourcefulconfig.api.patching;

import com.google.gson.JsonObject;

import java.util.function.UnaryOperator;

public interface ConfigPatchEvent {

    void register(int version, UnaryOperator<JsonObject> json);
}
