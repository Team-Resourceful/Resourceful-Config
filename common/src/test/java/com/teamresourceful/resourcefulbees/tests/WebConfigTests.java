package com.teamresourceful.resourcefulbees.tests;

import com.google.gson.JsonPrimitive;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import com.teamresourceful.resourcefulconfig.common.info.ParsedInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WebConfigTests {

    private static final JsonPrimitive COLOR = new JsonPrimitive("#ff00ff");

    @ConfigInfo(
        title = "Test",
        description = "Test",
        icon = "test",
        links = {
            @ConfigInfo.Link(text = "Test", icon = "test", value = "https://www.google.com")
        }
    )
    @ConfigInfo.Color("#ff00ff")
    @ConfigOption.Hidden
    private static final class LoadedClass {}

    @Test
    public void testWebInfoLoading() {
        ResourcefulConfigInfo info = ParsedInfo.of(LoadedClass.class, "id");
        Assertions.assertTrue(info.isHidden());
        Assertions.assertEquals("Test", info.title().value());
        Assertions.assertEquals("Test", info.description().value());
        Assertions.assertEquals("test", info.icon());
        Assertions.assertEquals(COLOR, info.color().toJson());
        Assertions.assertEquals(1, info.links().length);
        Assertions.assertEquals("Test", info.links()[0].text().value());
        Assertions.assertEquals("test", info.links()[0].icon());
        Assertions.assertEquals("https://www.google.com", info.links()[0].url());
    }
}
