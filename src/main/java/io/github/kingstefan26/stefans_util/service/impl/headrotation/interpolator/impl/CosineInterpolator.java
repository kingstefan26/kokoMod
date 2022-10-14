/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.impl;

import io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.IInterpolator;

import javax.vecmath.Vector2f;

import static java.lang.Math.PI;

public class CosineInterpolator implements IInterpolator {


    private float partialTick;

    double CosineInterpolate(
            double y1, double y2,
            double mu) {
        double mu2;

        mu2 = (1 - Math.cos(mu * PI)) / 2;
        return (y1 * (1 - mu2) + y2 * mu2);
    }


    @Override
    public float interpolYaw(float target, float current) {
        return (float) CosineInterpolate(target, current, partialTick);
    }


    @Override
    public float interpolPitch(float target, float current) {
        return (float) CosineInterpolate(target, current, partialTick);
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
