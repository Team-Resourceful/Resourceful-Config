package com.teamresourceful.resourcefulconfig.common.jsonc;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsoncArray implements JsoncElement, Iterable<JsoncElement> {

    private final List<JsoncElement> elements = new ArrayList<>();
    private String comment = "";

    @Override
    public void comment(String comment) {
        this.comment = comment;
    }

    @Override
    public String comment() {
        return comment;
    }

    public JsoncArray add(JsoncElement element) {
        elements.add(element);
        return this;
    }

    public JsoncArray remove(JsoncElement element) {
        elements.remove(element);
        return this;
    }

    public JsoncArray remove(int index) {
        elements.remove(index);
        return this;
    }

    @Override
    public String toString(int indentation) {
        if (elements.isEmpty()) return "[]";
        StringBuilder builder = new StringBuilder("[\n");
        for (JsoncElement element : elements) {
            JsoncElement.writeComment(builder, element, indentation + 1);
            builder.append(INDENT.repeat(indentation + 1));
            builder.append(element.toString(indentation + 1));
            builder.append(",\n");
        }
        builder.deleteCharAt(builder.length() - 2);
        builder.append(INDENT.repeat(indentation)).append("]");
        return builder.toString();
    }

    @NotNull
    @Override
    public Iterator<JsoncElement> iterator() {
        return elements.iterator();
    }

    @Override
    public String toString() {
        return toString(0);
    }
}
