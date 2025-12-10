package com.teamresourceful.resourcefulconfig.demo;

import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigColor;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigColorValue;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink;
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue;

public class DemoInfoProvider implements ResourcefulConfigInfo {

    private final String id;

    public DemoInfoProvider(String id) {
        this.id = id;
    }

    @Override
    public TranslatableValue title() {
        return new TranslatableValue(id);
    }

    @Override
    public TranslatableValue description() {
        return new TranslatableValue(
                "This is a demo provider config. " +
                "This is super long to test multiline descriptions in the header of the config screen. " +
                "Even longer than that to see how it wraps around and looks in the UI."
        );
    }

    @Override
    public String icon() {
        return "box";
    }

    @Override
    public ResourcefulConfigColor color() {
        return (ResourcefulConfigColorValue) () -> "#FF0000";
    }

    @Override
    public ResourcefulConfigLink[] links() {
        return new ResourcefulConfigLink[] {
                ResourcefulConfigLink.create("https://example.com", "", TranslatableValue.EMPTY),
                ResourcefulConfigLink.create("https://example.com", "", TranslatableValue.EMPTY),
                ResourcefulConfigLink.create("https://example.com", "", TranslatableValue.EMPTY),
                ResourcefulConfigLink.create("https://example.com", "", TranslatableValue.EMPTY),
                ResourcefulConfigLink.create("https://example.com", "", TranslatableValue.EMPTY),
                ResourcefulConfigLink.create("https://example.com", "", TranslatableValue.EMPTY),
                ResourcefulConfigLink.create("https://example.com", "", TranslatableValue.EMPTY),
                ResourcefulConfigLink.create("https://example.com", "", TranslatableValue.EMPTY),
        };
    }

    @Override
    public boolean isHidden() {
        return false;
    }
}
