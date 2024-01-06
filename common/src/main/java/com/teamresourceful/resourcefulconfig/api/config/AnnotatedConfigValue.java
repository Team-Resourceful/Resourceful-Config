package com.teamresourceful.resourcefulconfig.api.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

public interface AnnotatedConfigValue {

    AnnotatedElement element();

    default <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return element().getAnnotation(annotationClass);
    }

    default <T extends Annotation> Optional<T> getOptionalAnnotation(Class<T> annotationClass) {
        return Optional.ofNullable(getAnnotation(annotationClass));
    }

}
