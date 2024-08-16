package com.teamresourceful.resourcefulconfig.client.components.options.types.color;

import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class AlphaSelector extends BaseWidget {

    private final HsbState state;

    public AlphaSelector(int width, int height, HsbState state) {
        super(width, height);
        this.state = state;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        HsbColor color = state.get();

        int size = Math.min(getWidth(), getHeight()) / 2;
        for (int x = 0; x < Math.ceilDiv(getWidth(), size); x++) {
            for (int y = 0; y < Math.ceilDiv(getHeight(), size); y++) {
                graphics.fill(
                        getX() + x * size, getY() + y * size,
                        getX() + (x + 1) * size, getY() + (y + 1) * size,
                        (y + x) % 2 == 0 ? 0xFFDEDEDE : 0xFFFFFFFF
                );
            }
        }

        for (int i = 0; i < getWidth(); i++) {
            int alpha = Math.round(((float)i / (float) getWidth()) * 255f);
            graphics.fill(
                    getX() + i, getY(),
                    getX() + i + 1, getY() + getHeight(),
                    Mth.hsvToArgb(color.hue(), color.saturation(), color.brightness(), alpha)
            );
        }

        int posX = Mth.floor((this.state.get().alpha() / 255f) * this.getWidth());
        graphics.renderOutline(getX() + posX - 1, getY() - 1, 3, getHeight() + 2, 0xFF000000);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        if (!isMouseOver(mouseX, mouseY)) return false;
        float alpha = Mth.clamp((float) (mouseX - getX()) / (float) getWidth(), 0f, 1f);
        this.state.set(this.state.get().withAlpha(Mth.ceil(alpha * 255f)));
        return true;
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        return mouseClicked(d, e, i);
    }
}
