package com.teamresourceful.resourcefulconfig.web.config.validators;

import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;

import java.util.function.Predicate;

public interface Validator extends Predicate<UserJwtPayload> {

    String id();
}
