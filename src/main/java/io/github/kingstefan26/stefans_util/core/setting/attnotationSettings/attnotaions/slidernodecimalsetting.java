/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.setting.attnotationSettings.attnotaions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface slidernodecimalsetting {
    String name();

    String description() default "null";

    int defaultValue();

    int min();

    int max();
}
