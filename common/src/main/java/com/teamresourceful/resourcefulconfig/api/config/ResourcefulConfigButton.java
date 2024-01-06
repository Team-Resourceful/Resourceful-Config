package com.teamresourceful.resourcefulconfig.api.config;

import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * This interface is used to define a button that can be added to the config screen.
 * <p>
 * If the annotation isn't added for this to work yell at me.
 */
public interface ResourcefulConfigButton extends AnnotatedConfigValue {

    String after();

    Method method();

    boolean invoke();

    @Override
    @ApiStatus.NonExtendable
    default AnnotatedElement element() {
        return method();
    }
}
