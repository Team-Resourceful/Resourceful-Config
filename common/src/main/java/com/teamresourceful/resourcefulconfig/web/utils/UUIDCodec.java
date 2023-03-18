package com.teamresourceful.resourcefulconfig.web.utils;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public final class UUIDCodec {

    public static Codec<UUID> AUTHLIB_CODEC = Codec.either(UUIDUtil.CODEC, Codec.STRING.comapFlatMap((string) -> {
        try {
            return DataResult.success(UUIDTypeAdapter.fromString(string), Lifecycle.stable());
        } catch (IllegalArgumentException var2) {
            return DataResult.error("Invalid UUID " + string + ": " + var2.getMessage());
        }
    }, UUIDTypeAdapter::fromUUID)).xmap((either) -> either.map((uUID) -> uUID, (uUID) -> uUID), Either::right);
}
