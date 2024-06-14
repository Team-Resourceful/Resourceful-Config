package com.teamresourceful.resourcefulconfig.api.loader;

import com.google.common.base.Suppliers;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public interface ConfigParser {

    Supplier<List<ConfigParser>> PARSERS = Suppliers.memoize(() -> {
        List<ConfigParser> parsers = new ArrayList<>();
        ServiceLoader.load(ConfigParser.class).forEach(parsers::add);
        parsers.sort((o1, o2) -> Integer.compare(o2.priority(), o1.priority()));
        return parsers;
    });

    int priority();

    /**
     * Parses the given class into a ResourcefulConfig.
     * @param clazz The class to parse.
     * @return The parsed ResourcefulConfig.
     */
    @Nullable
    ResourcefulConfig parse(Class<?> clazz);

    static ResourcefulConfig tryParse(Class<?> clazz) {
        for (ConfigParser parser : PARSERS.get()) {
            ResourcefulConfig config = parser.parse(clazz);
            if (config != null) {
                return config;
            }
        }
        throw new IllegalArgumentException("No parser found for class " + clazz.getName());
    }
}
