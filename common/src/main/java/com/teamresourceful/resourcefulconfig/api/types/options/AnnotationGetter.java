package com.teamresourceful.resourcefulconfig.api.types.options;

import java.lang.annotation.Annotation;

public interface AnnotationGetter {

    <T extends Annotation> T get(Class<T> annotationClass);
}
