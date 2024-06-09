package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.annotations.*;
import com.teamresourceful.resourcefulconfig.api.types.entries.Observable;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import net.minecraft.ChatFormatting;

@ConfigInfo.Provider(DemoInfoProvider.class)
@Config(value = "demo", version = 2)
@SuppressWarnings("unused")
public final class DemoConfig {

    @ConfigEntry(
            type = EntryType.BOOLEAN,
            id = "demoBoolean",
            translation = "true"
    )
    public static Observable<Boolean> demoBoolean = Observable.of(true);

    static {
        demoBoolean.addListener((oldValue, newValue) -> {
            System.out.println("Old Value: " + oldValue + " New Value: " + newValue);
        });
    }

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
            type = EntryType.INTEGER,
            id = "demoSlider",
            translation = "1"
    )
    @ConfigOption.Slider
    @ConfigOption.Range(min = 0, max = 10)
    public static int demoSlider = 1;


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
            id = "demoRange",
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

    @ConfigOption.Separator(
        value = "Separator",
        description = "This is a separator."
    )
    @ConfigEntry(
            type = EntryType.STRING,
            id = "demoRegex",
            translation = "regex"
    )
    @ConfigOption.Regex("#[a-fA-F0-9]{6}")
    public static String demoRegex = "#ff0000";

    @ConfigEntry(
            type = EntryType.INTEGER,
            id = "demoColor",
            translation = "demo color"
    )
    @ConfigOption.SearchTerm(":3")
    @ConfigOption.Color(presets = {
            16733525, 16733695, 16777045, 16777215, 16711680,
            11141290, 5635925, 11184810, 16755200, 16776960
    })
    public static int demoColor = 0xff0000;

    @ConfigEntry(
            type = EntryType.ENUM,
            id = "demoSelect",
            translation = "select"
    )
    @ConfigOption.Select
    public static ChatFormatting[] demoSelect = new ChatFormatting[]{
            ChatFormatting.RED,
            ChatFormatting.GREEN
    };

    @ConfigButton(title = "Test Button", text = "Click Me!")
    @Comment("This is a test button.")
    public static final Runnable test = () -> System.out.println("Clicked!");

    @ConfigEntry(
            type = EntryType.ENUM,
            id = "demoDraggable",
            translation = "draggable"
    )
    @ConfigOption.Draggable(
            value = "RED"
    )
    @ConfigOption.Range(min = 1, max = 10)
    public static ChatFormatting[] demoDraggable = new ChatFormatting[]{
            ChatFormatting.RED,
            ChatFormatting.GREEN
    };

    @ConfigEntry(
            type = EntryType.INTEGER,
            id = "demoKeybind",
            translation = "keybind"
    )
    @ConfigOption.Keybind
    public static int demoKeybind = 48;
}
