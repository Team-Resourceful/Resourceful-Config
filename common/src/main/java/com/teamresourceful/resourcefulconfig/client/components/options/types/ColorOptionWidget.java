package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.api.types.info.Translatable;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.types.color.*;
import com.teamresourceful.resourcefulconfig.client.screens.base.CloseableScreen;
import com.teamresourceful.resourcefulconfig.client.screens.base.OverlayScreen;
import com.teamresourceful.resourcefulconfig.client.utils.State;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class ColorOptionWidget extends BaseWidget {

    private static final int SIZE = 16;
    private static final int PADDING = 4;
    private static final int SPACING = 2;

    private final int[] presets;
    private final boolean hasAlpha;
    private final IntSupplier getter;
    private final IntConsumer setter;

    public ColorOptionWidget(int[] presets, boolean hasAlpha, IntSupplier getter, IntConsumer setter) {
        super(SIZE, SIZE);
        this.presets = presets;
        this.hasAlpha = hasAlpha;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(RenderType::guiTextured, ModSprites.BUTTON, getX(), getY(), this.height, this.height);
        graphics.fill(getX() + 1, getY() + 1, getX() + this.height - 1, getY() + this.height - 1, this.getter.getAsInt());
    }

    @Override
    public void onClick(double d, double e) {
        Minecraft.getInstance().setScreen(new PresetsOverlay(this));
    }

    private static class PresetsOverlay extends OverlayScreen implements CloseableScreen {

        private final ColorOptionWidget widget;
        private final HsbState state;
        private final State<PresetType> type;

        private int x;
        private int y;
        private int width;
        private int height;

        protected PresetsOverlay(ColorOptionWidget widget) {
            super(Minecraft.getInstance().screen);
            this.widget = widget;
            this.state = new HsbState(HsbColor.fromRgb(widget.getter.getAsInt()), color -> widget.setter.accept(color.toRgba()));
            this.type = State.of(
                    widget.presets.length == 0 ?
                            RecentColorStorage.hasValues() ?
                                    PresetType.RECENTS
                                    : PresetType.MC_COLORS
                            : PresetType.DEFAULTS
            );
        }

        @Override
        protected void init() {
            GridLayout layout = new GridLayout().spacing(SPACING);

            layout.addChild(new SaturationBrightnessSelector(100, 50, this.state), 0, 0);
            layout.addChild(new HueSelector(100, 10, this.state), 1, 0);
            if (this.widget.hasAlpha) {
                layout.addChild(new AlphaSelector(100, 10, this.state), 2, 0);
            }

            LinearLayout presets = LinearLayout.horizontal().spacing(SPACING * 2);
            presets.addChild(new EyedropperButton(this.state));
            presets.addChild(DropdownWidget.of(
                    this.widget.presets.length == 0 ? PresetType.WITHOUT_DEFAULT : PresetType.VALUES,
                    this.type, this.type
            ));
            layout.addChild(presets, 3, 0);

            layout.addChild(new PresetsSelector(100, this.widget.presets, this.type, this.state, this.widget.hasAlpha), 4, 0);

            layout.arrangeElements();

            int windowHeight = this.getRectangle().height();

            int y = this.widget.getY() + this.widget.getHeight() + SPACING + layout.getHeight() + PADDING * 2 > windowHeight ?
                    this.widget.getY() - layout.getHeight() - PADDING * 2 - SPACING :
                    this.widget.getY() + this.widget.getHeight() + SPACING;

            layout.setPosition(this.widget.getX() + PADDING, y + PADDING);
            layout.visitWidgets(this::addRenderableWidget);

            this.x = this.widget.getX();
            this.y = y;
            this.width = layout.getWidth() + PADDING * 2;
            this.height = layout.getHeight() + PADDING * 2;
        }

        @Override
        public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.renderBackground(graphics, mouseX, mouseY, partialTicks);
            graphics.blitSprite(RenderType::guiTextured, ModSprites.ACCENT, this.x, this.y, this.width, this.height);
            graphics.blitSprite(RenderType::guiTextured, ModSprites.BUTTON, this.x + 1, this.y + 1, this.width - 2, this.height - 2);
        }

        @Override
        public void onClosed(@Nullable Screen replacement) {
            if (replacement instanceof OverlayScreen) return;
            if (!this.state.hasChanged()) return;
            RecentColorStorage.add(this.state.get());
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button != 0 || this.isMouseOver(mouseX, mouseY)) {
                return super.mouseClicked(mouseX, mouseY, button);
            }
            this.onClose();
            return false;
        }

    }

    public enum PresetType implements Translatable {
        RECENTS,
        DEFAULTS,
        MC_COLORS,
        ;

        private static final PresetType[] VALUES = values();
        private static final PresetType[] WITHOUT_DEFAULT = {RECENTS, MC_COLORS};

        @Override
        public String getTranslationKey() {
            return "rconfig.color.preset." + this.name().toLowerCase(Locale.ROOT);
        }
    }
}
