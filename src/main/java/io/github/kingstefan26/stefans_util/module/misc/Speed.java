/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Speed extends BasicModule {
    public static double speed;
    public static double moveSpeed;
    public static boolean canStep;
    private int level = 1;
    private double lastDist;
    private double[] values = new double[]{0.08D, 0.09316090325960147D, 1.35D, 2.149D, 0.66D};

    public Speed() {
        super("speed", "DONT USE ON HYPCIKE", ModuleManager.Category.DEBUG);
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        } else {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
        }

        return baseSpeed;
    }

    public void onEvent() {
        double y = 0;
        double x;
        double z;

        if (mc.thePlayer.isSneaking()) {
            return;
        }

        if (mc.thePlayer.onGround) {
            this.level = 2;
        }

        if (round(mc.thePlayer.posY - (double) ((int) mc.thePlayer.posY), 3) == round(0.138D, 3)) {
            EntityPlayerSP var10000 = mc.thePlayer;
            var10000.motionY -= this.values[0];
            y -= this.values[1];
            var10000 = mc.thePlayer;
            var10000.posY -= this.values[1];
        }

        if (this.level != 1 || mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
            if (this.level == 2) {
                this.level = 3;
                if (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) {
                    mc.thePlayer.motionY = 0.4D;
                    y = 0.4D;
                    moveSpeed *= this.values[3];
                }
            } else if (this.level == 3) {
                this.level = 4;
                double difference = this.values[4] * (this.lastDist - this.getBaseMoveSpeed());
                moveSpeed = this.lastDist - difference;
            } else {

                if (mc.thePlayer.onGround && (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getCollisionBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically)) {
                    this.level = 1;
                }

                moveSpeed = this.lastDist - this.lastDist / 159.0D;
            }
        } else {
            this.level = 2;
            moveSpeed = this.values[2] * this.getBaseMoveSpeed() - 0.01D;
        }

        moveSpeed = Math.max(moveSpeed, this.getBaseMoveSpeed());
        MovementInput movementInput = mc.thePlayer.movementInput;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if (forward == 0.0F && strafe == 0.0F) {
            x = 0.0D;
            z = 0.0D;
        } else if (forward != 0.0F) {
            if (strafe > 0.0F) {
                yaw += (float) (forward > 0.0F ? -45 : 45);
                strafe = 0.0F;
            } else if (strafe < 0.0F) {
                yaw += (float) (forward > 0.0F ? 45 : -45);
                strafe = 0.0F;
            }

            if (forward > 0.0F) {
                forward = 1.0F;
            } else if (forward < 0.0F) {
                forward = -1.0F;
            }
        }

        double mx = Math.cos(Math.toRadians((double) (yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((double) (yaw + 90.0F)));
        x = (double) forward * moveSpeed * mx + (double) strafe * moveSpeed * mz;
        z = (double) forward * moveSpeed * mz - (double) strafe * moveSpeed * mx;
        canStep = true;
        mc.thePlayer.stepHeight = 0.6F;
        if (forward == 0.0F && strafe == 0.0F) {
            x = 0.0D;
            z = 0.0D;
        }

        mc.thePlayer.motionY = y;
        mc.thePlayer.motionX = x;
        mc.thePlayer.motionZ = z;

    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (!isToggled() || mc.thePlayer == null) return;

        onEvent();

        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);


    }
}
