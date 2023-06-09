package com.teamresourceful.resourcefulconfig.common.jsonc;

public interface JsoncElement {

    int INDENT_SIZE = 4;
    String INDENT = " ".repeat(INDENT_SIZE);

    static void writeComment(StringBuilder builder, JsoncElement element, int indentation) {
        if (element.comments().length > 0 && !element.comment().isBlank()) {
            if (element.comments().length > 1) {
                builder.append(INDENT.repeat(indentation)).append("/*\n");
                for (String line : element.comments()) {
                    builder.append(INDENT.repeat(indentation)).append(" * ").append(line).append("\n");
                }
                builder.append(INDENT.repeat(indentation)).append(" */\n");
            } else {
                builder.append(INDENT.repeat(indentation)).append("// ").append(element.comment()).append("\n");
            }
        }
    }

    String toString(int indentation);

    void comment(String comment);

    String comment();

    default String[] comments() {
        return comment().split("\\R");
    }
}
