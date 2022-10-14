/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.impl;

import io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.IInterpolator;
import io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.InterpolatorUtil;

import javax.vecmath.Vector2f;

public class LogarithmicInterpolator implements IInterpolator {


    private float rateOfChange;

    public LogarithmicInterpolator(float rateOfChange) {
        this.rateOfChange = rateOfChange;
    }

    float lerpAngle(float toDegrees, float fromDegrees) {
        float distanceLeft = InterpolatorUtil.getDistance(fromDegrees, toDegrees);
        return fromDegrees + ((distanceLeft * rateOfChange) % 360.0F);
    }

    @Override
    public float interpolYaw(float target, float current) {
        return lerpAngle(current, target);
    }

    @Override
    public float interpolPitch(float target, float current) {
        return lerpAngle(current, target);
    }

    @Override
    public Vector2f interpolPitchYaw(float targetYaw, float currentYaw, float targetPitch, float currentPitch) {
        return new Vector2f(lerpAngle(currentYaw, targetYaw), lerpAngle(currentPitch, targetPitch));
    }

    @Override
    public void setParitialTick(float renderTickTime) {

    }

    @Override
    public void update() {

    }
}
