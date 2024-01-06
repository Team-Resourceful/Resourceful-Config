package com.teamresourceful.resourcefulconfig.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Comment is an annotation used to add a comment to a config value.
 * <p>
 * This comment will be displayed in the config gui and the config file.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {

    /**
     * The comment to be displayed in the config file.
     * <p>
     * If the Comment annotation does not have a translation value then this value will be used for the gui.
     */
    String value();

    String translation() default "";
}
