package com.teamresourceful.resourcefulconfig.api.types;

import com.teamresourceful.resourcefulconfig.api.types.options.Position;

/**
 * This interface is used to define a button that can be added to the config screen.
 */
public interface ResourcefulConfigButton {

    /**
     * @return returns the title translations of the button.
     */
    String title();

    /**
     * @return returns the description translations of the button.
     */
    String description();

    String target();

    Position position();

    String text();

    boolean invoke();
}
