package com.teamresourceful.resourcefulconfig.web.annotations;

import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nls;

import java.lang.annotation.*;

@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "1.20.5")
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Link {

    @Pattern("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)")
    String value();

    String icon();

    @Nls(capitalization = Nls.Capitalization.Title)
    String title();
}
