package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class testTracer extends Module {
    public testTracer() {
        super("test tracer", "yazz", ModuleManager.Category.DEBUG);
        SettingsManager.getSettingsManager().rSetting(new Setting("boxes", this, true));
    }

    boolean a;

    @Override
    public void onEnable(){
        super.onEnable();
        a = SettingsManager.getSettingsManager().getSettingByName("boxes", this).getValBoolean();
    }

    @SubscribeEvent
    public void RenderLivingEvent(RenderWorldLastEvent event) {
        if (this.isToggled()) {
            //drawTracerLine(event.partialTicks);
            for (EntityPlayer ed : mc.theWorld.playerEntities) {
                drawTracer(event.partialTicks, ed, Minecraft.getMinecraft().getRenderViewEntity());
                if(a){
                    drawBoundingBox(event.partialTicks, ed, Minecraft.getMinecraft().getRenderViewEntity());
                }
            }
        }

    }

    private void drawTracer(float partialTick, EntityPlayer e, Entity viewer) {

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTick;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTick;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTick;

        if (e != mc.thePlayer && e != null && e.isEntityAlive()) {
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) partialTick - viewerX;
            double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) partialTick - viewerY;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) partialTick - viewerZ;
            double distance = mc.thePlayer.getDistanceToEntity(e);

            GL11.glBlendFunc(770, 771);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            if (e.isSneaking()) {
                GL11.glColor3f(255F, 255F, 0f);
            } else if (distance <= 50) {
                GL11.glColor4f(153F, (float) distance / 45, 0F, 153F);
            } else {
                GL11.glColor3f(255F, 255F, 255f);
            }

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(1F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2d(0.0D, 0.0D);
            GL11.glVertex3d(x, y, z);
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public static void drawBoundingBox(float partialTick, EntityPlayer e, Entity viewer) {
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTick;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTick;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTick;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        //GL11.glLineWidth(1f);
        GL11.glDepthMask(false);


        double x = e.posX - viewerX;
        double z = e.posZ - viewerZ;
        double y = e.posY - viewerY;
        double height = y + e.height + 0.2;
        double correctMeasureX = x + 0.4;
        double correctMeasureZ = z + 0.4;

        if (e != mc.thePlayer && e.isEntityAlive()) {

            GL11.glColor3f(0F, 4F, 11F);

            if (e.hurtTime > 0) {
                GL11.glColor3f(1F, 0F, 0F);
            } else if (e.isSneaking()) {
                GL11.glColor3f(255f, 255f, 0f);
            } else {
                GL11.glColor3f(0f, 0.75f, 0f);
            }

            drawOutlinedBoundingBox(new AxisAlignedBB(correctMeasureX - 0.8, y, correctMeasureZ - 0.8, correctMeasureX, height, correctMeasureZ));
        }

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDepthMask(true);
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB) {
        GL11.glLineWidth(0.75F);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        GL11.glVertex3d(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        GL11.glVertex3d(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        GL11.glVertex3d(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        GL11.glVertex3d(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        GL11.glVertex3d(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        GL11.glVertex3d(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        GL11.glEnd();
    }
}
