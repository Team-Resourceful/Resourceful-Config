package com.teamresourceful.resourcefulconfig.api.types.info;

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue;

/**
 * Generic information about a config.
 * This is used to provide information about a config to the web interface and mod menu.
 */
public interface ResourcefulConfigInfo {

    TranslatableValue title();

    TranslatableValue description();

    String icon();

    ResourcefulConfigColor color();

    ResourcefulConfigLink[] links();

    boolean isHidden();
}
