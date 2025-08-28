package com.teamresourceful.resourcefulconfig.client.utils;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ApiStatus.Internal
public class ListenableState<T> extends DelegatedState<T> {

    protected final List<Consumer<T>> listeners = new ArrayList<>();

    public ListenableState(T value) {
        this(State.of(value));
    }

    public ListenableState(State<T> value) {
        super(value);
    }

    public static <T> ListenableState<T> of(T initial) {
        return new ListenableState<>(initial);
    }

    public static <T> ListenableState<T> of(State<T> initial) {
        return new ListenableState<>(initial);
    }

    public static <T> ListenableState<T> empty() {
        return of(null);
    }

    @Override
    public void set(T value) {
        super.set(value);
        listeners.forEach(listener -> listener.accept(value));
    }

    public void registerListener(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void unregisterListener(Consumer<T> listener) {
        listeners.remove(listener);
    }
}
