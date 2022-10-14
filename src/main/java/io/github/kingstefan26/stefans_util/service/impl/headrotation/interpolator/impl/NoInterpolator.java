package io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.impl;

import io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.IInterpolator;

import javax.vecmath.Vector2f;

public class NoInterpolator implements IInterpolator {

    @Override
    public float interpolYaw(float target, float current) {
        return current;
    }

    @Override
    public float interpolPitch(float target, float current) {
        return current;
    }

    @Override
    public Vector2f interpolPitchYaw(float targetYaw, float currentYaw, float targetPitch, float currentPitch) {
        return new Vector2f(targetYaw, targetPitch);
    }

    @Override
    public void setParitialTick(float renderTickTime) {

    }

    @Override
    public void update() {

    }
}