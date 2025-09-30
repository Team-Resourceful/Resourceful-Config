package com.teamresourceful.resourcefulconfig.client.utils;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ApiStatus.Internal
public class ListenableState<T> implements State<T> {

    protected final State<T> delegate;
    protected final List<Consumer<T>> listeners = new ArrayList<>();

    public ListenableState(T value) {
        this(State.of(value));
    }

    public ListenableState(State<T> value) {
        this.delegate = value;
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
        this.delegate.set(value);
        this.listeners.forEach(listener -> listener.accept(value));
    }

    @Override
    public T get() {
        return this.delegate.get();
    }

    public void registerListener(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void unregisterListener(Consumer<T> listener) {
        listeners.remove(listener);
    }
}
