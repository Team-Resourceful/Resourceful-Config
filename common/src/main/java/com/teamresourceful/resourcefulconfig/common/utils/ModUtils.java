package com.teamresourceful.resourcefulconfig.common.utils;

import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.lang.reflect.Array;
import java.nio.file.Path;

@ApiStatus.Internal
public final class ModUtils {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ModLoaderService SERVICE = ModLoaderService.create();

    public static Path getConfigPath() {
        return SERVICE.getConfigPath();
    }

    public static boolean isDev() {
        return SERVICE.isDev();
    }

    public static boolean isModLoaded(String modid) {
        return SERVICE.isModLoaded(modid);
    }

    public static void log(String message) {
        LOGGER.info("[ResourcefulConfig] {}", message);
    }

    public static void log(String message, Throwable throwable) {
        LOGGER.info("[ResourcefulConfig] {}", message, throwable);
    }

    public static void debug(String message) {
        LOGGER.debug("[ResourcefulConfig] {}", message);
    }

    public static boolean isEnum(Class<?> clazz) {
        return clazz.isEnum() || clazz.getSuperclass().isEnum();
    }

    public static Enum<?>[] getEnumConstants(Class<?> clazz) {
        if (clazz.isEnum()) return (Enum<?>[]) clazz.getEnumConstants();
        if (clazz.getSuperclass().isEnum()) return (Enum<?>[]) clazz.getSuperclass().getEnumConstants();
        return new Enum<?>[0];
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] castArray(Object[] array, Class<T> clazz) {
        T[] newArray = (T[]) Array.newInstance(clazz, array.length);
        for (int i = 0; i < array.length; i++) {
            newArray[i] = clazz.cast(array[i]);
        }
        return newArray;
    }
}
