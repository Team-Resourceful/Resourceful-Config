package com.teamresourceful.resourcefulconfig.client.components.options.text.multiline;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefulconfig.client.utils.ListenableState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Whence;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

class MultilineTextInput extends MultilineTextState {

    public MultilineTextInput(ListenableState<String> state) {
        super(state);
    }

    public boolean onMouseDrag(double mouseX, double mouseY, int button) {
        this.selecting = true;
        var result = this.onMouseClick(mouseX, mouseY, button);
        this.selecting = false;
        return result;
    }

    public boolean onMouseClick(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int lineX = Mth.floor(mouseX);
            int lineY = Mth.floor(mouseY / 9.0);
            MultilineStringView line = this.lines.get(Mth.clamp(lineY, 0, this.lines.size() - 1));
            int k = this.font.plainSubstrByWidth(this.value.substring(line.start(), line.end()), lineX).length();
            this.seekCursor(Whence.ABSOLUTE, line.start() + k);
            return true;
        }
        return false;
    }

    public boolean onCharTyped(char character) {
        this.insertText(Character.toString(character));
        return true;
    }

    public boolean onKeyPress(int i) {
        if (Screen.isSelectAll(i)) {
            this.cursor = this.value.length();
            this.selectCursor = 0;
        } else if (Screen.isCopy(i)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.selection().substring(this.value));
        } else if (Screen.isPaste(i)) {
            this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
        } else if (Screen.isCut(i)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.selection().substring(this.value));
            this.insertText("");
        } else {
            switch (i) {
                case InputConstants.KEY_RETURN, InputConstants.KEY_NUMPADENTER -> this.insertText("\n");
                case InputConstants.KEY_BACKSPACE -> {
                    if (Screen.hasControlDown()) {
                        this.deleteText(this.getPreviousWordStart() - this.cursor);
                    } else {
                        this.deleteText(-1);
                    }
                }
                case InputConstants.KEY_DELETE -> {
                    if (Screen.hasControlDown()) {
                        this.deleteText(this.getNextWordStart() - this.cursor);
                    } else {
                        this.deleteText(1);
                    }
                }
                case InputConstants.KEY_RIGHT -> {
                    if (Screen.hasControlDown()) {
                        this.seekCursor(Whence.ABSOLUTE, this.getNextWordStart());
                    } else {
                        this.seekCursor(Whence.RELATIVE, 1);
                    }
                }
                case InputConstants.KEY_LEFT -> {
                    if (Screen.hasControlDown()) {
                        this.seekCursor(Whence.ABSOLUTE, this.getPreviousWordStart());
                    } else {
                        this.seekCursor(Whence.RELATIVE, -1);
                    }
                }
                case InputConstants.KEY_DOWN -> {
                    if (!Screen.hasControlDown()) {
                        this.moveCursorY(1);
                    }
                }
                case InputConstants.KEY_UP -> {
                    if (!Screen.hasControlDown()) {
                        this.moveCursorY(-1);
                    }
                }
                case InputConstants.KEY_PAGEUP -> this.seekCursor(Whence.ABSOLUTE, 0);
                case InputConstants.KEY_PAGEDOWN -> this.seekCursor(Whence.END, 0);
                case InputConstants.KEY_HOME -> {
                    if (Screen.hasControlDown()) {
                        this.seekCursor(Whence.ABSOLUTE, 0);
                    } else {
                        this.seekCursor(Whence.ABSOLUTE, this.getCursorLineView().start());
                    }
                }
                case InputConstants.KEY_END -> {
                    if (Screen.hasControlDown()) {
                        this.seekCursor(Whence.END, 0);
                    } else {
                        this.seekCursor(Whence.ABSOLUTE, this.getCursorLineView().end());
                    }
                }
                default -> {
                    return false;
                }
            }
        }

        return true;
    }
}
