/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newconfig.prop;

import lombok.Getter;

import java.util.function.Consumer;

public abstract class Property<T> implements Iproperty<T> {
    @Getter
    private final String name;

    private transient Consumer<T> callback;

    protected Property(String name) {
        this.name = name;
    }

    @Override
    public final void setCallBack(Consumer<T> callback) {
        this.callback = callback;
    }

    @Override
    public void setProperty(T set) {
        this.set(set);
        if (callback != null) callback.accept(set);
    }

    protected abstract void set(T value);

}
