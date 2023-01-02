package com.teamresourceful.resourcefulconfig.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.teamresourceful.resourcefulconfig.common.annotations.ConfigSeparator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SeparatorValueWidget extends ValueWidget {

    private final int left;
    private final int right;

    private final Component text;
    private final Component tooltip;

    public SeparatorValueWidget(int left, int right, ConfigSeparator separator) {
        this.left = left;
        this.right = right;
        this.text = Component.translatable(separator.translation());
        this.tooltip = separator.tooltip().isEmpty() ? CommonComponents.EMPTY : Component.translatable(separator.tooltip());
    }

    @Override
    public void render(@NotNull PoseStack stack, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
        Font font = Minecraft.getInstance().font;
        font.draw(stack, this.text, left + 10, (float) (j + 3), 5592405);
        drawLine(this.left + 5, this.right - 5, (j + 5 + font.lineHeight), (j + 5 + font.lineHeight), 5592405);
        super.render(stack, i, j, k, l, m, n, o, bl, f);
    }

    public static void drawLine(int x1, int x2, int y1, int y2, int colour) {
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(x1, y1, 0).color((colour >> 16 & 255) / 255.0f, (float) (colour >> 8 & 255) / 255.0f, (float) (colour & 255) / 255.0f, 1.0f).endVertex();
        bufferBuilder.vertex(x2, y2, 0).color((colour >> 16 & 255) / 255.0f, (float) (colour >> 8 & 255) / 255.0f, (float) (colour & 255) / 255.0f, 1.0f).endVertex();
        tessellator.end();
    }

    @Override
    public List<FormattedCharSequence> getTooltip() {
        return Minecraft.getInstance().font.split(this.tooltip, 200);
    }
}
