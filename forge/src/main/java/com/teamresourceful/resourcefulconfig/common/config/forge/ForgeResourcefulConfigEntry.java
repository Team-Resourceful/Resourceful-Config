package com.teamresourceful.resourcefulconfig.common.config.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;

import java.lang.reflect.Field;

public class ForgeResourcefulConfigEntry implements ResourcefulConfigEntry {

    private final EntryType type;
    private final Field field;
    private final Object defaultValue;

    private final ForgeConfigSpec.ConfigValue<?> configValue;

    private ForgeResourcefulConfigEntry(EntryType type, Field field, Object defaultValue, ForgeConfigSpec.ConfigValue<?> configValue) {
        this.type = type;
        this.field = field;
        this.defaultValue = defaultValue;
        this.configValue = configValue;
    }

    public static ForgeResourcefulConfigEntry create(ConfigEntry entry, Field field, ForgeConfigSpec.Builder builder) {
        Object value = ParsingUtils.getField(field);
        ForgeConfigSpec.ConfigValue<?> configValue = ForgeConfigUtils.addEntry(builder, entry.id(), field, entry.type(), value);

        return new ForgeResourcefulConfigEntry(entry.type(), field, value, configValue);
    }

    @Override
    public EntryType type() {
        return type;
    }

    @Override
    public Field field() {
        return field;
    }

    @Override
    public Object defaultValue() {
        return defaultValue;
    }

    public void saveEntry() {
        ForgeConfigUtils.setEntry(configValue, ParsingUtils.getField(field));
    }
}
