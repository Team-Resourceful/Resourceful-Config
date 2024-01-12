package com.teamresourceful.resourcefulconfig.api.types.info;

import com.google.gson.JsonElement;
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue;
import org.intellij.lang.annotations.Pattern;

public interface ResourcefulConfigLink {

    @Pattern("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)")
    String url();

    String icon();

    TranslatableValue text();

    JsonElement toJson();
}
