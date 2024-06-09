package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigObject;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;

@ConfigObject
public class DemoObject {

    @ConfigEntry(
        id = "string",
        translation = "Hello World!",
        type = EntryType.STRING
    )
    public String oldString = "Hello World!";

    @ConfigEntry(
        id = "integer",
        translation = "1",
        type = EntryType.INTEGER
    )
    public int oldInteger = 1;
}
