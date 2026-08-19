package com.teamresourceful.resourcefulconfig.common.config;

import com.teamresourceful.resourcefulconfig.common.utils.ModUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public final class ParsingUtils {

    public static void forEach(Object value, Consumer<Object> consumer) {
        if (value.getClass().getComponentType().isPrimitive()) {
            // Primitive arrays like int[], byte[], etc, are different from Object arrays.
            // Directly casting primitive arrays int[] into Object[] will throw an exception; manual copying is required
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                consumer.accept(Array.get(value, i));
            }
        } else {
            for (Object o : (Object[]) value) {
                consumer.accept(o);
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    public static Enum<?> parseEnum(Class<?> clazz, String name) {
        try {
            Class<Enum<?>> enumClass;
            if (clazz.isEnum()) {
                enumClass = (Class<Enum<?>>) clazz;
            } else if (clazz.getSuperclass().isEnum()) {
                enumClass = (Class<Enum<?>>) clazz.getSuperclass();
            } else {
                return null;
            }

            List<Enum<?>> result = Arrays.stream(enumClass.getEnumConstants())
                    .filter(anEnum -> anEnum.name().equalsIgnoreCase(name))
                    .toList();

            if(result.isEmpty()) {
                return null;
            }

            if(result.size() > 1) {
                ModUtils.warn(String.format(
                        "Enum class '%s' contains multiple constants that match the name '%s' case-insensitive. Using the first one.",
                        enumClass.getSimpleName(),
                        name
                ));
            }

            return result.getFirst();
        } catch (Exception ignored) {
            return null;
        }
    }

    public static Object getField(Field field, Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
