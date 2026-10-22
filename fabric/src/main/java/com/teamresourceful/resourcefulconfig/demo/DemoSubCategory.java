package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.annotations.Category;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;

@Category("sub")
public class DemoSubCategory {

    @ConfigEntry(id = "demoInteger", translation = "1")
    public static int demoInteger = 1;
}