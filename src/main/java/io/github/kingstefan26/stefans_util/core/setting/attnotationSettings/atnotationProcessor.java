/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.setting.attnotationSettings;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.setting.attnotationSettings.attnotaions.*;
import io.github.kingstefan26.stefans_util.core.setting.impl.*;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public class atnotationProcessor {
    private atnotationProcessor() {
        throw new SecurityException("Cannot create instance of this class");
    }

    public static void init(Object parent) {
        try {
            Field[] fields = parent.getClass().getDeclaredFields();

            processFields(fields, parent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void processFields(Field[] fields, Object parent) {

        for (Field field : fields) {
            field.setAccessible(true);
            if (parent instanceof basicModule) {

                Consumer<Integer> intconsumer = val -> {
                    try {
                        field.setAccessible(true);
                        field.set(parent, val);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                };

                final Consumer<Double> dobuleconsumer = val -> {
                    try {
                        field.setAccessible(true);
                        field.set(parent, val);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                };

                final Consumer<String> stringconsumer = val -> {
                    try {
                        field.setAccessible(true);
                        field.set(parent, val);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                };

                final Consumer<Boolean> boolconsumer = val -> {
                    try {
                        field.setAccessible(true);
                        field.set(parent, val);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                };


                if (field.isAnnotationPresent(boolenSetting.class)) {
                    boolenSetting optionAnnotation = field.getAnnotation(boolenSetting.class);

                    if (!optionAnnotation.description().equals("null")) {
                        new CheckSetting(optionAnnotation.name(), (basicModule) parent, optionAnnotation.defaultValue(), boolconsumer, optionAnnotation.description());
                    } else {
                        new CheckSetting(optionAnnotation.name(), (basicModule) parent, optionAnnotation.defaultValue(), boolconsumer);
                    }
                }
                if (field.isAnnotationPresent(choseakeysetting.class)) {
                    choseakeysetting optionAnnotation = field.getAnnotation(choseakeysetting.class);
                    if (!optionAnnotation.description().equals("null")) {
                        new ChoseAKeySetting(optionAnnotation.name(), (basicModule) parent, optionAnnotation.defaultValue(), intconsumer, optionAnnotation.description());
                    } else {
                        new ChoseAKeySetting(optionAnnotation.name(), (basicModule) parent, optionAnnotation.defaultValue(), intconsumer);
                    }
                }

                if (field.isAnnotationPresent(multiChoisesetting.class)) {
                    multiChoisesetting optionAnnotation = field.getAnnotation(multiChoisesetting.class);
                    if (!optionAnnotation.description().equals("null")) {
                        new MultichoiseSetting(optionAnnotation.name(),
                                (basicModule) parent,
                                optionAnnotation.defaultValue(),
                                optionAnnotation.options(),
                                stringconsumer,
                                optionAnnotation.description());

                    } else {
                        new MultichoiseSetting(optionAnnotation.name(),
                                (basicModule) parent,
                                optionAnnotation.defaultValue(),
                                optionAnnotation.options(),
                                stringconsumer);
                    }
                }

                if (field.isAnnotationPresent(slidersetting.class)) {
                    slidersetting optionAnnotation = field.getAnnotation(slidersetting.class);
                    if (!optionAnnotation.description().equals("null")) {
                        new SliderSetting(optionAnnotation.name(),
                                (basicModule) parent,
                                optionAnnotation.defaultValue(),
                                optionAnnotation.min(),
                                optionAnnotation.max(),
                                dobuleconsumer,
                                optionAnnotation.description());

                    } else {
                        new SliderSetting(optionAnnotation.name(),
                                (basicModule) parent,
                                optionAnnotation.defaultValue(),
                                optionAnnotation.min(),
                                optionAnnotation.max(),
                                dobuleconsumer);
                    }
                }
                if (field.isAnnotationPresent(slidernodecimalsetting.class)) {
                    slidernodecimalsetting optionAnnotation = field.getAnnotation(slidernodecimalsetting.class);
                    if (!optionAnnotation.description().equals("null")) {
                        new SliderNoDecimalSetting(optionAnnotation.name(),
                                (basicModule) parent,
                                optionAnnotation.defaultValue(),
                                optionAnnotation.min(),
                                optionAnnotation.max(),
                                dobuleconsumer,
                                optionAnnotation.description());

                    } else {
                        new SliderNoDecimalSetting(optionAnnotation.name(),
                                (basicModule) parent,
                                optionAnnotation.defaultValue(),
                                optionAnnotation.min(),
                                optionAnnotation.max(),
                                dobuleconsumer);
                    }
                }


            }
        }
    }
}
