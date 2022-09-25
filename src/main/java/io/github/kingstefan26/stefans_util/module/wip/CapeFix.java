/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.wip;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.module.debug.TestAValue;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;

public class CapeFix extends BasicModule {
    public static boolean enabled = false;

    public static boolean flag1;

    public CapeFix() {
        super("CapeFix", "Make Cape not go erect when high speed", ModuleManager.Category.RENDER, new presistanceDecorator());

        new CheckSetting("flag1", this, false, newvalue -> flag1 = newvalue);
    }

    public static void rendercapeRedirect(AbstractClientPlayer playerIn, float partialTicks, RenderPlayer playerRenderer) {
        if (!playerIn.hasPlayerInfo()) return;
        if (playerIn.isInvisible()) return;
        if (!playerIn.isWearing(EnumPlayerModelParts.CAPE)) return;
        if (playerIn.getLocationCape() == null) return;


        // CAPE BEHAVIOR
        // it bobs up and down when walking in all directions
        // if we walk sideways it goes the opposite way
        // moves with shift
        // the faster we walk forward more straight it gets
        //


        GlStateManager.color(1F, 1.0F, 1.0F, 1.0F);
        playerRenderer.bindTexture(playerIn.getLocationCape());
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 0.125F);

        double realX = playerIn.prevChasingPosX + (playerIn.chasingPosX - playerIn.prevChasingPosX) * partialTicks - (playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * partialTicks);
        double realY = playerIn.prevChasingPosY + (playerIn.chasingPosY - playerIn.prevChasingPosY) * partialTicks - (playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * partialTicks);
        double realZ = playerIn.prevChasingPosZ + (playerIn.chasingPosZ - playerIn.prevChasingPosZ) * partialTicks - (playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * partialTicks);
        float realYawOffSet = playerIn.prevRenderYawOffset + (playerIn.renderYawOffset - playerIn.prevRenderYawOffset) * partialTicks;

        double d3 = MathHelper.sin(realYawOffSet * (float) Math.PI / 180.0F);
        double d4 = (-MathHelper.cos(realYawOffSet * (float) Math.PI / 180.0F));

        if (flag1) {

        }

        // d3 d4 realYawOffSet dont seem to go out of norm when insane speed


        // this appers to control modyfiing cape when flying and the bobbing behavior
        float f1 = (float) realY * 10.0F;
        f1 = MathHelper.clamp_float(f1, -6.0F, 32.0F);

        // and this one the one corralled with speed
        float f2 = (float) (realX * d3 + realZ * d4) * 100.0F;

        // this contros the side to side movment
        float f3 = (float) (realX * d4 - realZ * d3) * 100.0F;


        // this makes so the cape doesn't fly into you when walking back
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (CapeFix.enabled) {
            f2 = MathHelper.clamp_float(f2, -150F, 150F);
            f3 = MathHelper.clamp_float(f3, -150F, 150F);
        }


        float realYaw = playerIn.prevCameraYaw + (playerIn.cameraYaw - playerIn.prevCameraYaw) * partialTicks;

        // sin( The distance walked a tick ago + the difference between distance walked now and then interpolated for ticks * 6 ) * 32 * change that when player moves around
        // this is the rate of "change" in perspective to walking its -6 flying up 32 max
        // -6 = sit flush with player,,,, 32 stay away from player
        // WHEN PLAYER IS FLYING UP AND WALKING FORWARD f1 IS -6  CAPE IS FLYTING UP INTO THE HEAD WE DONT WANT THAT
        // IT SHOULD BE POINTING BACKWARDS NOT INTO THE HEAD
        f1 = f1 + MathHelper.sin((playerIn.prevDistanceWalkedModified + (playerIn.distanceWalkedModified - playerIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * realYaw;

        if (playerIn.isSneaking()) {
            f1 += 25.0F;
        }

        TestAValue.ToDraw[0] = "f1: " + f1;
        TestAValue.ToDraw[1] = "f2: " + f2;
        TestAValue.ToDraw[2] = "f3: " + f3;
        TestAValue.ToDraw[3] = "d3: " + d3;
        TestAValue.ToDraw[4] = "d4: " + d4;

        if (flag1) {

            f1 = f1 * 0.5F;
            f2 = f2 * 0.5F;
            f3 = f3 * 0.5F;
//            f2 = 0;


//            f3 = 0;
        }


        // THIS PART IS MINE
        // temporality is the actual rotate value, 6 at rest 0 at flaccid, ~165 at erect
        float temporary = 6.0F + f2 / 2.0F + f1;

        if (temporary >= 90f && enabled) {
//            temporary = 90F;

            temporary = (float) (temporary + (Math.log(temporary) * 5));
        }

        // END OF MINE


        GlStateManager.rotate(temporary, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        playerRenderer.getMainModel().renderCape(0.0625F);
        GlStateManager.popMatrix();
    }

    @Override
    public void onEnable() {
        enabled = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        enabled = false;
        super.onDisable();
    }
}
