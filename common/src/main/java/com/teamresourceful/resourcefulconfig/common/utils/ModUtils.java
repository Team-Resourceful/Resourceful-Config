package com.teamresourceful.resourcefulconfig.common.utils;

import com.mojang.logging.LogUtils;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;

import java.nio.file.Path;

public final class ModUtils {

    private static final Logger LOGGER = LogUtils.getLogger();

    @ExpectPlatform
    public static Path getConfigPath() {
        throw new NotImplementedException("Not implemented yet");
    }

    public static void log(String message) {
        LOGGER.info("[ResourcefulConfig] " + message);
    }

    public static void debug(String message) {
        LOGGER.debug("[ResourcefulConfig] " + message);
    }
}
