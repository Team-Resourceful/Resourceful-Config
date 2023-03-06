package com.teamresourceful.resourcefulconfig.web.annotations;

import org.intellij.lang.annotations.Pattern;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Gradient {

    boolean disabled() default false;

    @Pattern("\\d+deg")
    String value();

    @Pattern("^#(?:[0-9a-fA-F]{3}){1,2}$")
    String first();

    @Pattern("^#(?:[0-9a-fA-F]{3}){1,2}$")
    String second();
}
