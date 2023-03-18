package com.teamresourceful.resourcefulconfig.web.config.validators;

import com.mojang.serialization.Codec;

public class Validators {

    public static final Codec<Validator> CODEC = Codec.STRING.dispatch(Validator::id, Validators::get);

    private static Codec<? extends Validator> get(String id) {
        return switch (id) {
            case "password" -> PasswordValidator.CODEC;
            case "derived" -> DerivedPasswordValidator.CODEC;
            case "hashed" -> HashedPasswordValidator.CODEC;
            case "or" -> OrValidator.CODEC;
            case "if" -> IfValidator.CODEC;
            case "false" -> AlwaysFalseValidator.CODEC;
            default -> throw new IllegalArgumentException("Unknown validator type: " + id);
        };
    }
}