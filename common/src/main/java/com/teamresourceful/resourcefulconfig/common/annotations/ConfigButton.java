package com.teamresourceful.resourcefulconfig.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigButton {

    /**
     * The config entry it will be after.
     */
    String after() default "";

    String translation();

    String text();
}
