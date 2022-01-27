/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newconfig.impl;

import com.google.gson.annotations.Expose;
import io.github.kingstefan26.stefans_util.core.newconfig.Property;

public class boolProp extends Property<Boolean> {
    @Expose
    private boolean value;

    public boolProp(String name, boolean deafultValue) {
        super(name);
        this.value = deafultValue;
    }

    @Override
    public Boolean getProperty() {
        return value;
    }

    @Override
    public void set(Boolean value) {
        this.value = value;
    }
}
