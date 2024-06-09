package com.teamresourceful.resourcefulconfig.client.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigButton;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.options.Option;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class ConfigSearching {

    private static final BiMap<String, String> ALIASES = HashBiMap.create();
    private static final Cache<String, List<String>> CACHE = CacheBuilder.newBuilder().maximumSize(300).build();
    private static String search = "";

    static {
        ALIASES.put("color", "colour");
        ALIASES.put("gray", "grey");
        ALIASES.put("center", "centre");
        ALIASES.put("favorite", "favourite");
        ALIASES.put("armor", "armour");
    }

    public static boolean setSearch(String search) {
        search = search.trim().toLowerCase(Locale.ROOT);
        if (search.equals(ConfigSearching.search)) return false;
        ConfigSearching.search = search;
        return true;
    }

    public static String getSearch() {
        return search;
    }

    private static List<String> getAdditionalTerms(String text) {
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

    private static boolean fulfillsSearch(String text, Function<String, List<String>> termCollector) {
        text = text.trim().toLowerCase(Locale.ROOT);
        if (text.contains(search)) return true;

        for (String term : termCollector.apply(text)) {
            if (term.contains(search)) return true;
        }
        for (String term : getAdditionalTerms(text)) {
            if (term.contains(search)) return true;
        }
        return false;
    }

    public static boolean fulfillsSearch(ResourcefulConfig config, Function<String, List<String>> termCollector) {
        if (search.isBlank()) return true;
        for (ResourcefulConfigButton button : config.buttons()) {
            if (fulfillsSearch(button, termCollector)) return true;
        }
        for (ResourcefulConfigEntry entry : config.entries().values()) {
            if (fulfillsSearch(entry, termCollector)) return true;
        }
        for (ResourcefulConfig category : config.categories().values()) {
            if (fulfillsSearch(category, termCollector)) return true;
        }
        String title = config.info().title().toLocalizedString();
        String description = config.info().description().toLocalizedString();
        return fulfillsSearch(title, termCollector) || fulfillsSearch(description, termCollector);
    }

    public static boolean fulfillsSearch(ResourcefulConfigButton button, Function<String, List<String>> termCollector) {
        if (search.isBlank()) return true;
        String title = Component.translatable(button.title()).getString();
        String description = Component.translatable(button.description()).getString();
        String text = Component.translatable(button.text()).getString();
        return fulfillsSearch(title, termCollector) ||
                fulfillsSearch(description, termCollector) ||
                fulfillsSearch(text, termCollector);
    }

    public static boolean fulfillsSearch(ResourcefulConfigEntry entry, Function<String, List<String>> termCollector) {
        if (search.isBlank()) return true;
        String title = entry.options().title().toLocalizedString();
        String description = entry.options().comment().toLocalizedString();
        for (String term : entry.options().getOrDefaultOption(Option.SEARCH_TERM, List.of())) {
            if (fulfillsSearch(term, termCollector)) return true;
        }
        return fulfillsSearch(title, termCollector) || fulfillsSearch(description, termCollector);
    }
}
