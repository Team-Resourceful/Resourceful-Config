package com.teamresourceful.resourcefulconfig.common.info;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class ConfigInfoLoader {

    public static ResourcefulConfigInfo load(Class<?> clazz, String id) {
        ConfigInfo.Provider provider = clazz.getAnnotation(ConfigInfo.Provider.class);
        if (provider != null) {
            Class<?> providerClass = provider.value();
            assertValidClass(providerClass);
            try {
                Constructor<?> constructor = providerClass.getConstructor();
                assertValidConstructor(constructor);
                return (ResourcefulConfigInfo) constructor.newInstance();
            } catch (Exception ignored1) {
                try {
                    Constructor<?> constructor = providerClass.getConstructor(String.class);
                    assertValidConstructor(constructor);
                    return (ResourcefulConfigInfo) constructor.newInstance(id);
                } catch (Exception ignored2) {}
            }
            return null;
        }
        return null;
    }

    private static void assertValidConstructor(Constructor<?> constructor) {
        if (!Modifier.isPublic(constructor.getModifiers())) {
            throw new IllegalArgumentException("Info provider constructor must be public!");
        }
        if (constructor.getParameterCount() == 0) return;
        if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0] == String.class) return;
        throw new IllegalArgumentException("Info provider constructor must have no parameters or a single String parameter!");
    }

    private static void assertValidClass(Class<?> clazz) {
        if (!Modifier.isPublic(clazz.getModifiers())) {
            throw new IllegalArgumentException("Info provider class must be public!");
        }
        if (clazz.getEnclosingClass() != null && !Modifier.isStatic(clazz.getModifiers())) {
            throw new IllegalArgumentException("Info provider class must be static!");
        }
        if (clazz.isEnum()) {
            throw new IllegalArgumentException("Info provider class cannot be an enum!");
        }
        if (clazz.isInterface()) {
            throw new IllegalArgumentException("Info provider class cannot be an interface!");
        }
        if (clazz.isAnnotation()) {
            throw new IllegalArgumentException("Info provider class cannot be an annotation!");
        }
        if (clazz.isRecord()) {
            throw new IllegalArgumentException("Info provider class cannot be a record!");
        }
        if (!ResourcefulConfigInfo.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Info provider class must implement ResourcefulConfigInfo!");
        }
    }
}
