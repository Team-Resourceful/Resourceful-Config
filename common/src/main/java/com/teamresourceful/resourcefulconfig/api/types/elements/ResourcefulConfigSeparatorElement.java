package com.teamresourceful.resourcefulconfig.api.types.elements;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue;

import java.util.function.Predicate;

public interface ResourcefulConfigSeparatorElement extends ResourcefulConfigElement {

    TranslatableValue title();

    TranslatableValue description();

    @Override
    default boolean search(Predicate<String> predicate) {
        return predicate.test(title().toLocalizedString()) || predicate.test(description().toLocalizedString());
    }
}
