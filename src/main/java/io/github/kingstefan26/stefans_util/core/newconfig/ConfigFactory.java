/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newconfig;

import io.github.kingstefan26.stefans_util.core.newconfig.impl.*;

public class ConfigFactory {
    private ConfigFactory() {
    }

    public static Iproperty getProperty(String name, Object deafult) {
        if (deafult instanceof String) {
            return new stringProp(name, (String) deafult);
        } else if (deafult instanceof Double) {
            return new doubleProp(name, (Double) deafult);
        } else if (deafult instanceof Integer) {
            return new intProp(name, (int) deafult);
        } else if (deafult instanceof Boolean) {
            return new boolProp(name, (boolean) deafult);
        } else if (deafult instanceof Float) {
            return new floatProp(name, (Float) deafult);
        }
        return null;
    }
}
