package com.teamresourceful.resourcefulconfig.client;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class UIConstants {
    public static final int BACKGROUND = 0xFF131517;
    public static final int TEXT_TITLE = 0xFFFAF9F6;
    public static final int TEXT_PARAGRAPH = 0xFF727478;

    public static final int PAGE_PADDING = 10;
    public static final int SPACING = 4;

    public static final Component BACK = CommonComponents.GUI_BACK;
    public static final Component CLOSE = Component.translatable("rconfig.ui.constant.close");
    public static final Component RESET = Component.translatable("rconfig.ui.constant.reset");
    public static final Component EDIT = Component.translatable("rconfig.ui.constant.edit");
    public static final Component EDIT_STRING = Component.translatable("rconfig.ui.constant.edit.string");
    public static final Component EDIT_LIST = Component.translatable("rconfig.ui.constant.edit.list");
    public static final Component EDIT_OBJECT = Component.translatable("rconfig.ui.constant.edit.object");
    public static final Component CHOOSE_ITEM = Component.translatable("rconfig.ui.constant.choose_item");
    public static final Component ADD_ITEM = Component.translatable("rconfig.ui.constant.add_item");
    public static final Component ADD_ITEM_TOOLTIP = Component.translatable("rconfig.ui.constant.add_item.tooltip");
    public static final Component REMOVE_ITEM = Component.translatable("rconfig.ui.constant.remove_item");
    public static final Component MOVE_UP = Component.translatable("rconfig.ui.constant.move_up");
    public static final Component MOVE_DOWN = Component.translatable("rconfig.ui.constant.move_down");
    public static final Component MOD_CONFIGS = Component.translatable("rconfig.ui.constant.mod_configs").withColor(UIConstants.TEXT_TITLE);
    public static final Component MOD_CONFIGS_DESCRIPTION = Component.translatable("rconfig.ui.constant.mod_configs.description").withColor(UIConstants.TEXT_PARAGRAPH);
    public static final Component SELECT = Component.translatable("rconfig.ui.constant.select");
    public static final Component NONE = Component.translatable("rconfig.ui.constant.none");
    public static final Component REMOVE = Component.translatable("rconfig.ui.constant.remove");
    public static final Component SEARCH_PLACEHOLDER = Component.translatable("rconfig.ui.constant.search_placeholder");

    public static final Component LINK_OPEN = Component.translatable("rconfig.ui.constant.link.open").withColor(0xFFFFFF);
    public static final Component LINK_VERIFICATION = Component.translatable("rconfig.ui.constant.link.verification").withColor(0xFFFFFF);
    public static final String LINK = "rconfig.ui.constant.link";
    public static final MutableComponent KEYBIND_OPEN = Component.translatable("rconfig.ui.constant.keybind.open").withColor(UIConstants.TEXT_PARAGRAPH);
    public static final MutableComponent KEYBIND_CLOSE = Component.translatable("rconfig.ui.constant.keybind.close").withColor(UIConstants.TEXT_PARAGRAPH);

    public static final String RANGE = "rconfig.ui.constant.range";
    public static final String RANGE_MAX = "rconfig.ui.constant.range.max";
    public static final String RANGE_MIN = "rconfig.ui.constant.range.min";
    public static final String COLOR_EYEDROPPER = "rconfig.ui.constant.eyedropper";
    public static final MutableComponent PRESET_SELECTOR_OPEN = Component.translatable("rconfig.ui.constant.preset_selector.open");
    public static final MutableComponent PRESET_SELECTOR_CLOSE = Component.translatable("rconfig.ui.constant.preset_selector.close");
    public static final String PRESET_SELECTOR = "rconfig.ui.constant.preset_selector";


    public static MutableComponent argComponent(String key, Object... args) {
        return Component.translatable(key, args);
    }
}