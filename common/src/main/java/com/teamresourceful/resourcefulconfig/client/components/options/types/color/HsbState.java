package com.teamresourceful.resourcefulconfig.client.components.options.types.color;

import com.teamresourceful.resourcefulconfig.client.utils.State;

import java.util.function.Consumer;

public class HsbState implements State<HsbColor> {

    private final Consumer<HsbColor> onChange;
    private HsbColor color;
    private boolean hasChanged = false;

    public HsbState(HsbColor color, Consumer<HsbColor> onChange) {
        this.color = color;
        this.onChange = onChange;
    }

    @Override
    public void set(HsbColor color) {
        this.color = color;
        this.hasChanged = true;
        this.onChange.accept(color);
    }

    @Override
    public HsbColor get() {
        return color;
    }

    public boolean hasChanged() {
        return hasChanged;
    }
}
