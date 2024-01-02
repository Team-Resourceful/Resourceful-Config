package com.teamresourceful.resourcefulconfig.common.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

public interface ResourcefulConfigButton {

    String after();

    Method method();

    default <T extends Annotation> T getAnnotation(Class<T> annotation) {
        return method().getAnnotation(annotation);
    }

    default <T extends Annotation> Optional<T> getOptAnnotation(Class<T> annotation) {
        return Optional.ofNullable(getAnnotation(annotation));
    }

    default boolean invoke() {
        try {
            method().invoke(null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
