package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigObject;

@ConfigObject
public class DemoObject {

    @ConfigEntry(
        id = "string",
        translation = "Hello World!"
    )
    public String oldString = "Hello World!";

    @ConfigEntry(
        id = "integer",
        translation = "1"
    )
    public int oldInteger = 1;
}
