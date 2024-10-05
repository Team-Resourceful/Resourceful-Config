package com.teamresourceful.resourcefulconfig.client;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

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
    public static final Component MOD_CONFIGS = Component.translatable("rconfig.ui.constant.mod_configs").withColor(UIConstants.TEXT_TITLE);
    public static final Component MOD_CONFIGS_DESCRIPTION = Component.translatable("rconfig.ui.constant.mod_configs.description").withColor(UIConstants.TEXT_PARAGRAPH);
}
