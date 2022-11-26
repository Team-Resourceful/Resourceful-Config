package com.teamresourceful.resourcefulconfig.client.options;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;

public class StringInputBox extends EditBox {

    public StringInputBox(Font font, int x, int y, int width, int height, ResourcefulConfigEntry entry) {
        super(font, x + 1, y + 3, width - 2, height - 2, CommonComponents.EMPTY);
        setMaxLength(32767);
        setValue(ParsingUtils.getField(entry.field()).toString());
        setResponder(entry::setString);
    }
}
