package com.teamresourceful.resourcefulconfig.client.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface State<T> extends Consumer<T>, Supplier<T> {

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
