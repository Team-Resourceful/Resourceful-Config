package com.teamresourceful.resourcefulconfig.api.types.options;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import com.teamresourceful.resourcefulconfig.common.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Option<T extends Annotation, D> {

    private static final List<Option<?, ?>> REGISTERED = new ArrayList<>();

    public static final Option<ConfigOption.Color, ConfigOption.Color> COLOR = Option.create(
            ConfigOption.Color.class,
            type -> type == int.class || type == Integer.class
    );
    public static final Option<ConfigOption.Slider, ConfigOption.Slider> SLIDER = Option.create(
            ConfigOption.Slider.class
    );
    public static final Option<ConfigOption.Multiline, ConfigOption.Multiline> MULTILINE = Option.create(
            ConfigOption.Multiline.class,
            type -> type == String.class
    );
    public static final Option<ConfigOption.Hidden, ConfigOption.Hidden> HIDDEN = Option.create(
            ConfigOption.Hidden.class
    );
    public static final Option<ConfigOption.Draggable, Enum<?>[]> DRAGGABLE = Option.create(
            ConfigOption.Draggable.class,
            type -> type.isArray() && type.getComponentType().isEnum(),
            (type, data) -> {
                Class<?> componentType = type.getComponentType();
                Map<String, Enum<?>> ids = new HashMap<>();
                for (Enum<?> e : ModUtils.getEnumConstants(componentType)) {
                    ids.put(e.name(), e);
                }
                List<Enum<?>> duplicates = new ArrayList<>();
                for (String s : data.value()) {
                    if (!ids.containsKey(s)) throw new IllegalArgumentException("Invalid enum value: " + s);
                    duplicates.add(ids.get(s));
                }
                return duplicates.toArray((Enum<?>[]) Array.newInstance(componentType, duplicates.size()));
            }
    );
    public static final Option<ConfigOption.Range, ConfigOption.Range> RANGE = Option.create(
            ConfigOption.Range.class
    );
    public static final Option<ConfigOption.Select, Component> SELECT = Option.create(
            ConfigOption.Select.class,
            type -> type.isArray() && type.getComponentType().isEnum(),
            (type, data) -> Component.translatable(data.value())
    );
    public static final Option<ConfigOption.Separator, ConfigOption.Separator> SEPARATOR = Option.create(
            ConfigOption.Separator.class
    );
    public static final Option<ConfigOption.Regex, Pattern> REGEX = Option.create(
            ConfigOption.Regex.class,
            type -> type == String.class,
            (type, data) -> Pattern.compile(data.value())
    );
    public static final Option<ConfigOption.Keybind, ConfigOption.Keybind> KEYBIND = Option.create(
            ConfigOption.Keybind.class,
            type -> type == int.class || type == Integer.class
    );
    public static final Option<ConfigOption.SearchTerm, List<String>> SEARCH_TERM = Option.create(
            ConfigOption.SearchTerm.class,
            type -> true,
            (type, data) -> List.of(data.value())
    );
    public static final Option<ConfigOption.Renderer, ResourceLocation> RENDERER = Option.create(
            ConfigOption.Renderer.class,
            type -> true,
            (type, data) -> {
                String value = data.value();
                if (value.isEmpty()) return null;
                return ResourceLocation.tryParse(value);
            }
    );

    private final @Nullable Class<T> annotation;
    private final @Nullable Predicate<Class<?>> isAllowed;
    private final @Nullable BiFunction<Class<?>, T, D> mapper;

    private Option(@Nullable Class<T> annotation, @Nullable Predicate<Class<?>> isAllowed, @Nullable BiFunction<Class<?>, T, D> mapper) {
        this.annotation = annotation;
        this.isAllowed = isAllowed;
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    private Object getData(Annotation annotation, Class<?> clazz) {
        Objects.requireNonNull(this.mapper, "Mapper function is not defined for this option.");
        return this.mapper.apply(clazz, (T) annotation);
    }

    public static Map<Option<?, ?>, Object> gatherOptions(AnnotationGetter getter, Class<?> type) {
        Map<Option<?, ?>, Object> values = new IdentityHashMap<>();
        for (Option<?, ?> value : REGISTERED) {
            if (value.annotation == null || value.isAllowed == null || value.mapper == null) continue;

            Annotation annotation = getter.get(value.annotation);
            if (annotation != null && value.isAllowed.test(type)) {
                values.put(value, value.getData(annotation, type));
            }
        }
        return values;
    }

    private static <T extends Annotation> Option<T, T> create(Class<T> annotation) {
        Option<T, T> option = new Option<>(annotation, type -> true, (c, a) -> a);
        REGISTERED.add(option);
        return option;
    }

    private static <T extends Annotation> Option<T, T> create(Class<T> annotation, Predicate<Class<?>> isAllowed) {
        Option<T, T> option = new Option<>(annotation, isAllowed, (c, a) -> a);
        REGISTERED.add(option);
        return option;
    }

    private static <T extends Annotation, D> Option<T, D> create(Class<T> annotation, Predicate<Class<?>> isAllowed, BiFunction<Class<?>, T, D> mapper) {
        Option<T, D> option = new Option<>(annotation, isAllowed, mapper);
        REGISTERED.add(option);
        return option;
    }

    public static <D> Option<?, D> create() {
        return new Option<>(null, null, null);
    }

    /**
     * @deprecated This method was removed in 1.21.11.
     * If a custom option is required is must not be registered and must be created via {@link #create()}.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.21.11")
    public static <T extends Annotation> Option<T, T> of(Class<T> annotation, Predicate<Class<?>> isAllowed) {
        return create(annotation, isAllowed);
    }
}
