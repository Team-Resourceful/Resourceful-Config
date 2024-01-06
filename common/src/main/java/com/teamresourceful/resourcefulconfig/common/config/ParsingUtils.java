package com.teamresourceful.resourcefulconfig.common.config;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Locale;
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Enum<?> parseEnum(Class<?> clazz, String name) {
        try {
            return Enum.valueOf((Class<Enum>) clazz, name.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
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
