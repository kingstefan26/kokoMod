/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newconfig.impl;

import com.google.gson.annotations.Expose;
import io.github.kingstefan26.stefans_util.core.newconfig.Property;

public class floatProp extends Property<Float> {

    @Expose
    private float value;

    public floatProp(String name, float deafultValue) {
        super(name);
        this.value = deafultValue;
    }

    @Override
    protected void set(Float value) {
        this.value = value;
    }

    @Override
    public Float getProperty() {
        return value;
    }
}
