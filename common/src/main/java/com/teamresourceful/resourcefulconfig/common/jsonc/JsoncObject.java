package com.teamresourceful.resourcefulconfig.common.jsonc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsoncObject implements JsoncElement {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private String comment = "";
    private final Map<String, JsoncElement> elements = new LinkedHashMap<>();

    public JsoncObject add(String key, JsoncElement element) {
        elements.put(key, element);
        return this;
    }

    public JsoncObject remove(String key) {
        elements.remove(key);
        return this;
    }

    @Override
    public String toString(int indentation) {
        if (elements.isEmpty()) return "{}";
        StringBuilder builder = new StringBuilder("{\n");
        for (Map.Entry<String, JsoncElement> entry : elements.entrySet()) {
            JsoncElement.writeComment(builder, entry.getValue(), indentation + 1);
            builder.append(INDENT.repeat(indentation + 1));
            builder.append("\"").append(entry.getKey()).append("\": ");
            builder.append(entry.getValue().toString(indentation + 1));
            builder.append(",\n");
        }
        builder.deleteCharAt(builder.length() - 2);
        builder.append(INDENT.repeat(indentation)).append("}");
        return builder.toString();
    }

    @Override
    public void comment(String comment) {
        this.comment = comment;
    }

    @Override
    public String comment() {
        return comment;
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public static JsonObject parse(String json) {
        List<String> parsed = new ArrayList<>();

        boolean inMultiLineComment = false;

        for (String line : json.split("\n")) {
            int singleLineCommentIndex = line.indexOf("//");
            if (singleLineCommentIndex != -1 && line.substring(0, singleLineCommentIndex).isBlank()) {
                continue;
            }

            int multiLineCommentStartIndex = line.indexOf("/*");
            if (multiLineCommentStartIndex != -1 && line.substring(0, multiLineCommentStartIndex).isBlank()) {
                inMultiLineComment = true;
            }

            if (line.lastIndexOf("*/") != -1 && inMultiLineComment) {
                inMultiLineComment = false;
                continue;
            }

            if (inMultiLineComment) {
                continue;
            }

            parsed.add(line);
        }

        return GSON.fromJson(String.join("\n", parsed), JsonObject.class);
    }
}
