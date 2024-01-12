package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.annotations.*;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import com.teamresourceful.resourcefulconfig.api.types.options.Position;
import net.minecraft.ChatFormatting;

@ConfigInfo.Provider(DemoInfoProvider.class)
@Config(
    value = "demo"
)
@SuppressWarnings("unused")
public final class DemoConfig {

    @ConfigEntry(
            type = EntryType.BOOLEAN,
            id = "demoBoolean",
            translation = "true"
    )
    public static boolean demoBoolean = true;

    @ConfigEntry(
            type = EntryType.INTEGER,
            id = "demoInteger",
            translation = "1"
    )
    public static int demoInteger = 1;

    @ConfigEntry(
            type = EntryType.DOUBLE,
            id = "demoDouble",
            translation = "1.0"
    )
    public static double demoDouble = 1.0;

    @ConfigEntry(
            type = EntryType.STRING,
            id = "demoString",
            translation = "Hello World!"
    )
    public static String demoString = "Hello World!";

    @ConfigEntry(
            type = EntryType.STRING,
            id = "multiline",
            translation = "multiline"
    )
    @ConfigOption.Multiline
    public static String multiline = """
            This is a multiline string.
            It has multiple lines.
            It is very long.
            """;

    @ConfigEntry(
            type = EntryType.INTEGER,
            id = "demoInteger",
            translation = "range"
    )
    @ConfigOption.Range(min = 0, max = 10)
    public static int demoIntegerRange = 5;

    @ConfigEntry(
            type = EntryType.ENUM,
            id = "demoEnum",
            translation = "enum"
    )
    public static ChatFormatting demoEnum = ChatFormatting.RED;

    @ConfigEntry(
        type = EntryType.STRING,
        id = "demoRegex",
        translation = "regex"
    )
    @ConfigOption.Regex("#[a-fA-F0-9]{6}")
    public static String demoRegex = "#ff0000";

    @ConfigButton(
        target = "demoEnum",
        text = "Click ME!",
        position = Position.AFTER
    )
    public static void clickMe() {
        System.out.println("Clicked!");
    }

    @ConfigButton(
            target = "demoEnum",
            text = "Click ME!",
            position = Position.BEFORE
    )
    public static void clickMe2() {
        System.out.println("Clicked! 2");
    }

    @ConfigButton(target = "test", text = "Click Me!", position = Position.BEFORE)
    public static void button() {
        System.out.println("clicked me!");
    }
}
