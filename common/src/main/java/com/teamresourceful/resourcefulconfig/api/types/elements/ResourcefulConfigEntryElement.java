package com.teamresourceful.resourcefulconfig.api.types.elements;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.options.Option;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.function.Predicate;

public interface ResourcefulConfigEntryElement extends ResourcefulConfigElement {

    String id();

    ResourcefulConfigEntry entry();

    @Override
    default Identifier renderer() {
        return entry().options().getOrDefaultOption(Option.RENDERER, null);
    }

    @Override
    default boolean search(Predicate<String> predicate) {
        String title = entry().options().title().toLocalizedString();
        String description = entry().options().comment().toLocalizedString();
        for (String term : entry().options().getOrDefaultOption(Option.SEARCH_TERM, List.of())) {
            if (predicate.test(term)) return true;
        }
        return predicate.test(title) || predicate.test(description);
    }

    @Override
    default boolean isHidden() {
        return entry().options().hasOption(Option.HIDDEN);
    }
}
