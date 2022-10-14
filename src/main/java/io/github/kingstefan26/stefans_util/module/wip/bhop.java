/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.wip;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderSetting;
import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Comparator;
import java.util.List;

public class bhop extends BasicModule {

    static float accel = 1.2f;
    public Double range = 3.85d;
    public float rotationYaw;
    public float rotationPitch;
    public float aps = 6.1f;
    float bhopspeed;
    Double lenghtLimit;
    double rotationSpeed;
    long timer;
    float targetYaw;
    float targetPitch;
    private Boolean bhop;
    private EntityLivingBase targetEntity;
    private boolean lockview = true;

    public bhop() {
        super("Bhop", "ehh", ModuleManager.Category.WIP);

    }

    public static float lerpAngle(float fromRadians, float toRadians, float progress) {
        float f = ((toRadians - fromRadians) % 360.0F + 540.0F) % 360.0F - 180.0F;
//        f *= accel;
        return (fromRadians + f * progress % 360.0F);
    }

    @Override
    public void onLoad() {
        new SliderSetting("Bhop Speed", this, 2, 1, 20, (newvalue) -> {
            bhopspeed = (float) (newvalue * 0.1);
        });

        new SliderSetting("aura range", this, 6, 1, 20, (newvalue) -> {
            range = newvalue;
        });

        new SliderSetting("dont get to close", this, 2, 1, 4, (newvalue) -> {
            lenghtLimit = newvalue;
        });

        new SliderSetting("rotation Speed", this, 10, 1, 200, (newvalue) -> {
            rotationSpeed = newvalue * 0.001;
        });


        new CheckSetting("Bhop", this, true, (nval) -> {
            bhop = nval;
        });


        super.onLoad();
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!isToggled() || mc.thePlayer == null || targetEntity == null) return;
        setPlayerRotations(targetYaw, targetPitch);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!isToggled() || mc.thePlayer == null) return;
        if (WorldInfoService.isOnHypixel()) {
            this.toggle();
            return;
        }
        onPreMotionUpdates();
        onPostMotionUpdates();
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!isToggled()) return;
        if (event.entity == mc.thePlayer) {
//            onPreMotionUpdates();
//            onPostMotionUpdates();
            if (bhop) {
                setMoveSpeed(bhopspeed, (EntityPlayer) event.entity);
                mc.thePlayer.velocityChanged = true;
                if (event.entity.onGround && ((EntityPlayerSP) event.entity).moveForward > 0 && !event.entity.isSneaking()) {
                    mc.thePlayer.jump();
                }
            }
        }
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873D;
//        if (mc.theWorld != null && mc.thePlayer.isPotionActive(Potion.getPotionLocations().get)) {
//            int amplifire = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
//            baseSpeed *= (1D + 0.2D * (amplifire + 1));
//        }
        return baseSpeed;
    }

    private void setMoveSpeed(double moveSpeed, EntityPlayer event) {
        double forward = mc.thePlayer.moveForward;
        double strafe = mc.thePlayer.moveStrafing;
        float yaw = mc.thePlayer.rotationYaw;


        if ((forward == 0.0D) && (strafe == 0.0D)) {
            event.motionX = 0.0D;
            event.motionZ = (0.0D);
        } else {
            if (targetEntity != null) {
                if (event.getDistanceToEntity(targetEntity) < lenghtLimit * lenghtLimit) {
                    forward = 0;
                }
            }
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1.0D;
                } else if (forward < 0.0D) {
                    forward = -1.0D;
                }
            }
            event.motionX = (forward * moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)));
            event.motionZ = (forward * moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)));
        }
    }

    @Override
    public void onDisable() {
        targetEntity = null;
    }

    public final void onPreMotionUpdates() {
//        this.rotationYaw = mc.thePlayer.rotationYaw;
//        this.rotationPitch = mc.thePlayer.rotationPitch;
        targetEntity = getEntity();
        if (targetEntity == null) return;
        faceEntity(targetEntity);
    }

    public final void onPostMotionUpdates() {
        if (targetEntity == null) return;
        attackEntity(targetEntity);
        if (!lockview) {
            mc.thePlayer.rotationYaw = this.rotationYaw;
            mc.thePlayer.rotationPitch = this.rotationPitch;
        }
    }

    public void setPlayerRotations(float yaw, float pitch) {
        mc.thePlayer.rotationYaw = lerpAngle(mc.thePlayer.rotationYaw, yaw, (float) rotationSpeed);
        mc.thePlayer.rotationPitch = lerpAngle(mc.thePlayer.rotationPitch, pitch, (float) rotationSpeed);
    }

    public boolean canAttackEntity(EntityLivingBase entity) {
        if (entity == null) return false;
        return entity.isEntityAlive() && mc.thePlayer.isEntityAlive() && mc.thePlayer.canEntityBeSeen(entity) && mc.thePlayer.getDistanceToEntity(entity) <= range && entity != mc.thePlayer && entity instanceof EntityLivingBase;
    }

    public final void attackEntity(EntityLivingBase entity) {

        if (System.currentTimeMillis() > timer) {
            timer = System.currentTimeMillis() + 50;
            if (mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) return;
            mc.thePlayer.swingItem();
            mc.thePlayer.setSprinting(false);
            mc.playerController.attackEntity(mc.thePlayer, entity);
            mc.thePlayer.setSprinting(false);
        }

    }

    private EntityLivingBase getEntity() {
        List<EntityLivingBase> entieres = mc.theWorld.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(mc.thePlayer.posX + range, mc.thePlayer.posY + range, mc.thePlayer.posZ + range, mc.thePlayer.posX - range, mc.thePlayer.posY - range, mc.thePlayer.posZ - range));

        Comparator<EntityLivingBase> changeComparator = Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer));

        entieres.sort(changeComparator);

        for (final EntityLivingBase o : entieres) {
            if (o == null) continue;
            if (o == mc.thePlayer) continue;
            if (!mc.thePlayer.canEntityBeSeen(o)) continue;
            if (canAttackEntity(o)) {
                return o;
            }
        }
        return null;
    }

    public synchronized void faceEntity(Entity entity) {
        double var4 = entity.posX - mc.thePlayer.posX;
        double var8 = entity.posZ - mc.thePlayer.posZ;
        double var6 = (entity.posY - mc.thePlayer.posY) - (entity.height / 2);
//        double var6 = entity.posY - mc.thePlayer.posY + (entity instanceof EntityPlayer ? 0 : entity.height / 2);
//        logger.info(entity.height);
//        logger.info(entity.posY);
//        entity.getEntityBoundingBox();
        double var14 = (double) MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float) (Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
        float var13 = (float) (-(Math.atan2(var6, var14) * 180.0D / Math.PI));
        targetPitch = mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(var13 - mc.thePlayer.rotationPitch);
        targetYaw = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(var12 - mc.thePlayer.rotationYaw);
    }

}
