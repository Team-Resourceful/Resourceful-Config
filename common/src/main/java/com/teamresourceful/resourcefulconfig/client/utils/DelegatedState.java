package com.teamresourceful.resourcefulconfig.client.utils;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class DelegatedState<T> implements State<T> {

    protected final State<T> delegate;

    public DelegatedState(State<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void set(T value) {
        this.delegate.set(value);
    }

    @Override
    public T get() {
        return this.delegate.get();
    }
}

