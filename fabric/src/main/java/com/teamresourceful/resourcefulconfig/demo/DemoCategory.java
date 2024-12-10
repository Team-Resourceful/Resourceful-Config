package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.annotations.Category;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;

@ConfigOption.Hidden
@Category("category")
public class DemoCategory {

    // This category will be hidden

    @ConfigEntry(
            id = "demoInteger",
            translation = "1"
    )
    public static int demoInteger = 1;
}
