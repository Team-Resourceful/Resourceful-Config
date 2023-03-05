package com.teamresourceful.resourcefulconfig.web.config.validators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public record DerivedPasswordValidator(SecretKeySpec spec) implements Validator {

    private static final Codec<SecretKeySpec> SPEC_CODEC = Codec.STRING.xmap(
            s -> new SecretKeySpec(s.getBytes(StandardCharsets.UTF_8), "HmacSHA256"),
            s -> new String(s.getEncoded())
    );

    public static final Codec<DerivedPasswordValidator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SPEC_CODEC.fieldOf("secret").forGetter(DerivedPasswordValidator::spec)
    ).apply(instance, DerivedPasswordValidator::new));

    @Override
    public boolean test(UserJwtPayload userJwtPayload) {
        final String password = userJwtPayload.password();
        final String uuid = userJwtPayload.uuid().toString();

        try {
            final var mac = Mac.getInstance("HmacSHA256");
            mac.init(spec);
            final var hash = mac.doFinal(uuid.getBytes(StandardCharsets.UTF_8));
            return Objects.equals(Base64.getEncoder().encodeToString(hash), password);
        }catch (Exception ignored) {}
        return false;
    }

    @Override
    public String id() {
        return "derived";
    }
}
