package com.teamresourceful.resourcefulconfig.api.types.info;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;

public interface ResourcefulConfigLink {

    @Pattern("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)")
    String url();

    String icon();

    TranslatableValue text();

    default JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("title", this.text().toLocalizedString());
        json.addProperty("icon", this.icon());
        json.addProperty("url", this.url());
        return json;
    }

    static ResourcefulConfigLink create(
            @Subst("https://example.com")
            @Pattern("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)")
            String url,

            String icon,

            TranslatableValue text
    ) {
        return new ResourcefulConfigLink() {

            @Override
            @Pattern("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)")
            public String url() {
                return url;
            }

            @Override
            public String icon() {
                return icon;
            }

            @Override
            public TranslatableValue text() {
                return text;
            }
        };
    }
}
