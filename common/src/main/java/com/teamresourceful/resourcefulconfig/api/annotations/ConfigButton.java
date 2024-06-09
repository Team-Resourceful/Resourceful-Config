package com.teamresourceful.resourcefulconfig.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigButton {

    /**
     * @return returns the title translations of the button.
     */
    String title() default "";

    /**
     * The text to display on the button.
     */
    String text();
}
