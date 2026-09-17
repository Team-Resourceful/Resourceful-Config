package com.teamresourceful.resourcefulconfig.api.annotations;

import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ConfigOption {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Range {

        double min();

        double max();
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Slider {

    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Multiline {

    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Color {

        /**
         * The presets to display in the color picker.
         */
        int[] presets() default {};

        boolean alpha() default false;
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Regex {

        @Language("RegExp")
        String value();
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Separator {

        String value();

        String description() default "";
    }

    @Target({ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Hidden {
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Select {
        String value() default "Select";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Draggable {

        String[] value() default {};
    }

    /**
     * @deprecated Keybinds should not be handled by the config system and instead should be in the vanilla keybind system.
     * You can use the ResourcefulConfigUI to create a keybind option in your config screen, but the value should be stored in the vanilla keybind system.
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Deprecated
    public @interface Keybind {


    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SearchTerm {

        String[] value();
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Renderer {

        @Pattern("^([a-z0-9._-]+:)?[a-z0-9/._-]+$")
        String value();
    }
}
