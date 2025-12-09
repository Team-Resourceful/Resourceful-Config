package com.teamresourceful.resourcefulconfig.client.components;

import net.minecraft.resources.Identifier;

public class ModSprites {

    public static final Identifier LINK = Identifier.fromNamespaceAndPath("resourcefulconfig", "icons/link");
    public static final Identifier CURSEFORGE = Identifier.fromNamespaceAndPath("resourcefulconfig", "icons/curseforge");
    public static final Identifier MODRINTH = Identifier.fromNamespaceAndPath("resourcefulconfig", "icons/modrinth");
    public static final Identifier DOWNLOAD = Identifier.fromNamespaceAndPath("resourcefulconfig", "icons/download");
    public static final Identifier CODE = Identifier.fromNamespaceAndPath("resourcefulconfig", "icons/code");
    public static final Identifier CODE2 = Identifier.fromNamespaceAndPath("resourcefulconfig", "icons/code_2");
    public static final Identifier CLIPBOARD = Identifier.fromNamespaceAndPath("resourcefulconfig", "icons/clipboard");
    public static final Identifier CLIPBOARD_LIST = Identifier.fromNamespaceAndPath("resourcefulconfig", "icons/clipboard_list");
    public static final Identifier CLIPBOARD_EDIT = Identifier.fromNamespaceAndPath("resourcefulconfig", "icons/clipboard_edit");

    public static final Identifier RESET = Identifier.fromNamespaceAndPath("resourcefulconfig", "reset");
    public static final Identifier EDIT = Identifier.fromNamespaceAndPath("resourcefulconfig", "edit");
    public static final Identifier CHEVRON_DOWN = Identifier.fromNamespaceAndPath("resourcefulconfig", "chevron_down");
    public static final Identifier CHEVRON_LEFT = Identifier.fromNamespaceAndPath("resourcefulconfig", "chevron_left");
    public static final Identifier CROSS = Identifier.fromNamespaceAndPath("resourcefulconfig", "cross");
    public static final Identifier CHECK = Identifier.fromNamespaceAndPath("resourcefulconfig", "check");
    public static final Identifier DRAGGABLE = Identifier.fromNamespaceAndPath("resourcefulconfig", "draggable");
    public static final Identifier DELETE = Identifier.fromNamespaceAndPath("resourcefulconfig", "delete");
    public static final Identifier EYE_DROPPER = Identifier.fromNamespaceAndPath("resourcefulconfig", "eye_dropper");

    public static final Identifier HEADER = Identifier.fromNamespaceAndPath("resourcefulconfig", "header");
    public static final Identifier CONTAINER = Identifier.fromNamespaceAndPath("resourcefulconfig", "container");
    public static final Identifier ACCENT = Identifier.fromNamespaceAndPath("resourcefulconfig", "accent");

    public static final Identifier BUTTON = Identifier.fromNamespaceAndPath("resourcefulconfig", "button");
    public static final Identifier BUTTON_HOVER = Identifier.fromNamespaceAndPath("resourcefulconfig", "button_hover");

    public static final Identifier SWITCH_ON = Identifier.fromNamespaceAndPath("resourcefulconfig", "switch_on");
    public static final Identifier SWITCH_OFF = Identifier.fromNamespaceAndPath("resourcefulconfig", "switch_off");

    public static Identifier ofButton(boolean hovered) {
        return hovered ? BUTTON_HOVER : BUTTON;
    }

    public static Identifier ofSwitch(boolean on) {
        return on ? SWITCH_ON : SWITCH_OFF;
    }

    public static Identifier ofIcon(String icon) {
        return switch (icon.intern()) {
            case "curseforge" -> CURSEFORGE;
            case "modrinth" -> MODRINTH;
            case "download" -> DOWNLOAD;
            case "code" -> CODE;
            case "code-2" -> CODE2;
            case "clipboard" -> CLIPBOARD;
            case "clipboard-list" -> CLIPBOARD_LIST;
            case "clipboard-edit" -> CLIPBOARD_EDIT;
            default -> LINK;
        };
    }
}
