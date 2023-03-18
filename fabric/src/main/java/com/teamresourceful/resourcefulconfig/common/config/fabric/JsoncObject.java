package com.teamresourceful.resourcefulconfig.common.config.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JsoncObject {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Pattern COMMENT_PATTERN = Pattern.compile("(^\s*//.*$)|(/\\*(\\*(?!/)|[^*])*\\*/)", Pattern.MULTILINE);
    private static final int INDENT_SIZE = 4;
    private static final String INDENT = " ".repeat(INDENT_SIZE);

    private final Map<String, CommentedEntry> entries = new LinkedHashMap<>();

    public JsoncObject add(String key, CommentedEntry entry) {
        entries.put(key, entry);
        return this;
    }

    public JsoncObject add(String key, String comment, Int2ObjectFunction<String> value) {
        return add(key, new CommentedEntry(value, comment));
    }

    public JsoncObject add(String key, String comment, JsoncObject value) {
        return add(key, new CommentedEntry(value::toString, comment));
    }

    public String toString(int currentIndent) {
        int indent = currentIndent;
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        indent++;
        for (Map.Entry<String, CommentedEntry> entry : entries.entrySet()) {
            comment(indent, entry.getValue(), builder);
            builder.append(INDENT.repeat(indent));
            builder.append("\"").append(entry.getKey()).append("\": ");
            builder.append(entry.getValue().value().get(indent));
            builder.append(",\n");
        }
        indent--;
        if (builder.charAt(builder.length() - 2) == ',') {
            builder.deleteCharAt(builder.length() - 2);
        }
        builder.append(INDENT.repeat(indent)).append("}");
        return builder.toString();
    }

    private void comment(int indent, CommentedEntry entry, StringBuilder builder) {
        String comment = entry.comment();
        if (comment != null) {
            var split = comment.split("\\R");
            if (split.length > 1) {
                builder.append(INDENT.repeat(indent)).append("/*\n");
                for (String s : split) {
                    builder.append(INDENT.repeat(indent + 1)).append(s).append("\n");
                }
                builder.append(INDENT.repeat(indent)).append(" */\n");
            } else {
                builder.append(INDENT.repeat(indent));
                builder.append("// ").append(comment).append("\n");
            }
        }
    }

    @Override
    public String toString() {
        return toString(0);
    }

    private record CommentedEntry(Int2ObjectFunction<String> value, @Nullable String comment) {}

    public static JsonObject load(String data) {
        data = COMMENT_PATTERN.matcher(data).replaceAll("");
        return GSON.fromJson(data, JsonObject.class);
    }
}