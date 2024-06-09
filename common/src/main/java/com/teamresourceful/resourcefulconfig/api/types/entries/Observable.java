package com.teamresourceful.resourcefulconfig.api.types.entries;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Observable<T> implements Supplier<T>, Consumer<T> {

    private final List<BiConsumer<T, T>> listeners = new ArrayList<>();
    private T value;

    private Observable(T value) {
        this.value = value;
    }

    public Class<?> type() {
        return value.getClass();
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public void accept(T t) {
        var previous = this.value;
        this.value = t;
        this.listeners.forEach(listener -> listener.accept(previous, value));
    }

    /**
     * @hidden Casts the value to the correct type, this method is used internally.
     */
    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    public void setAndCast(Object value) {
        accept((T) value);
    }

    public void addListener(BiConsumer<T, T> listener) {
        listeners.add(listener);
    }

    public static <T> Observable<T> of(T value) {
        return new Observable<>(value);
    }
}
