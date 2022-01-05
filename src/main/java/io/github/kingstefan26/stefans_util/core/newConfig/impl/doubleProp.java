/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newConfig.impl;

import io.github.kingstefan26.stefans_util.core.newConfig.Iproperty;

public class doubleProp implements Iproperty {
    public doubleProp(String name, double deafultValue) {
        this.name = name;
        this.value = deafultValue;
    }
    private final String name;

    private double value;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setProperty(Object set) {
        value = (double) set;
    }

    @Override
    public Object getProperty() {
        return value;
    }
}
