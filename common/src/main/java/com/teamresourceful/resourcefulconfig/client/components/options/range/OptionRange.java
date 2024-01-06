package com.teamresourceful.resourcefulconfig.client.components.options.range;

import net.minecraft.network.chat.Component;

public interface OptionRange {

    Component toComponent();

    Component minComponent();

    Component maxComponent();

    void setPercent(double value);

    double getPercent();

    double getStepPercent();

    boolean hasRange();
}
