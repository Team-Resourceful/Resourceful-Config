package com.teamresourceful.resourcefulconfig.web.annotations;

import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Nls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebInfo {

    String icon() default "box";

    @Pattern("^#(?:[0-9a-fA-F]{3}){1,2}$")
    String color() default "#ffffff";

    @Nls(capitalization = Nls.Capitalization.Title)
    String title() default "";

    @Nls(capitalization = Nls.Capitalization.Sentence)
    String description() default "";

    Link[] links() default {};

    boolean hidden() default false;
}
