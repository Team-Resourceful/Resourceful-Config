package com.teamresourceful.resourcefulconfig.client.components.options;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigButton;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigObjectEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.api.types.options.Option;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.CustomButton;
import com.teamresourceful.resourcefulconfig.client.components.base.SpriteButton;
import com.teamresourceful.resourcefulconfig.client.components.options.range.DecimalOptionRange;
import com.teamresourceful.resourcefulconfig.client.components.options.range.OptionRange;
import com.teamresourceful.resourcefulconfig.client.components.options.range.WholeOptionRange;
import com.teamresourceful.resourcefulconfig.client.components.options.types.*;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class Options {

    public static void populateOptions(OptionsListWidget widget, Map<String, ResourcefulConfigEntry> entries, List<ResourcefulConfigButton> buttons) {
        Multimap<String, ResourcefulConfigButton> buttonsBefore = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);
        Multimap<String, ResourcefulConfigButton> buttonsAfter = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);
        buttons.forEach(button -> {
            switch (button.position()) {
                case BEFORE -> buttonsBefore.put(button.target(), button);
                case AFTER -> buttonsAfter.put(button.target(), button);
            }
        });

        buttonsBefore.get("").forEach(button -> addButton(widget, button));

        for (var value : entries.entrySet()) {
            final EntryData options = value.getValue().options();
            if (options.hasOption(Option.HIDDEN)) continue;

            if (options.hasOption(Option.SEPARATOR)) {
                ConfigOption.Separator separator = options.getOption(Option.SEPARATOR);
                widget.add(new OptionItem(Component.literal(separator.value()), Component.nullToEmpty(separator.description()), List.of()));
            }

            buttonsBefore.get(value.getKey()).forEach(button -> addButton(widget, button));

            if (value.getValue() instanceof ResourcefulConfigValueEntry entry) {
                populateValueEntry(widget, entry);
            } else if (value.getValue() instanceof ResourcefulConfigObjectEntry entry) {
                widget.add(new OptionItem(entry, List.of(new ObjectOptionWidget(entry))));
            }

            buttonsAfter.get(value.getKey()).forEach(button -> addButton(widget, button));
        }

        buttonsAfter.get("").forEach(button -> addButton(widget, button));
    }

    private static void addButton(OptionsListWidget list, ResourcefulConfigButton button) {
        list.add(new OptionItem(
                Component.translatable(button.title()),
                Component.translatable(button.description()),
                List.of(
                        new CustomButton(96, 12, Component.translatable(button.text()), button::invoke)
                )
        ));
    }

    private static void populateValueEntry(OptionsListWidget list, ResourcefulConfigValueEntry entry) {
        final EntryData data = entry.options();

        List<AbstractWidget> widgets = new ArrayList<>();

        switch (entry.type()) {
            case BOOLEAN -> widgets.add(new BooleanOptionWidget(entry::getBoolean, entry::setBoolean));
            case STRING -> {
                if (data.hasOption(Option.MULTILINE)) {
                    widgets.add(new MultilineStringOptionWidget(entry::getString, entry::setString));
                } else if (entry.isArray()) {
                    widgets.add(new MultilineStringOptionWidget(
                            () -> String.join("\n", (String[]) entry.getArray()),
                            s -> entry.setArray(s.split("\n"))
                    ));
                } else {
                    widgets.add(new StringOptionWidget(entry::getString, entry::setString));
                }
            }
            case ENUM -> {
                if (entry.isArray()) {
                    if (data.hasOption(Option.DRAGGABLE)) {
                        widgets.add(DraggableListOptionWidget.of(entry, data));
                    } else {

                        widgets.add(new SelectWidget(
                                data.getOrDefaultOption(Option.SELECT, Component.literal("Select")),
                                (Enum<?>[]) entry.objectType().getEnumConstants(),
                                () -> (Enum<?>[]) entry.getArray(),
                                entry::setArray
                        ));
                    }
                } else {
                    widgets.add(new DropdownWidget(
                            (Enum<?>[]) entry.objectType().getEnumConstants(),
                            entry::getEnum,
                            entry::setEnum
                    ));
                }
            }
            case INTEGER -> {
                OptionRange range = WholeOptionRange.of(entry);
                if (range.hasRange() && data.hasOption(Option.SLIDER)) {
                    widgets.add(new RangeOptionWidget(range));
                } else if (data.hasOption(Option.COLOR)) {
                    ConfigOption.Color color = data.getOption(Option.COLOR);
                    widgets.add(new ColorOptionWidget(
                            color.presets(),
                            color.alpha(),
                            () -> color.alpha() ? entry.getInt() : entry.getInt() | 0xFF000000,
                            value -> {
                                value = color.alpha() ? value : value & 0x00FFFFFF;
                                entry.setInt(value);
                            }
                    ));
                    widgets.add(new StringOptionWidget(
                            () -> "#" + String.format("%06X", entry.getInt()),
                            s -> {
                                try {
                                    if (s.length() == 8 && !color.alpha()) s = s.substring(2);
                                    if (s.length() == 3) {
                                        s = "" + s.charAt(0) + s.charAt(0) + s.charAt(1) + s.charAt(1) + s.charAt(2) + s.charAt(2);
                                    }
                                    entry.setInt(Long.decode(s).intValue());
                                    return true;
                                } catch (NumberFormatException e) {
                                    return false;
                                }
                            },
                            false
                    ));
                } else if (data.hasOption(Option.KEYBIND)) {
                    widgets.add(new KeybindOptionWidget(entry::getInt, entry::setInt));
                } else {
                    widgets.add(new NumberOptionWidget<>(
                            entry::getInt, entry::setInt,
                            parseNumber(data, Integer::parseInt),
                            NumberOptionWidget.INTEGER_FILTER
                    ));
                }
            }
            case BYTE, SHORT, LONG -> {
                OptionRange range = WholeOptionRange.of(entry);
                if (range.hasRange() && data.hasOption(Option.SLIDER)) {
                    widgets.add(new RangeOptionWidget(range));
                } else {
                    widgets.add(switch (entry.type()) {
                        case BYTE ->
                                new NumberOptionWidget<>(entry::getByte, entry::setByte, parseNumber(data, Byte::parseByte), NumberOptionWidget.INTEGER_FILTER);
                        case SHORT ->
                                new NumberOptionWidget<>(entry::getShort, entry::setShort, parseNumber(data, Short::parseShort), NumberOptionWidget.INTEGER_FILTER);
                        case LONG ->
                                new NumberOptionWidget<>(entry::getLong, entry::setLong, parseNumber(data, Long::parseLong), NumberOptionWidget.INTEGER_FILTER);
                        default -> throw new IllegalStateException("Unexpected value: " + entry.type());
                    });
                }
            }
            case FLOAT, DOUBLE -> {
                OptionRange range = DecimalOptionRange.of(entry);
                if (range.hasRange() && data.hasOption(Option.SLIDER)) {
                    widgets.add(new RangeOptionWidget(range));
                } else {
                    widgets.add(switch (entry.type()) {
                        case FLOAT ->
                                new NumberOptionWidget<>(entry::getFloat, entry::setFloat, Float::parseFloat, NumberOptionWidget.DECIMAL_FILTER);
                        case DOUBLE ->
                                new NumberOptionWidget<>(entry::getDouble, entry::setDouble, Double::parseDouble, NumberOptionWidget.DECIMAL_FILTER);
                        default -> throw new IllegalStateException("Unexpected value: " + entry.type());
                    });
                }
            }
            case OBJECT -> throw new IllegalStateException("Unexpected value: " + entry.type());
        }

        var reset = SpriteButton.builder(12, 12)
                .padding(2)
                .sprite(ModSprites.RESET)
                .tooltip(UIConstants.RESET)
                .onPress(resetValue(entry, widgets))
                .build();

        widgets.add(reset);

        list.add(new OptionItem(entry, widgets));
    }

    private static Runnable resetValue(ResourcefulConfigEntry entry, List<AbstractWidget> widgets) {
        return () -> {
            entry.reset();
            for (AbstractWidget widget : widgets) {
                if (widget instanceof ResetableWidget resetable) {
                    resetable.reset();
                }
            }
        };
    }

    private static <T extends Number> Function<String, T> parseNumber(EntryData options, Function<String, T> parser) {
        return s -> {
            T value = parser.apply(s);
            if (options.hasOption(Option.RANGE)) {
                ConfigOption.Range range = options.getOption(Option.RANGE);
                if (value.doubleValue() < range.min()) {
                    throw new NumberFormatException();
                } else if (value.doubleValue() > range.max()) {
                    throw new NumberFormatException();
                }
            }
            return value;
        };
    }
}
