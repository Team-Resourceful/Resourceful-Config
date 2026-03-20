package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class NumberOptionWidget<T extends Number> extends EditBox implements ResetableWidget {

    public static final Pattern INTEGER_FILTER = Pattern.compile("-?[0-9]*");
    public static final Pattern DECIMAL_FILTER = Pattern.compile("-?[0-9]*(\\.[0-9]*)?");

    private static final int FOCUSED_EXTRA_WIDTH = 40;
    private static final int WIDTH = 80;
    private static final int FOCUSED_WIDTH = WIDTH + FOCUSED_EXTRA_WIDTH;

    private final Supplier<T> getter;
    private final Function<T, Boolean> setter;
    private final Function<String, T> parser;
    private final Pattern filter;

    public NumberOptionWidget(Supplier<T> getter, Function<T, Boolean> setter, Function<String, T> parser, Pattern filter) {
        super(Minecraft.getInstance().font, WIDTH, 16, CommonComponents.EMPTY);
        setMaxLength(Short.MAX_VALUE);
        setBordered(false);
        setCanLoseFocus(true);

        this.getter = getter;
        this.setter = setter;
        this.parser = parser;
        this.filter = filter;

        DecimalFormat format = new DecimalFormat();
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(340);

        setValue(format.format(getter.get()));
        setResponder();
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        updateIfFocused();

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.BUTTON, getX(), getY(), this.width, this.height);

        graphics.enableScissor(getX() + 4, getY() + 4, getX() + this.width - 4, getY() + this.height - 4);

        var pose = graphics.pose();
        pose.pushMatrix();
        pose.translate(4, 4);
        super.extractWidgetRenderState(graphics, mouseX, mouseY, partialTicks);
        pose.popMatrix();

        graphics.disableScissor();
    }

    public void updateIfFocused() {
        if (this.width != FOCUSED_WIDTH && isFocused()) {
            setWidth(FOCUSED_WIDTH);
            setX(getX() - FOCUSED_EXTRA_WIDTH);
        } else if (this.width != WIDTH && !isFocused()) {
            setWidth(WIDTH);
            setX(getX() + FOCUSED_EXTRA_WIDTH);
        }
    }

    @Override
    public void reset() {
        setResponder(_ -> {});
        setValue(this.getter.get().toString());
        setResponder();
    }

    public void setResponder() {
        setResponder(s -> {
            try {
                if (s.isBlank()) throw new NumberFormatException();
                if (!this.filter.matcher(s).matches()) throw new NumberFormatException();

                T value = this.parser.apply(s);
                if (this.setter.apply(value)) {
                    this.setTextColor(0xFFE0E0E0);
                } else {
                    this.setTextColor(0xFFFF0000);
                }
            } catch (NumberFormatException ignored) {
                this.setTextColor(0xFFFF0000);
            }
        });
    }
}
