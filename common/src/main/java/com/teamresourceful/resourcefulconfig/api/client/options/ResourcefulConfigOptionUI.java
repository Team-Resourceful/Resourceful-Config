package com.teamresourceful.resourcefulconfig.api.client.options;

import com.teamresourceful.resourcefulconfig.api.types.options.data.DraggableOptionEntry;
import com.teamresourceful.resourcefulconfig.client.components.options.types.DraggableListOptionWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.types.DropdownWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.types.SelectWidget;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Contains option specific UI components and utilities.
 */
public class ResourcefulConfigOptionUI {

    public static <T> AbstractWidget draggable(
            Component title,
            List<DraggableOptionEntry<T>> options,
            Supplier<List<T>> getter,
            Consumer<List<T>> setter
    ) {
        return new DraggableListOptionWidget<>(title, options, getter, setter, null);
    }

    public static <T> AbstractWidget draggable(
            Component title,
            List<DraggableOptionEntry<T>> options,
            Supplier<List<T>> getter, Consumer<List<T>> setter,
            int min, int max
    ) {
        return new DraggableListOptionWidget<>(title, options, getter, setter, IntIntPair.of(min, max));
    }

    public static <T> AbstractWidget select(
            Component title,
            List<T> options,
            Supplier<List<T>> getter, Consumer<List<T>> setter
    ) {
        return new SelectWidget<>(title, options, getter, setter);
    }

    public static <T> AbstractWidget dropdown(
            Component title,
            List<T> options,
            Supplier<T> getter, Consumer<T> setter
    ) {
        return new DropdownWidget<>(title, options, getter, setter);
    }
}
