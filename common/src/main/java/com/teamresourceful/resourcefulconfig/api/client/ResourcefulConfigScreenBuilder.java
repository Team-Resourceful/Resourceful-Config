package com.teamresourceful.resourcefulconfig.api.client;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.client.ConfigScreenContext;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;
import java.util.function.Function;

public class ResourcefulConfigScreenBuilder {

    private final ResourcefulConfig config;

    private Screen parent = null;
    private Function<String, List<String>> termCollector = s -> List.of();
    private String query = "";

    protected ResourcefulConfigScreenBuilder(ResourcefulConfig config) {
        this.config = config;
    }

    public ResourcefulConfigScreenBuilder withParent(Screen parent) {
        this.parent = parent;
        return this;
    }

    public ResourcefulConfigScreenBuilder withTermCollector(Function<String, List<String>> termCollector) {
        this.termCollector = termCollector;
        return this;
    }

    public ResourcefulConfigScreenBuilder withQuery(String query) {
        this.query = query;
        return this;
    }

    public Screen build() {
        return new ConfigScreen(
                this.parent,
                this.config,
                new ConfigScreenContext(
                        this.query,
                        this.termCollector
                )
        );
    }

}
