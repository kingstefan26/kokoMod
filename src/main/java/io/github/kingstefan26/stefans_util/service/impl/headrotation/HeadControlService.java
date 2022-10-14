/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.service.impl.headrotation;

import io.github.kingstefan26.stefans_util.service.Service;
import io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.IInterpolator;
import io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.impl.LogarithmicInterpolator;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import javax.vecmath.Vector2d;

public class HeadControlService extends Service {
    @Setter
    IInterpolator interpolator = new LogarithmicInterpolator(0.1F);
    @Getter
    float partialTick;
    private float currenttargetyaw;
    private float currentTragetPitch;
    private float originalYaw;
    private float originalPitch;

    public HeadControlService() {
        super(HeadControlService.class.getName());
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    void stateCheck() {
        if (interpolator == null) {
            throw new IllegalStateException("interpolator is null");
        }
    }

    public Vector2d faceTarget(Vec3 target) {
        double var4 = target.xCoord - mc.thePlayer.posX;
        double var8 = target.zCoord - mc.thePlayer.posZ;
        double var6 = target.yCoord - mc.thePlayer.posY;
        double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float) (Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
        float var13 = (float) (-(Math.atan2(var6, var14) * 180.0D / Math.PI));
        float targetPitch = mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(var13 - mc.thePlayer.rotationPitch);
        float targetYaw = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(var12 - mc.thePlayer.rotationYaw);
        return new Vector2d(targetYaw, targetPitch);
    }

    public void update() {
        this.interpolator.update();
        mc.thePlayer.rotationYaw = interpolator.interpolYaw(originalYaw, currenttargetyaw);
        mc.thePlayer.rotationPitch = interpolator.interpolPitch(originalPitch, currentTragetPitch);
    }

    public void setPlayerRotations(float yaw, float pitch) {
        setPlayerPitch(pitch);
        setPlayerYaw(yaw);
    }

    public void setPlayerYaw(float yaw) {
        stateCheck();
        currenttargetyaw = yaw;
        originalYaw = mc.thePlayer.rotationYaw;
    }

    public void setPlayerPitch(float pitch) {
        stateCheck();
        currentTragetPitch = pitch;
        originalPitch = mc.thePlayer.renderArmPitch;
    }

    public void setPartialTick(float renderTickTime) {
        partialTick = renderTickTime;
        interpolator.setParitialTick(partialTick);
    }


}
