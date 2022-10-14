/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator;

import javax.vecmath.Vector2f;

public interface IInterpolator {
    float interpolYaw(float target, float current);

    float interpolPitch(float target, float current);

    Vector2f interpolPitchYaw(float targetYaw, float currentYaw, float targetPitch, float currentPitch);

    void setParitialTick(float renderTickTime);

    void update();
}
