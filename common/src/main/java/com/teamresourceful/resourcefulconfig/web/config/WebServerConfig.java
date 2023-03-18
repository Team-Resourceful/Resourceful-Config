package com.teamresourceful.resourcefulconfig.web.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefulconfig.web.config.validators.IfValidator;
import com.teamresourceful.resourcefulconfig.web.config.validators.PasswordValidator;
import com.teamresourceful.resourcefulconfig.web.config.validators.Validator;
import com.teamresourceful.resourcefulconfig.web.config.validators.Validators;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public record WebServerConfig(boolean enabled, int port, Optional<String> configSite, Validator validator) {

    public static final Codec<WebServerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("enabled").forGetter(WebServerConfig::enabled),
            Codec.intRange(0, 65535).fieldOf("port").orElse(7903).forGetter(WebServerConfig::port),
            Codec.STRING.optionalFieldOf("config_site").forGetter(WebServerConfig::configSite),
            Validators.CODEC.fieldOf("validator").forGetter(WebServerConfig::validator)
    ).apply(instance, WebServerConfig::new));

    public static final WebServerConfig DEFAULT = new WebServerConfig(false, 7903, Optional.empty(), new IfValidator(Set.of(), new PasswordValidator(UUID.randomUUID().toString()), Optional.empty()));

    public boolean valid(UserJwtPayload info) {
        return enabled() && validator.test(info);
    }

    public String getSite() {
        return configSite.orElse("https://config.teamresourceful.com");
    }
}