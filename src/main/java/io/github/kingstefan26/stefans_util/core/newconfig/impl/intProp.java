/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newconfig.impl;

import com.google.gson.annotations.Expose;
import io.github.kingstefan26.stefans_util.core.newconfig.Property;

public class intProp extends Property<Integer> {

    @Expose
    private int value;

    public intProp(String name, int deafultValue) {
        super(name);
        this.value = deafultValue;
    }

    @Override
    protected void set(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getProperty() {
        return value;
    }
}
