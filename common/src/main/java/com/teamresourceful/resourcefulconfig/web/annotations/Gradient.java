package com.teamresourceful.resourcefulconfig.web.annotations;

import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "1.20.5")
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
