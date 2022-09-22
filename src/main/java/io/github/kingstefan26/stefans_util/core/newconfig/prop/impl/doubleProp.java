/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newconfig.prop.impl;

import com.google.gson.annotations.Expose;
import io.github.kingstefan26.stefans_util.core.newconfig.prop.Property;

public class doubleProp extends Property<Double> {
    @Expose
    private double value;

    public doubleProp(String name, double deafultValue) {
        super(name);
        this.value = deafultValue;
    }

    @Override
    public void set(Double set) {
        value = set;
    }

    @Override
    public Double getProperty() {
        return value;
    }
}
