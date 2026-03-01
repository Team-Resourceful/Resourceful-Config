package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.annotations.*;
import com.teamresourceful.resourcefulconfig.api.types.entries.Observable;
import com.teamresourceful.resourcefulconfig.api.types.info.ListEntrySummaryProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

@ConfigInfo.Provider(DemoInfoProvider.class)
@Config(
        value = "demo",
        version = 2,
        categories = {
            DemoCategory.class
        }
)
@SuppressWarnings("unused")
public final class DemoConfig {

    @ConfigEntry(
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
            id = "demoInteger",
            translation = "1"
    )
    public static int demoInteger = 1;

    @ConfigEntry(
            id = "demoDouble",
            translation = "1.0"
    )
    public static double demoDouble = 1.0;

    @ConfigEntry(
            id = "demoSlider",
            translation = "1"
    )
    @ConfigOption.Slider
    @ConfigOption.Range(min = 0, max = 10)
    public static int demoSlider = 1;


    @ConfigEntry(
            id = "demoString",
            translation = "Hello World!"
    )
    public static String demoString = "Hello World!";

    @ConfigEntry(
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
            id = "demoRange",
            translation = "range"
    )
    @ConfigOption.Range(min = 0, max = 10)
    public static int demoIntegerRange = 5;

    @ConfigEntry(
            id = "demoEnum",
            translation = "enum"
    )
    public static ChatFormatting demoEnum = ChatFormatting.RED;

    @ConfigOption.Separator(
        value = "Separator",
        description = "This is a separator."
    )
    @ConfigEntry(
            id = "demoRegex",
            translation = "regex"
    )
    @ConfigOption.Regex("#[a-fA-F0-9]{6}")
    public static String demoRegex = "#ff0000";

    @ConfigEntry(
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
            id = "demoAlphaColor",
            translation = "demo alpha color"
    )
    @ConfigOption.Color(alpha = true)
    public static int demoAlphaColor = 0xffff0000;

    @ConfigEntry(
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
            id = "demoDraggableWithTooltip",
            translation = "draggable with tooltip"
    )
    @ConfigOption.Draggable
    public static DemoEnum[] demoDraggableWithTooltip = new DemoEnum[]{
            DemoEnum.FIRST
    };

    @ConfigEntry(
            id = "demoKeybind",
            translation = "keybind"
    )
    @ConfigOption.Keybind
    public static int demoKeybind = 48;

    @ConfigEntry(id = "object")
    public static final DemoObject object = new DemoObject();

    @ConfigEntry(id = "object2")
    public static final DemoObject2 object2 = new DemoObject2();

    @ConfigEntry(
            id = "demoRenderer2",
            translation = "renderer"
    )
    @ConfigOption.Renderer("demo:demo")
    public static String demoRenderer2 = "meow";

    @ConfigEntry(
            id = "demoEnumWithAbstract",
            translation = "enum with abstract method"
    )
    public static DemoEnum demoEnumWithAbstract = DemoEnum.FIRST;

    @ConfigEntry(id = "list")
    @Comment("Filter rules. Each rule targets a category and sets a threshold.")
    public static final List<FilterRule> filterRules = new ArrayList<>(List.of(new FilterRule()));

    @ConfigObject
    public static class FilterRule implements ListEntrySummaryProvider {

        @ConfigEntry(id = "name")
        public String name = "default";

        @ConfigEntry(id = "category")
        public ChatFormatting category = ChatFormatting.WHITE;

        @ConfigEntry(id = "threshold")
        @ConfigOption.Range(min = 0, max = 100)
        @ConfigOption.Slider
        public int threshold = 50;

        @ConfigEntry(id = "enabled")
        public boolean enabled = true;

        @Override
        public Component getTitle(int index) {
            return Component.literal(name + " (" + threshold + "%)");
        }
    }

    @ConfigEntry(id = "list2")
    @Comment("Named color presets.")
    public static final List<ColorPreset> colorPresets = new ArrayList<>();

    @ConfigObject
    public static class ColorPreset implements ListEntrySummaryProvider {

        @ConfigEntry(id = "label")
        public String label = "preset";

        @ConfigEntry(id = "color")
        @ConfigOption.Color
        public int color = 0xFF0000;

        @Override
        public Component getTitle(int index) {
            return Component.literal(label).withColor(color);
        }

        @Override
        public Component getDescription(int index) {
            return Component.literal("#" + String.format("%06X", color));
        }
    }

    @ConfigEntry(id = "list3")
    @Comment("Number entries — exercises int, double, and range fields.")
    public static final List<NumberEntry> numberEntries = new ArrayList<>();

    @ConfigObject
    public static class NumberEntry implements ListEntrySummaryProvider {

        @ConfigEntry(id = "count")
        @ConfigOption.Range(min = 0, max = 64)
        public int count = 1;

        @ConfigEntry(id = "multiplier")
        public double multiplier = 1.0;

        @ConfigEntry(id = "offset")
        @ConfigOption.Range(min = -100, max = 100)
        @ConfigOption.Slider
        public int offset = 0;

        @Override
        public Component getTitle(int index) {
            return Component.literal("x" + multiplier + " +" + offset);
        }
    }

    @ConfigEntry(id = "list4")
    @Comment("Text entries — exercises string and multiline fields.")
    public static final List<TextEntry> textEntries = new ArrayList<>();

    @ConfigObject
    public static class TextEntry implements ListEntrySummaryProvider {

        @ConfigEntry(id = "title")
        public String title = "untitled";

        @ConfigEntry(id = "body")
        @ConfigOption.Multiline
        public String body = "";

        @Override
        public Component getTitle(int index) {
            return Component.literal(title);
        }

        @Override
        public Component getDescription(int index) {
            String preview = body.length() > 32 ? body.substring(0, 32) + "…" : body;
            return Component.literal(preview);
        }
    }

    @ConfigEntry(id = "list5")
    @Comment("A plain list with no summary provider — uses the reflection fallback.")
    public static final List<SimpleEntry> simpleEntries = new ArrayList<>();

    @ConfigObject
    public static class SimpleEntry {

        @ConfigEntry(id = "value")
        public int value = 0;

        @ConfigEntry(id = "active")
        public boolean active = false;
    }
}
