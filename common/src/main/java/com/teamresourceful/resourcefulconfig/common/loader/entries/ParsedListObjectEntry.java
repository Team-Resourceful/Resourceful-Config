package com.teamresourceful.resourcefulconfig.common.loader.entries;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigObjectEntry;
import com.teamresourceful.resourcefulconfig.api.types.info.Translatable;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public record ParsedListObjectEntry(
        EntryType type,
        Field field,
        EntryData options,
        List<ResourcefulConfigElement> elements,
        Object instance
) implements ResourcefulConfigObjectEntry {

    public ParsedListObjectEntry(Field field, Object instance) {
        this(EntryType.OBJECT, field, EntryData.of(field, field.getType()), new ArrayList<>(), instance);
    }

    @Override
    public void reset() {
        for (ResourcefulConfigElement element : this.elements) {
            if (element instanceof ResourcefulConfigEntryElement entry) {
                entry.entry().reset();
            }
        }
    }

    @Override
    public Component getTitle(@NotNull Component fallback) {
        return Translatable.toSpeifiedComponent(this.instance, fallback);
    }
}