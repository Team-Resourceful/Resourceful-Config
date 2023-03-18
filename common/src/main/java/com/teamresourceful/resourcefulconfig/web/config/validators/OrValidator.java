package com.teamresourceful.resourcefulconfig.web.config.validators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;

import java.util.List;

public record OrValidator(List<Validator> validators) implements Validator {

    public static final Codec<OrValidator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Validators.CODEC.listOf().fieldOf("validators").forGetter(OrValidator::validators)
    ).apply(instance, OrValidator::new));

    @Override
    public boolean test(UserJwtPayload userJwtPayload) {
        if (validators.isEmpty()) return false;
        for (Validator validator : validators) {
            if (validator.test(userJwtPayload)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String id() {
        return "or";
    }
}