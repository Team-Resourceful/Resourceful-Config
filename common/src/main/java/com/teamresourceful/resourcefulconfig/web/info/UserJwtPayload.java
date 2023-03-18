package com.teamresourceful.resourcefulconfig.web.info;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefulconfig.web.utils.UUIDCodec;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;

@ApiStatus.Internal
public record UserJwtPayload(
        UUID uuid,
        String name,
        String password,
        long expiration
) {

    public static final Codec<UserJwtPayload> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDCodec.AUTHLIB_CODEC.fieldOf("sub").forGetter(UserJwtPayload::uuid),
            Codec.STRING.fieldOf("name").forGetter(UserJwtPayload::name),
            Codec.STRING.fieldOf("svr_pw").forGetter(UserJwtPayload::password),
            Codec.LONG.fieldOf("exp").forGetter(UserJwtPayload::expiration)
    ).apply(instance, UserJwtPayload::new));

    public static UserJwtPayload fromJson(JsonElement json) {
        return UserJwtPayload.CODEC.parse(JsonOps.INSTANCE, json)
                .getOrThrow(false, s -> { });
    }
}