package com.teamresourceful.resourcefulconfig.client.components.options.text.multiline;

import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.text.utils.TextBoxStringUtils;
import com.teamresourceful.resourcefulconfig.client.utils.ListenableState;
import com.teamresourceful.resourcefulconfig.client.utils.State;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class MultilineTextBox extends BaseWidget {

    private final MultilineTextInput state;
    private static final int WIDTH = 80;

    private Font font = Minecraft.getInstance().font;

    private double scroll = - 1;
    private int lastHeight;
    private boolean scrollbarHovered = false;

    public MultilineTextBox(State<String> state) {
        super(WIDTH, 16);
        this.state = new MultilineTextInput(state instanceof ListenableState<String> it ? it : new ListenableState<>(state));
    }

    public MultilineTextBox withFont(Font font) {
        this.font = font;
        return this;
    }

    protected void renderText(GuiGraphics graphics, int x, int y, int width) {
        var cursor = this.state.cursor();
        var selection = this.state.selection();
        var lines = this.state.lines(this.width - 8);

        for (var line : lines) {
            var text = this.state.value().substring(line.start(), line.end());

            graphics.drawString(this.font, TextBoxStringUtils.format(text), x, y, -1);

            if (this.state.hasSelection()) {
                if (line.contains(selection.end()) || line.contains(selection.start())) {
                    var startIndex = Math.max(selection.start() - line.start(), 0);
                    var endIndex = selection.end() - line.start();
                    var startX = TextBoxStringUtils.width(this.font, text.substring(0, startIndex)) + x;
                    var endX = endIndex > text.length() ? x + width : startX + TextBoxStringUtils.width(this.font, text.substring(startIndex, endIndex));
                    graphics.fill(startX, y, endX, y + this.font.lineHeight, 0x80AAAAAA);
                } else if (selection.contains(line.start()) && selection.contains(line.end())) {
                    graphics.fill(x, y, x + width, y + this.font.lineHeight, 0x80AAAAAA);
                }
            }

            if (line.contains(cursor)) {
                var first = text.substring(0, cursor - line.start());
                var cursorX = TextBoxStringUtils.width(this.font, first) + x;
                graphics.fill(cursorX, y, cursorX + 1, y + this.font.lineHeight, System.currentTimeMillis() % 1000 < 500 ? 0xFFFFFFFF : 0x00000000);
            }

            y += this.font.lineHeight;
        }

        this.lastHeight = lines.size() * this.font.lineHeight;

        if (this.scroll == -1) {
            this.setScroll(this.lastHeight - (this.height - 8));
        }
    }

    protected void setScroll(double scroll) {
        this.scroll = Mth.clamp(scroll, 0, Math.max(0, this.lastHeight - (this.height - 8)));
    }

    protected void changeScroll(double delta) {
        this.setScroll(this.scroll + delta);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!this.isVisible()) return;

        var texture = ModSprites.BUTTON;

        graphics.blitSprite(RenderType::guiOpaqueTexturedBackground, texture, getX(), getY(), getWidth(), getHeight());

        boolean renderScrollbar = this.lastHeight > this.height - 8;

        graphics.enableScissor(getX() + 2, getY() + 3, getX() + getWidth() - 4, getY() + getHeight() - 2);
        this.renderText(graphics, getX() + 6 - (renderScrollbar ? 2 : 0), (int) (getY() + 4 - scroll), getWidth() - 12);
        graphics.disableScissor();

        this.scrollbarHovered = false;
        if (renderScrollbar) {
            int minX = getX() + getWidth() - 5;
            int maxX = getX() + getWidth() - 3;
            graphics.fill(
                    minX, getY() + 4,
                    maxX, getY() + getHeight() - 4,
                    0xFF121314
            );

            var scrollBarHeight = (int) ((this.height - 8) / (double) this.lastHeight * (this.height - 8));
            var scrollBarY = (int) ((this.scroll / (double) this.lastHeight) * (this.height - 8));
            this.scrollbarHovered = mouseX >= minX - 2 && mouseX < maxX + 2;

            graphics.fill(
                    minX, getY() + 4 + scrollBarY,
                    maxX, getY() + 4 + scrollBarY + scrollBarHeight,
                    this.scrollbarHovered ? 0xFF404040 : 0xFF303030
            );
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!this.isVisible()) return false;

        this.changeScroll(scrollY * -10);
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isVisible()) return false;

        if (this.lastHeight > this.height - 8 && mouseX > getX() + getWidth() - 7) {
            this.setScroll((mouseY - getY() - 4) / (this.height - 8) * this.lastHeight);
            return true;
        }

        return this.state.onMouseClick(mouseX - getX() - 4, mouseY - getY() - 4 + scroll, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!this.isVisible()) return false;

        if (this.lastHeight > this.height - 8 && mouseX > getX() + getWidth() - 7) {
            return this.mouseClicked(mouseX, mouseY, button);
        }

        return this.state.onMouseDrag(mouseX - getX() - 4, mouseY - getY() - 4 + scroll, button);
    }

    @Override
    public boolean keyPressed(int key, int scan, int modifier) {
        if (!this.isVisible()) return false;

        var result = this.state.onKeyPress(key);
        this.lastHeight = this.state.lines(this.width - 8).size() * this.font.lineHeight;
        this.setScroll(this.state.getLineAtCursor() * this.font.lineHeight);
        return result;
    }

    @Override
    public boolean charTyped(char character, int modifier) {
        if (!this.isVisible()) return false;

        this.setScroll(this.state.getLineAtCursor() * this.font.lineHeight);
        return this.state.onCharTyped(character);
    }

    public boolean isVisible() {
        return this.visible;
    }
}
