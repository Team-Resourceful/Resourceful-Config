package com.teamresourceful.resourcefulconfig.common.loader;

import com.google.gson.JsonObject;
import com.teamresourceful.resourcefulconfig.api.patching.ConfigPatchEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.GsonHelper;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class Patcher {

    public static JsonObject patch(JsonObject json, int currentVersion, Consumer<ConfigPatchEvent> handler) {
        if (currentVersion < 0) return json;

        int fileVersion = GsonHelper.getAsInt(json, Writer.VERSION_KEY, 0);
        var patches = getPatches(currentVersion, handler);
        for (int i = fileVersion; i < currentVersion; i++) {
            UnaryOperator<JsonObject> patch = patches.get(i);
            if (patch != null) {
                json = patch.apply(json);
            }
        }
        return json;
    }

    private static Int2ObjectFunction<UnaryOperator<JsonObject>> getPatches(int currentVersion, Consumer<ConfigPatchEvent> handler) {
        Int2ObjectMap<UnaryOperator<JsonObject>> patches = new Int2ObjectOpenHashMap<>();
        handler.accept((version, patcher) -> {
            if (version > currentVersion) {
                throw new IllegalStateException("Patch version is greater than current version");
            }
            patches.compute(version, (key, value) -> value == null ? patcher : andThen(value, patcher));
        });
        return (version) -> patches.getOrDefault(version, UnaryOperator.identity());
    }

    private static <T> UnaryOperator<T> andThen(UnaryOperator<T> first, UnaryOperator<T> second) {
        return t -> second.apply(first.apply(t));
    }
}
