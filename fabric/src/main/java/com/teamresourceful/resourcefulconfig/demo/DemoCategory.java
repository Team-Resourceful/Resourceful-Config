package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.annotations.Category;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;

@Category(value = "category", categories = { DemoSubCategory.class })
public class DemoCategory {

    // TEMP: made visible with a sub category to test recursive search

    @ConfigEntry(
            id = "demoInteger",
            translation = "1"
    )
    public static int demoInteger = 1;
}


