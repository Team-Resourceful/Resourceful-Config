package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.api.types.options.Option;
import com.teamresourceful.resourcefulconfig.api.types.options.data.DraggableOptionEntry;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.misc.draggable.DraggableList;
import com.teamresourceful.resourcefulconfig.client.screens.base.ModalOverlay;
import com.teamresourceful.resourcefulconfig.common.utils.ModUtils;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DraggableListOptionWidget<T> extends BaseWidget {

    private static final int WIDTH = 80;
    private static final int SIZE = 12;
    private static final int SPACING = 4;
    private static final int PADDING = 2;

    private final Component title;
    private final List<DraggableOptionEntry<T>> options;
    private final Supplier<List<T>> getter;
    private final Consumer<List<T>> setter;
    private final IntIntPair range;

    public DraggableListOptionWidget(
            Component title,
            List<DraggableOptionEntry<T>> options,
            Supplier<List<T>> getter, Consumer<List<T>> setter,
            IntIntPair range
    ) {
        super(WIDTH, 16);

        this.title = title;
        this.options = options;
        this.getter = getter;
        this.setter = setter;
        this.range = range == null ? null : IntIntPair.of(range.firstInt(), range.secondInt());
    }

    public static DraggableListOptionWidget<Enum<?>> of(ResourcefulConfigValueEntry entry, EntryData data) {
        var range = data.getOption(Option.RANGE);
        var entries = new ArrayList<DraggableOptionEntry<Enum<?>>>();
        var duplicates = Set.of(data.getOrDefaultOption(Option.DRAGGABLE, new Enum<?>[0]));
        for (Enum<?> e : ModUtils.getEnumConstants(entry.objectType())) {
            entries.add(new DraggableOptionEntry<>(e, duplicates.contains(e)));
        }

        return new DraggableListOptionWidget<>(
                entry.options().title().toComponent(),
                entries,
                () -> Arrays.asList((Enum<?>[]) entry.getArray()),
                value -> {
                    Enum<?>[] array = (Enum<?>[]) Array.newInstance(entry.objectType(), value.size());
                    for (int i = 0; i < value.size(); i++) {
                        array[i] = value.get(i);
                    }
                    entry.setArray(array);
                },
                range == null ? null : IntIntPair.of((int) range.min(), (int) range.max())
        );
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.ofButton(this.isHovered()), getX(), getY(), getWidth(), getHeight());

        int contentWidth = font.width(UIConstants.EDIT) + SPACING + SIZE;

        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                ModSprites.EDIT,
                getX() + (getWidth() - contentWidth) / 2, getY() + PADDING,
                SIZE, SIZE
        );
        graphics.drawString(
                font, UIConstants.EDIT,
                getX() + (getWidth() - contentWidth) / 2 + SIZE + SPACING,
                getY() + (getHeight() - font.lineHeight) / 2 + 1,
                UIConstants.TEXT_TITLE
        );
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        new DraggableListOverlay<>(this).open();
    }

    private static class DraggableListOverlay<T> extends ModalOverlay {

        private final DraggableListOptionWidget<T> widget;

        protected DraggableListOverlay(DraggableListOptionWidget<T> widget) {
            super();
            this.widget = widget;
            this.title = UIConstants.EDIT_LIST;
        }

        protected List<T> getOptions() {
            Set<T> chosen = new HashSet<>(this.widget.getter.get());
            List<T> options = new ArrayList<>();

            for (var option : this.widget.options) {
                if (chosen.contains(option.value()) && !option.duplicatable()) continue;
                options.add(option.value());
            }
            return options;
        }

        @Override
        protected void init() {
            super.init();

            LinearLayout layout = LinearLayout.horizontal();


            final StringWidget title = layout.addChild(new StringWidget(
                    this.contentWidth - 84, 16,
                    Component.empty(), font
            ).alignLeft());

            Runnable updateTitle = () -> {
                if (this.widget.range != null) {
                    String count = "%d/%d".formatted(this.widget.getter.get().size(), this.widget.range.secondInt());
                    title.setMessage(Component.empty().append(this.widget.title).append(" ").append(count));
                } else {
                    title.setMessage(this.widget.title);
                }
            };

            updateTitle.run();

            var dropdown = layout.addChild(new DropdownWidget<>(
                    UIConstants.ADD_ITEM,
                    this.getOptions(),
                    () -> null,
                    (value) -> {
                        List<T> list = new ArrayList<>(this.widget.getter.get());
                        list.addFirst(value);
                        this.widget.setter.accept(list);
                    }
            ));
            dropdown.active = this.widget.range == null || this.widget.getter.get().size() < this.widget.range.secondInt();

            layout.setPosition(left + 4, top + 1);
            layout.arrangeElements();
            layout.visitWidgets(this::addRenderableWidget);

            int heading = layout.getHeight() + 4;

            DraggableList<T> list = addRenderableWidget(new DraggableList<>(left + 1, top + heading, contentWidth - 2, contentHeight - heading));
            list.addAll(this.widget.getter.get());
            list.setOnUpdate(value -> {
                this.widget.setter.accept(value);
                list.setCanDelete(this.widget.range == null || this.widget.getter.get().size() > this.widget.range.firstInt());
                dropdown.active = this.widget.range == null || this.widget.getter.get().size() < this.widget.range.secondInt();
                dropdown.setOptions(this.getOptions());
                updateTitle.run();
            });
            list.setCanDelete(this.widget.range == null || this.widget.getter.get().size() > this.widget.range.firstInt());
        }

        @Override
        public void renderBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.renderBackground(graphics, mouseX, mouseY, partialTicks);

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.BUTTON, left, top + 20, contentWidth, contentHeight - 20);
        }
    }
}
