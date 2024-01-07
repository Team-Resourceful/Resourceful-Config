package com.teamresourceful.resourcefulconfig.client.components.options;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigButton;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigObjectEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
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

        for (var value : entries.entrySet()) {
            final EntryData options = value.getValue().options();

            if (options.separator() != null) {
                widget.add(new OptionItem(Component.literal(options.separator()), Component.nullToEmpty(options.separatorDescription()), List.of()));
            }

            for (ResourcefulConfigButton button : buttonsBefore.get(value.getKey())) {
                widget.add(new OptionItem(value.getValue(), List.of(
                        new CustomButton(96, 12, Component.translatable(button.text()), button::invoke)
                )));
            }

            if (value.getValue() instanceof ResourcefulConfigValueEntry entry) {
                populateValueEntry(widget, entry);
            } else if (value.getValue() instanceof ResourcefulConfigObjectEntry entry) {
                widget.add(new OptionItem(entry, List.of(new ObjectOptionWidget(entry))));
            }

            for (ResourcefulConfigButton button : buttonsAfter.get(value.getKey())) {
                widget.add(new OptionItem(value.getValue(), List.of(
                    new CustomButton(96, 12, Component.translatable(button.text()), button::invoke)
                )));
            }
        }
    }

    private static void populateValueEntry(OptionsListWidget list, ResourcefulConfigValueEntry entry) {
        final EntryData options = entry.options();

        AbstractWidget widget = switch (entry.type()) {
            case BOOLEAN -> new BooleanOptionWidget(entry::getBoolean, entry::setBoolean);
            case STRING -> {
                if (options.isMultiline()) {
                    yield new MultilineStringOptionWidget(entry::getString, entry::setString);
                }
                yield new StringOptionWidget(entry::getString, entry::setString);
            }
            case ENUM -> new DropdownWidget(
                    (Enum<?>[]) entry.objectType().getEnumConstants(),
                    entry::getEnum,
                    entry::setEnum
            );
            case BYTE, SHORT, INTEGER, LONG -> {
                OptionRange range = WholeOptionRange.of(entry);
                if (range.hasRange() && options.hasSlider()) {
                    yield new RangeOptionWidget(range);
                }



                yield switch (entry.type()) {
                    case BYTE -> new NumberOptionWidget<>(entry::getByte, entry::setByte, parseNumber(options, Byte::parseByte), NumberOptionWidget.INTEGER_FILTER);
                    case SHORT -> new NumberOptionWidget<>(entry::getShort, entry::setShort, parseNumber(options, Short::parseShort), NumberOptionWidget.INTEGER_FILTER);
                    case INTEGER -> new NumberOptionWidget<>(entry::getInt, entry::setInt, parseNumber(options, Integer::parseInt), NumberOptionWidget.INTEGER_FILTER);
                    case LONG -> new NumberOptionWidget<>(entry::getLong, entry::setLong, parseNumber(options, Long::parseLong), NumberOptionWidget.INTEGER_FILTER);
                    default -> throw new IllegalStateException("Unexpected value: " + entry.type());
                };
            }
            case FLOAT, DOUBLE -> {
                OptionRange range = DecimalOptionRange.of(entry);
                if (range.hasRange() && options.hasSlider()) {
                    yield new RangeOptionWidget(range);
                }
                yield switch (entry.type()) {
                    case FLOAT -> new NumberOptionWidget<>(entry::getFloat, entry::setFloat, Float::parseFloat, NumberOptionWidget.DECIMAL_FILTER);
                    case DOUBLE -> new NumberOptionWidget<>(entry::getDouble, entry::setDouble, Double::parseDouble, NumberOptionWidget.DECIMAL_FILTER);
                    default -> throw new IllegalStateException("Unexpected value: " + entry.type());
                };
            }
            case OBJECT -> throw new IllegalStateException("Unexpected value: " + entry.type());
        };

        var reset = SpriteButton.builder(12, 12)
                .padding(2)
                .sprite(ModSprites.RESET)
                .tooltip(UIConstants.RESET)
                .onPress(resetValue(entry, widget))
                .build();

        list.add(new OptionItem(entry, List.of(widget, reset)));
    }

    private static Runnable resetValue(ResourcefulConfigEntry entry, AbstractWidget widget) {
        return () -> {
            entry.reset();
            if (widget instanceof ResetableWidget resetable) {
                resetable.reset();
            }
        };
    }

    private static <T extends Number> Function<String, T> parseNumber(EntryData options, Function<String, T> parser) {
        return s -> {
            T value = parser.apply(s);
            if (options.hasRange()) {
                if (value.doubleValue() < options.min()) {
                    throw new NumberFormatException();
                } else if (value.doubleValue() > options.max()) {
                    throw new NumberFormatException();
                }
            }
            return value;
        };
    }
}
