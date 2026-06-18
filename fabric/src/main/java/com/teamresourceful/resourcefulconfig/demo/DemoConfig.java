package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.annotations.*;
import com.teamresourceful.resourcefulconfig.api.types.entries.Observable;
import com.teamresourceful.resourcefulconfig.api.types.info.ListEntryInfoProvider;
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
    public static DemoColorEnum demoEnum = DemoColorEnum.RED;

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
    public static DemoColorEnum[] demoSelect = new DemoColorEnum[]{
            DemoColorEnum.RED,
            DemoColorEnum.GREEN
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
    public static DemoColorEnum[] demoDraggable = new DemoColorEnum[]{
            DemoColorEnum.RED,
            DemoColorEnum.GREEN
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

    @ConfigEntry(id = "demoList")
    public static final List<DemoListEntry> demoList = new ArrayList<>(List.of(new DemoListEntry()));

    @ConfigObject
    public static class DemoListEntry implements ListEntryInfoProvider {

        @ConfigEntry(id = "name")
        public String name = "default";

        @ConfigEntry(id = "formatting")
        public DemoColorEnum formatting = DemoColorEnum.WHITE;

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

        @Override
        public Component getDescription(int index) {
            return Component.literal("Category: " + formatting.name() + " Enabled: " + enabled);
        }
    }

    @ConfigEntry(id = "demoList2")
    public static final List<String> demoList2 = new ArrayList<>(List.of("Hello", "World"));
}
