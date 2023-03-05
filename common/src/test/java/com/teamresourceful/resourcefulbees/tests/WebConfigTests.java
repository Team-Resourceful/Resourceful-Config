package com.teamresourceful.resourcefulbees.tests;

import com.teamresourceful.resourcefulconfig.web.annotations.Link;
import com.teamresourceful.resourcefulconfig.web.annotations.WebInfo;
import com.teamresourceful.resourcefulconfig.web.info.ResourcefulWebConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WebConfigTests {

    @WebInfo(
        hidden = true,
        title = "Test",
        description = "Test",
        icon = "test",
        color = "#ff00ff",
        links = {
            @Link(title = "Test", icon = "test", value = "https://www.google.com")
        }
    )
    private static final class LoadedClass {}

    @Test
    public void testWebInfoLoading() {
        ResourcefulWebConfig config = ResourcefulWebConfig.of(LoadedClass.class);
        Assertions.assertTrue(config.hidden());
        Assertions.assertEquals("Test", config.title());
        Assertions.assertEquals("Test", config.description());
        Assertions.assertEquals("test", config.icon());
        Assertions.assertEquals("#ff00ff", config.color());
        Assertions.assertEquals(1, config.links().length);
        Assertions.assertEquals("Test", config.links()[0].title());
        Assertions.assertEquals("test", config.links()[0].icon());
        Assertions.assertEquals("https://www.google.com", config.links()[0].value());
    }
}
