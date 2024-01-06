package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;

public class BooleanOptionWidget extends BaseWidget {

    private static final int WIDTH = 80;
    private static final int HALF_WIDTH = WIDTH / 2;
    private static final int SWITCH_WIDTH = WIDTH / 2 - 2;
    private static final int HALF_SWITCH_WIDTH = SWITCH_WIDTH / 2;

    private final BooleanSupplier getter;
    private final BooleanConsumer setter;

    public BooleanOptionWidget(BooleanSupplier getter, BooleanConsumer setter) {
        super(WIDTH, 16);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        boolean value = this.getter.getAsBoolean();
        int offX = getX() + 1;
        int onX = getX() + 1 + HALF_WIDTH;
        int color = value ? UIConstants.TEXT_PARAGRAPH : UIConstants.TEXT_TITLE;

        graphics.blitSprite(ModSprites.BUTTON, getX(), getY(), this.width, this.height);
        graphics.blitSprite(ModSprites.ofSwitch(value), value ? onX : offX, getY() + 1, SWITCH_WIDTH, this.height - 2);
        drawCenteredString(graphics, this.font, CommonComponents.OPTION_OFF, offX + HALF_SWITCH_WIDTH, getY() + 4, color, value && isHovered());
        drawCenteredString(graphics, this.font, CommonComponents.OPTION_ON, onX + HALF_SWITCH_WIDTH, getY() + 4, color, !value && isHovered());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setter.accept(!this.getter.getAsBoolean());
    }

    public static void drawCenteredString(GuiGraphics graphics, Font font, Component component, int x, int y, int color, boolean shadowed) {
        int actualX = x - font.width(component) / 2;
        graphics.drawString(font, component, actualX, y, color, shadowed);
    }
}
