package com.teamresourceful.resourcefulconfig.client.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
public class ConfigSearching {

    private static final BiMap<String, String> ALIASES = HashBiMap.create();
    private static final Cache<String, List<String>> CACHE = CacheBuilder.newBuilder().maximumSize(300).build();

    static {
        ALIASES.put("color", "colour");
        ALIASES.put("gray", "grey");
        ALIASES.put("center", "centre");
        ALIASES.put("favorite", "favourite");
        ALIASES.put("armor", "armour");
    }

    public static List<String> getAdditionalTerms(String text) {
        List<String> cached = CACHE.getIfPresent(text);
        if (cached != null) {
            return cached;
        } else {
            List<String> terms = new ArrayList<>();
            for (String s : text.split(" ")) {
                if (ALIASES.containsKey(s)) terms.add(ALIASES.get(s));
                if (ALIASES.containsValue(s)) terms.add(ALIASES.inverse().get(s));
            }
            CACHE.put(text, terms);
            return terms;
        }
    }
}
