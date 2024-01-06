package com.teamresourceful.resourcefulconfig.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Category annotation is used to mark a class as a category.
 * <p>
 * Categories are used to group config values together in the config gui it can contain other categories or config values.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Category {

    String value();

    String translation() default "";

    Class<?>[] categories() default {};
}