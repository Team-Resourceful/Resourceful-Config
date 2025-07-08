package com.teamresourceful.resourcefulconfig.client;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfig.client.utils.ConfigSearching;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

@ApiStatus.Internal
public class ConfigScreenContext {

    private String query = "";
    private Function<String, List<String>> termCollector = s -> List.of();

    public ConfigScreenContext() {
    }

    public ConfigScreenContext(String query, Function<String, List<String>> termCollector) {
        this.query = query.trim().toLowerCase(Locale.ROOT);
        this.termCollector = termCollector;
    }

    public boolean setQuery(String query) {
        query = query.trim().toLowerCase(Locale.ROOT);
        if (query.equals(this.query)) return false;
        this.query = query;
        return true;
    }

    public String getQuery() {
        return query;
    }

    public boolean fulfillsSearch(String text) {
        text = text.trim().toLowerCase(Locale.ROOT);
        if (text.contains(this.query)) return true;

        for (String term : termCollector.apply(text)) {
            if (term.contains(this.query)) return true;
        }
        for (String term : ConfigSearching.getAdditionalTerms(text)) {
            if (term.contains(this.query)) return true;
        }
        return false;
    }

    public boolean fulfillsSearch(ResourcefulConfig config) {
        if (this.query.isBlank()) return true;
        for (ResourcefulConfigElement element : config.elements()) {
            if (fulfillsSearch(element)) return true;
        }
        for (ResourcefulConfig category : config.categories().values()) {
            if (fulfillsSearch(category)) return true;
        }
        String title = config.info().title().toLocalizedString();
        String description = config.info().description().toLocalizedString();
        return fulfillsSearch(title) || fulfillsSearch(description);
    }

    public boolean fulfillsSearch(ResourcefulConfigElement element) {
        if (this.query.isBlank()) return true;
        return element.search(this::fulfillsSearch);
    }
}
