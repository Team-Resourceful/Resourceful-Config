package com.teamresourceful.resourcefulconfig.common.loader.entries;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigListEntry;
import com.teamresourceful.resourcefulconfig.api.types.info.ListEntrySummaryProvider;
import com.teamresourceful.resourcefulconfig.api.types.info.Translatable;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import com.teamresourceful.resourcefulconfig.common.loader.JavaConfigParser;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record ParsedListEntry(
        EntryType type,
        Field field,
        Class<?> objectType,
        EntryType elementType,
        List<Object> list,
        List<Object> defaultList,
        EntryData options
) implements ResourcefulConfigListEntry {

    @SuppressWarnings("unchecked")
    public ParsedListEntry(Field field, Class<?> objectType, List<?> list) {
        this(
                EntryType.LIST,
                field,
                objectType,
                JavaConfigParser.resolveElementType(objectType),
                (List<Object>) list,
                snapshot(objectType, JavaConfigParser.resolveElementType(objectType), (List<Object>) list),
                EntryData.of(field, field.getType())
        );
    }

    @Override
    public Class<?> objectType() {
        return objectType;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public ResourcefulConfigEntry get(int index) {
        int i = wrap(index);

        if (elementType == EntryType.OBJECT) {
            ParsedListObjectEntry itemEntry = new ParsedListObjectEntry(field, list.get(i));
            JavaConfigParser.populateEntries(list.get(i), itemEntry);
            return itemEntry;
        }

        EntryData elementOptions = EntryData.of(field::getAnnotation, objectType);

        return new ParsedListItemValueEntry(elementType, elementOptions, objectType, list, i, defaultElement());
    }

    @Override
    public void move(int from, int to) {
        Collections.swap(list, wrap(from), wrap(to));
    }

    @Override
    public void remove(int index) {
        list.remove(wrap(index));
    }

    @Override
    public void add(int index) {
        int i = list.isEmpty() ? 0 : Math.floorMod(index, list.size() + 1);
        list.add(i, defaultElement());
    }

    @Override
    public void reset() {
        list.clear();
        for (Object def : defaultList) {
            if (elementType == EntryType.OBJECT) {
                Object item = defaultElement();
                copyFields(def, item);
                list.add(item);
            } else {
                list.add(def);
            }
        }
    }

    @Override
    public Component getTitle(int index) {
        Object item = list.get(wrap(index));

        if (item instanceof ListEntrySummaryProvider provider) {
            return provider.getTitle(index);
        }

        return Component.literal("Entry #" + (index + 1));
    }

    @Override
    public Component getDescription(int index) {
        Object item = list.get(wrap(index));
        if (item instanceof ListEntrySummaryProvider provider) {
            return provider.getDescription(index);
        }
        return Component.empty();
    }

    private Object defaultElement() {
        return switch (elementType) {
            case BYTE -> (byte) 0;
            case SHORT -> (short) 0;
            case INTEGER -> 0;
            case LONG -> 0L;
            case FLOAT -> 0.0f;
            case DOUBLE -> 0.0;
            case BOOLEAN -> false;
            case STRING -> "";
            case ENUM -> objectType.getEnumConstants()[0];
            case OBJECT -> {
                try {
                    yield objectType.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to instantiate list element " + objectType, e);
                }
            }
            default -> throw new IllegalStateException("Unsupported list element type: " + objectType);
        };
    }

    private static void copyFields(Object from, Object to) {
        if (from == null || to == null) return;
        for (Field f : from.getClass().getDeclaredFields()) {
            if (!f.isAnnotationPresent(ConfigEntry.class)) continue;
            try {
                f.set(to, f.get(from));
            } catch (Exception ignored) {}
        }
    }

    private static List<Object> snapshot(Class<?> objectType, EntryType elementType, List<Object> source) {
        if (elementType != EntryType.OBJECT) {
            return new ArrayList<>(source);
        }
        List<Object> snap = new ArrayList<>(source.size());
        for (Object item : source) {
            try {
                Object copy = objectType.getDeclaredConstructor().newInstance();
                copyFields(item, copy);
                snap.add(copy);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return snap;
    }
}