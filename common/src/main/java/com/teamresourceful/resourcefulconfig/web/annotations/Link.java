package com.teamresourceful.resourcefulconfig.web.annotations;

import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Nls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Link {

    @Pattern("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)")
    String value();

    String icon();

    @Nls(capitalization = Nls.Capitalization.Title)
    String title();
}