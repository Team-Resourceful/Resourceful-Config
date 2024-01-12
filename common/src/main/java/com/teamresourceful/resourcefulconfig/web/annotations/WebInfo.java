package com.teamresourceful.resourcefulconfig.web.annotations;

import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "1.20.5")
@SuppressWarnings("unused")
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebInfo {

    String icon() default "box";

    @Pattern("^#(?:[0-9a-fA-F]{3}){1,2}$")
    String color() default "#ffffff";

    Gradient gradient() default @Gradient(disabled = true, value = "0deg", first = "#ffffff", second = "#ffffff");

    @Nls(capitalization = Nls.Capitalization.Title)
    String title() default "";

    @Nls(capitalization = Nls.Capitalization.Sentence)
    String description() default "";

    Link[] links() default {};

    /**
     * @deprecated @use {@link com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption.Hidden} instead.
     */
    @Deprecated
    boolean hidden() default false;
}
