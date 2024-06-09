package com.teamresourceful.resourcefulconfig.client.components;

import net.minecraft.resources.ResourceLocation;

public class ModSprites {

    public static final ResourceLocation LINK = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "link");
    public static final ResourceLocation RESET = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "reset");
    public static final ResourceLocation EDIT = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "edit");
    public static final ResourceLocation CHEVRON_DOWN = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "chevron_down");
    public static final ResourceLocation CHEVRON_LEFT = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "chevron_left");
    public static final ResourceLocation CROSS = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "cross");
    public static final ResourceLocation CHECK = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "check");
    public static final ResourceLocation DRAGGABLE = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "draggable");
    public static final ResourceLocation DELETE = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "delete");

    public static final ResourceLocation HEADER = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "header");
    public static final ResourceLocation CONTAINER = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "container");
    public static final ResourceLocation ACCENT = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "accent");

    public static final ResourceLocation BUTTON = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "button");
    public static final ResourceLocation BUTTON_HOVER = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "button_hover");

    public static final ResourceLocation SWITCH_ON = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "switch_on");
    public static final ResourceLocation SWITCH_OFF = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "switch_off");

    public static ResourceLocation ofButton(boolean hovered) {
        return hovered ? BUTTON_HOVER : BUTTON;
    }

    public static ResourceLocation ofSwitch(boolean on) {
        return on ? SWITCH_ON : SWITCH_OFF;
    }
}
