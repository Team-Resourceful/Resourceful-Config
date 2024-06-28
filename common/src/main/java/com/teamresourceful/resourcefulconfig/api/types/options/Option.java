package com.teamresourceful.resourcefulconfig.api.types.options;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Option<T extends Annotation, D> {

    private static final List<Option<?, ?>> VALUES = new ArrayList<>();

    public static final Option<ConfigOption.Color, ConfigOption.Color> COLOR = Option.of(
            ConfigOption.Color.class,
            type -> type == int.class || type == Integer.class
    );
    public static final Option<ConfigOption.Slider, ConfigOption.Slider> SLIDER = Option.of(
            ConfigOption.Slider.class
    );
    public static final Option<ConfigOption.Multiline, ConfigOption.Multiline> MULTILINE = Option.of(
            ConfigOption.Multiline.class,
            type -> type == String.class
    );
    public static final Option<ConfigOption.Hidden, ConfigOption.Hidden> HIDDEN = Option.of(
            ConfigOption.Hidden.class
    );
    public static final Option<ConfigOption.Draggable, Enum<?>[]> DRAGGABLE = Option.of(
            ConfigOption.Draggable.class,
            type -> type.isArray() && type.getComponentType().isEnum(),
            (type, data) -> {
                Class<?> componentType = type.getComponentType();
                Map<String, Enum<?>> ids = new HashMap<>();
                for (Enum<?> e : (Enum<?>[]) componentType.getEnumConstants()) {
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
    public static final Option<ConfigOption.Range, ConfigOption.Range> RANGE = Option.of(
            ConfigOption.Range.class
    );
    public static final Option<ConfigOption.Select, Component> SELECT = Option.of(
            ConfigOption.Select.class,
            type -> type.isArray() && type.getComponentType().isEnum(),
            (type, data) -> Component.translatable(data.value())
    );
    public static final Option<ConfigOption.Separator, ConfigOption.Separator> SEPARATOR = Option.of(
            ConfigOption.Separator.class
    );
    public static final Option<ConfigOption.Regex, Pattern> REGEX = Option.of(
            ConfigOption.Regex.class,
            type -> type == String.class,
            (type, data) -> Pattern.compile(data.value())
    );
    public static final Option<ConfigOption.Keybind, ConfigOption.Keybind> KEYBIND = Option.of(
            ConfigOption.Keybind.class,
            type -> type == int.class || type == Integer.class
    );
    public static final Option<ConfigOption.SearchTerm, List<String>> SEARCH_TERM = Option.of(
            ConfigOption.SearchTerm.class,
            type -> true,
            (type, data) -> List.of(data.value())
    );


    private final Class<T> annotation;
    private final Predicate<Class<?>> isAllowed;
    private final BiFunction<Class<?>, T, D> mapper;

    private Option(Class<T> annotation, Predicate<Class<?>> isAllowed, BiFunction<Class<?>, T, D> mapper) {
        this.annotation = annotation;
        this.isAllowed = isAllowed;
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    private Object getData(Annotation annotation, Class<?> clazz) {
        return this.mapper.apply(clazz, (T) annotation);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "22.0")
    public static Map<Option<?, ?>, Object> fromField(Field field, Class<?> type) {
        return gatherOptions(field::getAnnotation, type);
    }

    public static Map<Option<?, ?>, Object> gatherOptions(AnnotationGetter getter, Class<?> type) {
        Map<Option<?, ?>, Object> values = new IdentityHashMap<>();
        for (Option<?, ?> value : VALUES) {
            Annotation annotation = getter.get(value.annotation);
            if (annotation != null && value.isAllowed.test(type)) {
                values.put(value, value.getData(annotation, type));
            }
        }
        return values;
    }

    private static <T extends Annotation> Option<T, T> of(Class<T> annotation) {
        Option<T, T> option = new Option<>(annotation, type -> true, (c, a) -> a);
        VALUES.add(option);
        return option;
    }

    public static <T extends Annotation> Option<T, T> of(Class<T> annotation, Predicate<Class<?>> isAllowed) {
        Option<T, T> option = new Option<>(annotation, isAllowed, (c, a) -> a);
        VALUES.add(option);
        return option;
    }

    private static <T extends Annotation, D> Option<T, D> of(Class<T> annotation, Predicate<Class<?>> isAllowed, BiFunction<Class<?>, T, D> mapper) {
        Option<T, D> option = new Option<>(annotation, isAllowed, mapper);
        VALUES.add(option);
        return option;
    }
}
