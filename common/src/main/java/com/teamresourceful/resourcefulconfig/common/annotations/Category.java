package com.teamresourceful.resourcefulconfig.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Category {

    String id();

    String translation();

    /**
     * The sort order of the category.
     *
     * @apiNote This is temporary as in future versions categories will be defined in the config annotation.
     */
    int sortOrder() default 1000;
}
