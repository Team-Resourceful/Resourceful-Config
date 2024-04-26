package com.teamresourceful.resourcefulconfig.web.config.validators;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public record HashedPasswordValidator(HashType type, String hash) implements Validator {

    public static final MapCodec<HashedPasswordValidator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HashType.CODEC.fieldOf("algorithm").forGetter(HashedPasswordValidator::type),
            Codec.STRING.fieldOf("input").forGetter(HashedPasswordValidator::hash)
    ).apply(instance, HashedPasswordValidator::new));

    @Override
    public boolean test(UserJwtPayload userJwtPayload) {
        final String password = userJwtPayload.password();
        final HashFunction function = switch (type) {
            case SHA1 -> Hashing.sha1();
            case SHA256 -> Hashing.sha256();
            case SHA512 -> Hashing.sha512();
            case MD5 -> Hashing.md5();
        };
        return function.hashString(password, StandardCharsets.UTF_8).toString().equals(hash);
    }

    @Override
    public String id() {
        return "hashed";
    }

    public enum HashType {
        SHA1,
        SHA256,
        SHA512,
        MD5;

        public static final Codec<HashType> CODEC = Codec.STRING.comapFlatMap(HashType::fromString, HashType::toString);

        public static DataResult<HashType> fromString(String string) {
            return switch (string.toLowerCase(Locale.ROOT)) {
                case "sha1" -> DataResult.success(SHA1);
                case "sha256" -> DataResult.success(SHA256);
                case "sha512" -> DataResult.success(SHA512);
                case "md5" -> DataResult.success(MD5);
                default -> DataResult.error(() -> "Unknown hash type: " + string);
            };
        }
    }
}
