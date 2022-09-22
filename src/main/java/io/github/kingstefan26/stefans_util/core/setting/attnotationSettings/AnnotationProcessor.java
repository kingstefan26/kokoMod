/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.setting.attnotationSettings;

import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.attnotationSettings.attnotaions.*;
import io.github.kingstefan26.stefans_util.core.setting.impl.*;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Consumer;

public class AnnotationProcessor implements IannotationProcessor {

    @Override
    public void processField(Field field, Object parent) {
        field.setAccessible(true);
        if (parent instanceof BasicModule) {


            final Consumer ungenericConsumer = value -> {
                try {
                    field.setAccessible(true);
                    field.set(parent, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            };


            if (field.isAnnotationPresent(boolenSetting.class)) {
                boolenSetting optionAnnotation = field.getAnnotation(boolenSetting.class);

                if (!optionAnnotation.description().equals("null")) {
                    new CheckSetting(optionAnnotation.name(), (BasicModule) parent, optionAnnotation.defaultValue(), ungenericConsumer, optionAnnotation.description());
                } else {
                    new CheckSetting(optionAnnotation.name(), (BasicModule) parent, optionAnnotation.defaultValue(), ungenericConsumer);
                }
            }
            if (field.isAnnotationPresent(choseakeysetting.class)) {
                choseakeysetting optionAnnotation = field.getAnnotation(choseakeysetting.class);
                if (!optionAnnotation.description().equals("null")) {
                    new ChoseAKeySetting(optionAnnotation.name(), (BasicModule) parent, optionAnnotation.defaultValue(), ungenericConsumer, optionAnnotation.description());
                } else {
                    new ChoseAKeySetting(optionAnnotation.name(), (BasicModule) parent, optionAnnotation.defaultValue(), ungenericConsumer);
                }
            }

            if (field.isAnnotationPresent(multiChoisesetting.class)) {
                multiChoisesetting optionAnnotation = field.getAnnotation(multiChoisesetting.class);
                if (!optionAnnotation.description().equals("null")) {
                    new MultichoiseSetting(optionAnnotation.name(),
                            (BasicModule) parent,
                            optionAnnotation.defaultValue(),
                            optionAnnotation.options(),
                            ungenericConsumer,
                            optionAnnotation.description());

                } else {
                    new MultichoiseSetting(optionAnnotation.name(),
                            (BasicModule) parent,
                            optionAnnotation.defaultValue(),
                            optionAnnotation.options(),
                            ungenericConsumer);
                }
            }

            if (field.isAnnotationPresent(slidersetting.class)) {
                slidersetting optionAnnotation = field.getAnnotation(slidersetting.class);
                if (!optionAnnotation.description().equals("null")) {
                    new SliderSetting(optionAnnotation.name(),
                            (BasicModule) parent,
                            optionAnnotation.defaultValue(),
                            optionAnnotation.min(),
                            optionAnnotation.max(),
                            ungenericConsumer,
                            optionAnnotation.description());

                } else {
                    new SliderSetting(optionAnnotation.name(),
                            (BasicModule) parent,
                            optionAnnotation.defaultValue(),
                            optionAnnotation.min(),
                            optionAnnotation.max(),
                            ungenericConsumer);
                }
            }
            if (field.isAnnotationPresent(slidernodecimalsetting.class)) {
                slidernodecimalsetting optionAnnotation = field.getAnnotation(slidernodecimalsetting.class);


                new SliderNoDecimalSetting(optionAnnotation.name(),
                        (BasicModule) parent,
                        optionAnnotation.defaultValue(),
                        optionAnnotation.min(),
                        optionAnnotation.max(),
                        ungenericConsumer,
                        // here im abusing the fact that arrays as function arguments can be null
                        Objects.equals(optionAnnotation.description(), "null") ? new String[]{optionAnnotation.description()} : null);

            }


        }
    }
}
