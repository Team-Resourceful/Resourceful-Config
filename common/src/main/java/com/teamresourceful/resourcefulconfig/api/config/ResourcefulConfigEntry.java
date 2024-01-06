package com.teamresourceful.resourcefulconfig.api.config;

import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

public interface ResourcefulConfigEntry extends AnnotatedConfigValue {

    EntryType type();

    Field field();

    EntryOptions options();

    void reset();

    @Override
    @ApiStatus.NonExtendable
    default AnnotatedElement element() {
        return field();
    }
}
