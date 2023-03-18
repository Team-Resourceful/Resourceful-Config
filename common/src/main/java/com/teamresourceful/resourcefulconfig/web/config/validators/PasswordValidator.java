package com.teamresourceful.resourcefulconfig.web.config.validators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;

public record PasswordValidator(String password) implements Validator {

    public static final Codec<PasswordValidator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("password").forGetter(PasswordValidator::password)
    ).apply(instance, PasswordValidator::new));

    @Override
    public boolean test(UserJwtPayload userJwtPayload) {
        return userJwtPayload.password().equals(password);
    }

    @Override
    public String id() {
        return "password";
    }
}