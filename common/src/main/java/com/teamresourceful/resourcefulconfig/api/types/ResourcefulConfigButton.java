package com.teamresourceful.resourcefulconfig.api.types;

import com.teamresourceful.resourcefulconfig.api.types.options.Position;

/**
 * This interface is used to define a button that can be added to the config screen.
 */
public interface ResourcefulConfigButton {

    String target();

    Position position();

    String text();

    boolean invoke();
}
