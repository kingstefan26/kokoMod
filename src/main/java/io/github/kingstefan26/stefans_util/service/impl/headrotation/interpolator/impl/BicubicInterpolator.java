package io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.impl;

import io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.IInterpolator;

import javax.vecmath.Vector2f;

public class BicubicInterpolator implements IInterpolator {


    private final float a;
    private final float b;
    private float tick;

    public BicubicInterpolator(float a, float b) {

        this.a = a;
        this.b = b;
    }

    /**
     * IDK the y's might be fucked up
     * BUT the pre and after points (necessary for bicubic) are made up to achieve different results
     *
     * @param y0           point before (made up)
     * @param y1           staring
     * @param y2           target
     * @param y3           after target (made up)
     * @param currentPoint float between 0-1 that determining how close to target are we
     * @return the interpolated value, somewhere between y1 - y2
     */
    double CubicInterpolate(double y0, double y1, double y2, double y3, double currentPoint) {
        y2 = y0 - 10;
        y3 = y1 + 10;

        double mu2 = currentPoint * currentPoint;
        double a0 = y3 - y2 - y0 + y1;
        double a1 = y0 - y1 - a0;
        double a2 = y2 - y0;
        double a3 = y1;

        return (a0 * currentPoint * mu2
                +
                a1 * mu2
                +
                a2 * currentPoint
                +
                a3
        );
    }

    @Override
    public float interpolYaw(float target, float current) {
        return (float) CubicInterpolate(current, target, a, b, tick);
    }

    @Override
    public float interpolPitch(float target, float current) {
        return (float) CubicInterpolate(current, target, a, b, tick);
    }

    @Override
    public Vector2f interpolPitchYaw(float targetYaw, float currentYaw, float targetPitch, float currentPitch) {
        return null;
    }

    @Override
    public void setParitialTick(float renderTickTime) {
        this.tick = renderTickTime;
    }

    @Override
    public void update() {

    }
}