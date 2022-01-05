/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newConfig.impl;

import io.github.kingstefan26.stefans_util.core.newConfig.Iproperty;

public class stringProp implements Iproperty {
    private final String name;
    private String value;

    public stringProp(String name, String deafultValue) {
        this.name = name;
        this.value = deafultValue;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setProperty(Object input) {
        value = (String) input;
    }

    @Override
    public Object getProperty() {
        return value;
    }
}
