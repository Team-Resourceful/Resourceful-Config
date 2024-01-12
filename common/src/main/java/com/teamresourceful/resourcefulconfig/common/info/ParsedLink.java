package com.teamresourceful.resourcefulconfig.common.info;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink;
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue;

public record ParsedLink(
        String url,
        String icon,
        TranslatableValue text
) implements ResourcefulConfigLink {

    public static ResourcefulConfigLink of(ConfigInfo.Link link) {
        return new ParsedLink(link.value(), link.icon(), new TranslatableValue(link.text(), link.textTranslation()));
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("title", this.text().toLocalizedString());
        json.addProperty("icon", this.icon());
        json.addProperty("url", this.url());
        return json;
    }
}
