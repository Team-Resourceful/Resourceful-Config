package com.teamresourceful.resourcefulconfig.client.components.options.types.color;

import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class HueSelector extends BaseWidget {

    private final HsbState state;

    public HueSelector(int width, int height, HsbState state) {
        super(width, height);
        this.state = state;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (int i = 0; i < getWidth(); i++) {
            graphics.fill(
                    getX() + i, getY(),
                    getX() + i + 1, getY() + getHeight(),
                    Mth.hsvToArgb((float)i / (float) getWidth(), 1f, 1f, 255)
            );
        }

        int posX = Mth.floor(this.state.get().hue() * this.getWidth());
        graphics.renderOutline(getX() + posX - 1, getY() - 1, 3, getHeight() + 2, 0xFF000000);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        if (!isMouseOver(mouseX, mouseY)) return false;
        float hue = Mth.clamp((float) (mouseX - getX()) / (float) getWidth(), 0f, 1f);
        this.state.set(this.state.get().withHue(hue));
        return true;
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        return mouseClicked(d, e, i);
    }
}
