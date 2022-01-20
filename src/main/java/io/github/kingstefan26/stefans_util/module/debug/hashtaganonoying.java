/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.prototypeModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class hashtaganonoying extends prototypeModule {
    public hashtaganonoying() {
        super("hashtaganonoying");
    }

    /**
     * Draws the entity to the screen. Args: xPos, yPos, scale, mouseX, mouseY, entityLiving
     */
    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float) Math.atan((double) (mouseX / 40.0F)) * 20.0F;
        ent.rotationYaw = (float) Math.atan((double) (mouseX / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    @Override
    protected void PROTOTYPETEST() {
        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("stefan_util:annoying-imp"), 0.8F));
//        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("stefan_util:annoying"), 0.8F));
//        try {
//            Thread.sleep(30);
//            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("stefan_util:annoying"), 0.79F));
//            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("stefan_util:annoying"), 0.83F));
//            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("stefan_util:annoying"), 0.701F));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        mc.displayGuiScreen(new GuiScreen() {
            @Override
            public void drawScreen(int mouseX, int mouseY, float partialTicks) {
                drawEntityOnScreen(this.width / 2, (this.height / 2) + (this.height / 8), 40, (float) (this.width / 2) - mouseX, (float) ((this.height / 2) + (this.height / 8) - 65) - mouseY, this.mc.thePlayer);

                super.drawScreen(mouseX, mouseY, partialTicks);
            }
        });

    }

    @Override
    public void onWorldRender(RenderWorldLastEvent e) {
//        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//        drawEntityOnScreen(1, 1, 30, -100000, -1000000000, mc.thePlayer);
        drawEntityOnScreen(192, 85, 30, (float) 192, (float) 85, mc.thePlayer);

        super.onWorldRender(e);
    }

}
