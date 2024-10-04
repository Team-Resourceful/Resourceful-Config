package com.teamresourceful.resourcefulconfig.api.annotations;

import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigEntry {

    String id();

    /**
     * @deprecated Type is no longer required and it will be inferred instead of parsing.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.21.2")
    EntryType type() default EntryType.CANNOT_BE_PARSED;

    String translation() default "";
}
