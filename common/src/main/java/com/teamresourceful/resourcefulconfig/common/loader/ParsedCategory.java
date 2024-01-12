package com.teamresourceful.resourcefulconfig.common.loader;

import com.teamresourceful.resourcefulconfig.api.annotations.Category;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigButton;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public record ParsedCategory(
    @NotNull String id,
    @NotNull String translation,
    @NotNull ResourcefulConfigInfo info,
    @NotNull LinkedHashMap<String, ResourcefulConfigEntry> entries,
    @NotNull LinkedHashMap<String, ResourcefulConfig> categories,
    @NotNull List<ResourcefulConfigButton> buttons,
    Runnable saveCallback,
    Runnable loadCallback
) implements ResourcefulConfig {

    public ParsedCategory(Category category, ResourcefulConfigInfo info, ResourcefulConfig parent) {
        this(
                category.value(), category.translation(), info,
                new LinkedHashMap<>(), new LinkedHashMap<>(), new ArrayList<>(),
                parent::save, parent::load
        );
    }

    @Override
    public void save() {
        this.saveCallback.run();
    }

    @Override
    public void load() {
        this.loadCallback.run();
    }

    @Override
    public boolean hasFile() {
        return false;
    }
}
