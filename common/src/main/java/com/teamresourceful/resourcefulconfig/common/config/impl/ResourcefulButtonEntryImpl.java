package com.teamresourceful.resourcefulconfig.common.config.impl;

import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigButton;

import java.lang.reflect.Method;

public record ResourcefulButtonEntryImpl(String after, Method method) implements ResourcefulConfigButton {

}
