package com.teamresourceful.resourcefulconfig.common.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.apache.commons.lang3.NotImplementedException;

import java.nio.file.Path;

public final class ModUtils {

    @ExpectPlatform
    public static Path getConfigPath() {
        throw new NotImplementedException("Not implemented yet");
    }
}
