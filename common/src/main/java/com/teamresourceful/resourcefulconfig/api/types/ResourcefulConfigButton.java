package com.teamresourceful.resourcefulconfig.api.types;

import com.teamresourceful.resourcefulconfig.api.types.options.Position;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Predicate;

/**
 * This interface is used to define a button that can be added to the config screen.
 */
public interface ResourcefulConfigButton extends ResourcefulConfigElement {

    /**
     * @return returns the title translations of the button.
     */
    String title();

    /**
     * @return returns the description translations of the button.
     */
    String description();

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = ">1.21.5")
    default String target() {
        return "";
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = ">1.21.5")
    default Position position() {
        return Position.AFTER;
    }

    String text();

    boolean invoke();

    @Override
    default boolean search(Predicate<String> predicate) {
        return predicate.test(title()) || predicate.test(description()) || predicate.test(text());
    }
}
