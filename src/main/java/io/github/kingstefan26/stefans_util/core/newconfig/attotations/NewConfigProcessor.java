/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newconfig.attotations;

import io.github.kingstefan26.stefans_util.core.newconfig.ConfigManagerz;
import io.github.kingstefan26.stefans_util.core.newconfig.Iproperty;
import io.github.kingstefan26.stefans_util.core.newconfig.attotations.impl.BooleanConfigValue;
import io.github.kingstefan26.stefans_util.core.newconfig.attotations.impl.DoubleConfigValue;
import io.github.kingstefan26.stefans_util.core.newconfig.attotations.impl.IntegerConfigValue;
import io.github.kingstefan26.stefans_util.core.newconfig.attotations.impl.StringConfigValue;
import io.github.kingstefan26.stefans_util.util.AttotaionProcessor;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public class NewConfigProcessor extends AttotaionProcessor {

    @Override
    public void processField(Field field, Object parent) {
        field.setAccessible(true);

        final Consumer ungenericConsumer = value -> {
            try {
                field.setAccessible(true);
                field.set(parent, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        };

        Iproperty createdproperty = null;

        if (field.isAnnotationPresent(BooleanConfigValue.class)) {
            BooleanConfigValue optionAnnotation = field.getAnnotation(BooleanConfigValue.class);

            String propname = optionAnnotation.category().equals("null") ? optionAnnotation.name() : optionAnnotation.category() + "." + optionAnnotation.name();

            createdproperty = ConfigManagerz.getInstance().getConfigObject(propname, optionAnnotation.defaultValue());
        }

        if (field.isAnnotationPresent(DoubleConfigValue.class)) {
            DoubleConfigValue optionAnnotation = field.getAnnotation(DoubleConfigValue.class);

            String propname = optionAnnotation.category().equals("null") ? optionAnnotation.name() : optionAnnotation.category() + "." + optionAnnotation.name();

            createdproperty = ConfigManagerz.getInstance().getConfigObject(propname, optionAnnotation.defaultValue());
        }

        if (field.isAnnotationPresent(StringConfigValue.class)) {
            StringConfigValue optionAnnotation = field.getAnnotation(StringConfigValue.class);

            String propname = optionAnnotation.category().equals("null") ? optionAnnotation.name() : optionAnnotation.category() + "." + optionAnnotation.name();

            createdproperty = ConfigManagerz.getInstance().getConfigObject(propname, optionAnnotation.defaultValue());
        }

        if (field.isAnnotationPresent(IntegerConfigValue.class)) {
            IntegerConfigValue optionAnnotation = field.getAnnotation(IntegerConfigValue.class);

            String propname = optionAnnotation.category().equals("null") ? optionAnnotation.name() : optionAnnotation.category() + "." + optionAnnotation.name();

            createdproperty = ConfigManagerz.getInstance().getConfigObject(propname, optionAnnotation.defaultValue());
        }

        if (createdproperty != null) {
            createdproperty.setCallBack(ungenericConsumer);
            ConfigProcessorEngine.getInstance().addEntry(createdproperty, new ProcessedFiled(field, parent));
        }

    }
}
