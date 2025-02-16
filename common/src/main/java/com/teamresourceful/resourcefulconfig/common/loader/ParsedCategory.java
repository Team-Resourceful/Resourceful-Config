package com.teamresourceful.resourcefulconfig.common.loader;

import com.teamresourceful.resourcefulconfig.api.annotations.Category;
import com.teamresourceful.resourcefulconfig.api.patching.ConfigPatchEvent;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigButton;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigCategory;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public record ParsedCategory(
    @NotNull String id,
    @NotNull ResourcefulConfig parent,
    @NotNull ResourcefulConfigInfo info,
    @NotNull LinkedHashMap<String, ResourcefulConfigEntry> entries,
    @NotNull LinkedHashMap<String, ResourcefulConfig> categories,
    @NotNull List<ResourcefulConfigButton> buttons
) implements ResourcefulConfigCategory {

    public ParsedCategory(Category category, ResourcefulConfigInfo info, ResourcefulConfig parent) {
        this(
                category.value(),
                parent,
                info,
                new LinkedHashMap<>(), new LinkedHashMap<>(), new ArrayList<>()
        );
    }

    @Override
    public void save() {
        this.parent.save();
    }

    @Override
    public void load(Consumer<ConfigPatchEvent> handler) {
        this.parent.load(handler);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ParsedCategory parsedCategory)) return false;
        return Objects.equals(this.id, parsedCategory.id) &&
                Objects.equals(this.info, parsedCategory.info) &&
                Objects.equals(this.entries, parsedCategory.entries) &&
                Objects.equals(this.categories, parsedCategory.categories) &&
                Objects.equals(this.buttons, parsedCategory.buttons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.id,
                this.info,
                this.entries,
                this.categories,
                this.buttons
        );
    }
}
