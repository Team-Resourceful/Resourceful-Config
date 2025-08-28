package com.teamresourceful.resourcefulconfig.client.utils;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface State<T> extends Consumer<T>, Supplier<T> {

    static <T> State<T> of(Consumer<T> setter, Supplier<T> getter) {
        return new State<T>() {
            @Override
            public void set(T s) {
                setter.accept(s);
            }

            @Override
            public T get() {
                return getter.get();
            }
        };
    }

    void set(T t);

    @Override
    default void accept(T t) {
        set(t);
    }

    static <T> State<T> of(T defaultValue) {
        return new State<>() {

            private T value = defaultValue;

            @Override
            public T get() {
                return this.value;
            }

            @Override
            public void set(T t) {
                this.value = t;
            }
        };
    }
}
