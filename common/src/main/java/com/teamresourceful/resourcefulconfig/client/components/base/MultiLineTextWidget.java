package com.teamresourceful.resourcefulconfig.client.components.base;

import java.util.OptionalInt;
import java.util.function.Consumer;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.SingleKeyCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A backport of features of MultiLineTextWidget from 1.21.6+ to allow for clickable text.
 */
public class MultiLineTextWidget extends AbstractStringWidget {
    private OptionalInt maxWidth = OptionalInt.empty();
    private OptionalInt maxRows = OptionalInt.empty();
    private final SingleKeyCache<MultiLineTextWidget.CacheKey, MultiLineLabel> cache;
    private boolean allowHoverComponents = false;
    @Nullable private Consumer<Style> componentClickHandler = null;

    public MultiLineTextWidget(Component component, Font font) {
        this(0, 0, component, font);
    }

    public MultiLineTextWidget(int i, int j, Component component, Font font) {
        super(i, j, 0, 0, component, font);
        this.cache = Util.singleKeyCache(
                cacheKey -> cacheKey.maxRows.isPresent()
                        ? MultiLineLabel.create(font, cacheKey.maxWidth, cacheKey.maxRows.getAsInt(), cacheKey.message)
                        : MultiLineLabel.create(font, cacheKey.message, cacheKey.maxWidth)
        );
        this.active = true;
    }

    public MultiLineTextWidget setColor(int i) {
        super.setColor(i);
        return this;
    }

    public MultiLineTextWidget setMaxWidth(int i) {
        this.maxWidth = OptionalInt.of(i);
        return this;
    }

    public MultiLineTextWidget setMaxRows(int i) {
        this.maxRows = OptionalInt.of(i);
        return this;
    }

    public MultiLineTextWidget configureStyleHandling(boolean bl, @Nullable Consumer<Style> consumer) {
        this.allowHoverComponents = bl;
        this.componentClickHandler = consumer;
        return this;
    }

    @Override
    public int getWidth() {
        return this.cache.getValue(this.getFreshCacheKey()).getWidth();
    }

    @Override
    public int getHeight() {
        return this.cache.getValue(this.getFreshCacheKey()).getLineCount() * 9;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        MultiLineLabel multiLineLabel = this.cache.getValue(this.getFreshCacheKey());
        int left = this.getX();
        int top = this.getY();
        int color = this.getColor();
        multiLineLabel.renderLeftAligned(graphics, left, top, 9, color);

        if (this.allowHoverComponents) {
            Style style = this.getComponentStyleAt(mouseX, mouseY);
            if (this.isHovered()) {
                graphics.renderComponentHoverEffect(this.getFont(), style, mouseX, mouseY);
            }
        }
    }

    @Nullable
    private Style getComponentStyleAt(double mouseX, double mouseY) {
        MultiLineLabel multiLineLabel = this.cache.getValue(this.getFreshCacheKey());
        int left = this.getX();
        int top = this.getY();
        return multiLineLabel.getStyleAtLeftAligned(left, top, 9, mouseX, mouseY);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.componentClickHandler != null) {
            Style style = this.getComponentStyleAt(mouseX, mouseY);
            if (style != null) {
                this.componentClickHandler.accept(style);
                return;
            }
        }

        super.onClick(mouseX, mouseY);
    }

    private MultiLineTextWidget.CacheKey getFreshCacheKey() {
        return new MultiLineTextWidget.CacheKey(this.getMessage(), this.maxWidth.orElse(Integer.MAX_VALUE), this.maxRows);
    }

    record CacheKey(Component message, int maxWidth, OptionalInt maxRows) {}
}
