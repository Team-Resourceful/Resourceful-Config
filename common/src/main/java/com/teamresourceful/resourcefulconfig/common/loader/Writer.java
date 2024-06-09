package com.teamresourceful.resourcefulconfig.common.loader;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigObjectEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.api.types.options.Option;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.jsonc.JsoncArray;
import com.teamresourceful.resourcefulconfig.common.jsonc.JsoncElement;
import com.teamresourceful.resourcefulconfig.common.jsonc.JsoncObject;
import com.teamresourceful.resourcefulconfig.common.jsonc.JsoncPrimitive;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Writer {

    public static final String VERSION_KEY = "rconfig:version";

    public static JsoncObject save(ResourcefulConfig config) {
        JsoncObject object = new JsoncObject();
        JsoncPrimitive version = new JsoncPrimitive(config.version());
        version.comment("The version of the config file. Do not change this unless you know what you are doing.");
        object.add(Writer.VERSION_KEY, version);

        writeEntries(config.entries(), object);
        writeCategories(config.categories(), object);
        return object;
    }

    private static void writeCategories(LinkedHashMap<String, ResourcefulConfig> entries, JsoncObject object) {
        entries.forEach((key, value) -> {
            JsoncObject category = new JsoncObject();
            writeEntries(value.entries(), category);
            writeCategories(value.categories(), category);
            object.add(key, category);
        });
    }

    private static JsoncObject writeEntries(LinkedHashMap<String, ResourcefulConfigEntry> entries, JsoncObject object) {
        entries.forEach((key, value) -> {
            JsoncElement element = toElement(value);
            if (element == null) return;
            element.comment(getComments(value));
            object.add(key, element);
        });
        return object;
    }

    private static JsoncElement toElement(ResourcefulConfigEntry entry) {
        if (entry instanceof ResourcefulConfigObjectEntry objectEntry) {
            return writeEntries(objectEntry.entries(), new JsoncObject());
        }
        if (entry instanceof ResourcefulConfigValueEntry valueEntry) {
            return valueOf(valueEntry.get());
        }
        return null;
    }

    private static JsoncElement valueOf(Object value) {
        if (value == null) throw new NullPointerException("Config value cannot be null!");
        if (value instanceof String string) return new JsoncPrimitive(string);
        if (value instanceof Number number) return new JsoncPrimitive(number);
        if (value instanceof Boolean bool) return new JsoncPrimitive(bool);
        if (value instanceof Enum<?> enumValue) return new JsoncPrimitive(enumValue.name());
        if (value.getClass().isArray()) {
            JsoncArray array = new JsoncArray();
            ParsingUtils.forEach(value, o -> array.add(valueOf(o)));
            return array;
        }
        return null;
    }

    private static String getComments(ResourcefulConfigEntry entry) {
        List<String> comments = new ArrayList<>();

        final EntryData options = entry.options();

        if (entry instanceof ResourcefulConfigValueEntry valueEntry) {

            if (options.hasOption(Option.RANGE)) {
                DecimalFormat format = new DecimalFormat();
                format.setGroupingUsed(false);
                format.setMaximumFractionDigits(340);

                ConfigOption.Range range = options.getOption(Option.RANGE);
                comments.add("Range: " + format.format(range.min()) + " - " + format.format(range.max()));
            }

            if (options.hasOption(Option.REGEX)) {
                comments.add("Regex: " + options.getOption(Option.REGEX).pattern());
            }

            if (options.hasOption(Option.MULTILINE) && entry.type() == EntryType.STRING) {
                comments.add("[Allows newlines]");
            }

            if (options.hasOption(Option.COLOR) && entry.type() == EntryType.INTEGER) {
                ConfigOption.Color color = options.getOption(Option.COLOR);
                if (color.alpha()) {
                    comments.add("[Color Format: RGBA]");
                } else {
                    comments.add("[Color Format: RGB]");
                }
            }

            if (entry.type() == EntryType.ENUM) {
                List<String> enumNames = new ArrayList<>();
                for (Enum<?> enumConstant : ((Enum<?>[]) valueEntry.objectType().getEnumConstants())) {
                    enumNames.add(enumConstant.name());
                }
                comments.add("Valid Values: " + String.join(", ", enumNames));
            }
        }

        if (comments.isEmpty() && entry.type() != EntryType.BOOLEAN && entry.type() != EntryType.STRING && entry.type() != EntryType.OBJECT) {
            comments.add("Type: " + entry.type().name().charAt(0) + entry.type().name().substring(1).toLowerCase());
        }

        options.comment().ifPresent((comment, translation) -> comments.addFirst(comment));

        return String.join("\n", comments);
    }


}
