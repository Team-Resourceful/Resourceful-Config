package com.teamresourceful.resourcefulconfig.api.annotations;

import com.teamresourceful.resourcefulconfig.api.config.EntryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigEntry {

    String id();

    EntryType type();

    String translation() default "";
}
