/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.impl;

import io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.IInterpolator;

import javax.vecmath.Vector2f;

public class LinearInterpolator implements IInterpolator {

    private float partialTick;

    float LinearInterpolate(
            float y1, float y2,
            float mu) {
        return (y1 * (1 - mu) + y2 * mu);
    }

    @Override
    public float interpolYaw(float target, float current) {
        return LinearInterpolate(target, current, partialTick);
    }

    @Override
    public float interpolPitch(float target, float current) {
        return LinearInterpolate(target, current, partialTick);
    }

    @Override
    public Vector2f interpolPitchYaw(float targetYaw, float currentYaw, float targetPitch, float currentPitch) {
        return null;
    }

    @Override
    public void setParitialTick(float renderTickTime) {
        this.partialTick = renderTickTime;
    }

    @Override
    public void update() {

    }
}
