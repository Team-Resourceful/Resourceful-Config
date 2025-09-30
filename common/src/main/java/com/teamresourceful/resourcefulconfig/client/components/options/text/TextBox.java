package com.teamresourceful.resourcefulconfig.client.components.options.text;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.text.utils.TextBoxStringUtils;
import com.teamresourceful.resourcefulconfig.client.utils.ListenableState;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextBox extends BaseWidget {
    protected static final int PADDING = 4;
    protected final Font font = Minecraft.getInstance().font;
    protected final ListenableState<String> state;
    protected int textColor = -1;
    private boolean shiftPressed;
    private int maxLength = Short.MAX_VALUE;
    private int displayPos;
    private int cursorPos;
    private int highlightPos;

    protected int placeholderColor = -1;
    protected String placeholder = "";

    public TextBox(int width, int height, ListenableState<@NotNull String> state) {
        super(width, height);
        this.state = state;

        this.setCursorPosition(this.state.get().length());
        this.setHighlightPos(this.cursorPos);
        this.displayPos = 0;
    }

    public String getValue() {
        return this.state.get();
    }

    public void setValue(String text) {
        if (text.length() > this.maxLength) {
            this.state.set(text.substring(0, this.maxLength));
        } else {
            this.state.set(text);
        }

        this.moveCursorTo(this.state.get().length());
        this.setHighlightPos(this.cursorPos);
    }

    public String getHighlighted() {
        int min = Math.min(this.cursorPos, this.highlightPos);
        int max = Math.max(this.cursorPos, this.highlightPos);
        return this.state.get().substring(min, max);
    }

    public void insertText(String textToWrite) {
        int min = Math.min(this.cursorPos, this.highlightPos);
        int max = Math.max(this.cursorPos, this.highlightPos);
        int k = this.maxLength - this.state.get().length() - (min - max);
        String string = TextBoxStringUtils.filterText(textToWrite);
        int l = string.length();
        if (k < l) {
            string = string.substring(0, k);
            l = k;
        }

        String string2 = new StringBuilder(this.state.get()).replace(min, max, string).toString();
        this.state.set(string2);
        this.setCursorPosition(min + l);
        this.setHighlightPos(this.cursorPos);
    }

    private void deleteText(int count) {
        if (this.state.get().isEmpty()) {
            return;
        }

        if (Screen.hasControlDown()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                this.deleteChars(this.getWordPosition(count) - this.cursorPos);
            }
        } else {
            this.deleteChars(count);
        }
    }

    public void deleteChars(int num) {
        if (!this.state.get().isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                int i = this.getCursorPos(num);
                int j = Math.min(i, this.cursorPos);
                int k = Math.max(i, this.cursorPos);
                if (j != k) {
                    String string = new StringBuilder(this.state.get()).delete(j, k).toString();
                    this.state.set(string);
                    this.moveCursorTo(j);
                }
            }
        }
    }

    public int getWordPosition(int numWords) {
        return this.getWordPosition(numWords, this.cursorPos);
    }

    private int getWordPosition(int n, int pos) {
        int i = pos;
        boolean bl = n < 0;
        int j = Math.abs(n);

        String value = this.state.get();
        for (int k = 0; k < j; ++k) {
            if (!bl) {
                int l = value.length();
                i = value.indexOf(32, i);
                if (i == -1) {
                    i = l;
                } else {
                    while (i < l && value.charAt(i) == ' ') {
                        ++i;
                    }
                }
            } else {
                while (i > 0 && value.charAt(i - 1) == ' ') {
                    --i;
                }

                while (i > 0 && value.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }

        return i;
    }

    private int getCursorPos(int delta) {
        return Util.offsetByCodepoints(this.state.get(), this.cursorPos, delta);
    }

    public void moveCursorTo(int pos) {
        this.setCursorPosition(pos);
        if (!this.shiftPressed) {
            this.setHighlightPos(this.cursorPos);
        }
    }

    public void setCursorPosition(int pos) {
        this.cursorPos = Mth.clamp(pos, 0, this.state.get().length());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isVisible() || !this.isFocused()) {
            return false;
        }

        this.shiftPressed = Screen.hasShiftDown();
        if (Screen.isSelectAll(keyCode)) {
            this.moveCursorTo(this.state.get().length());
            this.setHighlightPos(0);
            return true;
        } else if (Screen.isCopy(keyCode)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
            return true;
        } else if (Screen.isPaste(keyCode)) {
            this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
            return true;
        } else if (Screen.isCut(keyCode)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
            this.insertText("");
            return true;
        } else {
            return switch (keyCode) {
                case InputConstants.KEY_BACKSPACE -> {
                    this.shiftPressed = false;
                    this.deleteText(-1);
                    this.shiftPressed = Screen.hasShiftDown();
                    yield true;
                }
                case InputConstants.KEY_DELETE -> {
                    this.shiftPressed = false;
                    this.deleteText(1);
                    this.shiftPressed = Screen.hasShiftDown();
                    yield true;
                }
                case InputConstants.KEY_RIGHT -> {
                    int pos = Screen.hasControlDown() ? this.getWordPosition(1) : this.getCursorPos(1);
                    this.moveCursorTo(pos);
                    yield true;
                }
                case InputConstants.KEY_LEFT -> {
                    int pos = Screen.hasControlDown() ? this.getWordPosition(-1) : this.getCursorPos(-1);
                    this.moveCursorTo(pos);
                    yield true;
                }
                case InputConstants.KEY_HOME -> {
                    this.moveCursorTo(0);
                    yield true;
                }
                case InputConstants.KEY_END -> {
                    this.moveCursorTo(this.state.get().length());
                    yield true;
                }
                default -> false;
            };
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!this.isVisible() || !this.isFocused()) {
            return false;
        }
        if (!TextBoxStringUtils.isAllowedChatCharacter(codePoint)) {
            return false;
        }
        this.insertText(Character.toString(codePoint));
        return true;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        int relativeX = Mth.floor(mouseX) - this.getX() - PADDING;
        String string = TextBoxStringUtils.plainHeadByWidth(
                this.font,
                this.state.get().substring(this.displayPos),
                this.width - PADDING * 2);
        this.moveCursorTo(TextBoxStringUtils.plainHeadByWidth(this.font, string, relativeX).length() + this.displayPos);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.isVisible()) {
            String value = this.state.get();
            boolean showPlaceholder = value.isEmpty() && !this.placeholder.isEmpty() && !this.isFocused();
            if (showPlaceholder) {
                value = this.placeholder;
            }
            int textColor = showPlaceholder ? this.placeholderColor : this.textColor;

            int displayCursorDiff = this.cursorPos - this.displayPos;
            int displayHighlightDiff = this.highlightPos - this.displayPos;
            String truncatedValue = TextBoxStringUtils.plainHeadByWidth(
                    this.font,
                    value.substring(this.displayPos),
                    this.width - 8);
            boolean cursorVisible = displayCursorDiff >= 0 && displayCursorDiff <= truncatedValue.length();
            boolean showCursor = this.isFocused() && cursorVisible && System.currentTimeMillis() / 500 % 2 == 0;
            int l = this.getX() + 4;
            int m = this.getY() + (this.height - 8) / 2;
            int n = l;
            if (displayHighlightDiff > truncatedValue.length()) {
                displayHighlightDiff = truncatedValue.length();
            }

            if (!truncatedValue.isEmpty()) {
                String string2 = cursorVisible ? truncatedValue.substring(0, displayCursorDiff) : truncatedValue;
                graphics.drawString(this.font, TextBoxStringUtils.format(string2), l, m, textColor, false);
                n = l + TextBoxStringUtils.width(font, string2) + 1;
            }

            boolean bl3 = this.cursorPos < value.length() || value.length() >= this.maxLength;
            int o = n;
            if (!cursorVisible) {
                o = displayCursorDiff > 0 ? l + this.width : l;
            } else if (bl3) {
                o = n - 1;
                --n;
            }

            if (!truncatedValue.isEmpty() && cursorVisible && displayCursorDiff < truncatedValue.length()) {
                graphics.drawString(
                        this.font,
                        TextBoxStringUtils.format(truncatedValue.substring(displayCursorDiff)),
                        n,
                        m,
                        textColor,
                        false);
            }

            if (showCursor) {
                graphics.fill(RenderPipelines.GUI, o - 1, m - 1, o, m + 1 + 9, 0xffd0d0d0);
            }

            if (displayHighlightDiff != displayCursorDiff) {
                int p = l + TextBoxStringUtils.width(this.font, truncatedValue.substring(0, displayHighlightDiff));
                this.renderHighlight(graphics, o, m - 1, p - 1, m + 1 + 9);
            }
        }
    }

    private void renderHighlight(GuiGraphics graphics, int minX, int minY, int maxX, int maxY) {
        int x1 = Mth.clamp(Math.min(minX, maxX), this.getX(), this.getX() + this.width - PADDING);
        int x2 = Mth.clamp(Math.max(minX, maxX), this.getX(), this.getX() + this.width - PADDING);
        int y1 = Math.min(minY, maxY);
        int y2 = Math.max(minY, maxY);
        graphics.fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, x1, y1, x2, y2, 0xff0000ff);
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(@NotNull FocusNavigationEvent event) {
        return this.visible ? super.nextFocusPath(event) : null;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.visible
                && mouseX >= (double) this.getX()
                && mouseX < (double) (this.getX() + this.width)
                && mouseY >= (double) this.getY()
                && mouseY < (double) (this.getY() + this.height);
    }

    public void setHighlightPos(int position) {
        int length = this.state.get().length();
        this.highlightPos = Mth.clamp(position, 0, length);
        if (this.displayPos > length) {
            this.displayPos = length;
        }

        int textWidth = this.width - PADDING * 2;
        String string = TextBoxStringUtils.plainHeadByWidth(
                this.font,
                this.state.get().substring(this.displayPos),
                textWidth);
        int k = string.length() + this.displayPos;
        if (this.highlightPos == this.displayPos) {
            this.displayPos -= TextBoxStringUtils.plainTailByWidth(this.font, this.state.get(), textWidth).length();
        }

        if (this.highlightPos > k) {
            this.displayPos += this.highlightPos - k;
        } else if (this.highlightPos <= this.displayPos) {
            this.displayPos -= this.displayPos - this.highlightPos;
        }

        this.displayPos = Mth.clamp(this.displayPos, 0, length);
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    public void setPlaceholder(String placeholder, int color) {
        this.placeholder = placeholder;
        this.placeholderColor = color;
    }
}
