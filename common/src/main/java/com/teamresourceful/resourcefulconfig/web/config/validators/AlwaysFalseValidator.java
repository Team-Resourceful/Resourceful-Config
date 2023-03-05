package com.teamresourceful.resourcefulconfig.web.config.validators;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;

public final class AlwaysFalseValidator implements Validator {

    public static final AlwaysFalseValidator INSTANCE = new AlwaysFalseValidator();
    public static final Codec<AlwaysFalseValidator> CODEC = Codec.unit(INSTANCE);

    private AlwaysFalseValidator() {}

    @Override
    public boolean test(UserJwtPayload userJwtPayload) {
        return false;
    }

    @Override
    public String id() {
        return "false";
    }
}
