package com.teamresourceful.resourcefulconfig.client.components;

import net.minecraft.resources.ResourceLocation;

public class ModSprites {

    public static final ResourceLocation LINK = new ResourceLocation("resourcefulconfig", "link");
    public static final ResourceLocation RESET = new ResourceLocation("resourcefulconfig", "reset");
    public static final ResourceLocation EDIT = new ResourceLocation("resourcefulconfig", "edit");
    public static final ResourceLocation CHEVRON_DOWN = new ResourceLocation("resourcefulconfig", "chevron_down");
    public static final ResourceLocation CHEVRON_LEFT = new ResourceLocation("resourcefulconfig", "chevron_left");
    public static final ResourceLocation CROSS = new ResourceLocation("resourcefulconfig", "cross");

    public static final ResourceLocation HEADER = new ResourceLocation("resourcefulconfig", "header");
    public static final ResourceLocation CONTAINER = new ResourceLocation("resourcefulconfig", "container");
    public static final ResourceLocation ACCENT = new ResourceLocation("resourcefulconfig", "accent");

    public static final ResourceLocation BUTTON = new ResourceLocation("resourcefulconfig", "button");
    public static final ResourceLocation BUTTON_HOVER = new ResourceLocation("resourcefulconfig", "button_hover");

    public static final ResourceLocation SWITCH_ON = new ResourceLocation("resourcefulconfig", "switch_on");
    public static final ResourceLocation SWITCH_OFF = new ResourceLocation("resourcefulconfig", "switch_off");

    public static ResourceLocation ofButton(boolean hovered) {
        return hovered ? BUTTON_HOVER : BUTTON;
    }

    public static ResourceLocation ofSwitch(boolean on) {
        return on ? SWITCH_ON : SWITCH_OFF;
    }
}
