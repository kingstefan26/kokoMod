/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator;

public class InterpolatorUtil {
    public static float getDistance(float from, float to) {
        return ((to - from) % 360.0F + 540.0F) % 360.0F - 180.0F;
    }

}
