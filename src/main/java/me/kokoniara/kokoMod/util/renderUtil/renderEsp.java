package me.kokoniara.kokoMod.util.renderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

public class renderEsp {

    public static void drawbox(double x, double y, double z, RenderWorldLastEvent event) {

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) event.partialTicks;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) event.partialTicks;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) event.partialTicks;


        Vec3 pos = new Vec3(d0, d1, d2);
        Vec3 position = new Vec3(x, y, z);

        //Vec3 pos = event.player.getPositionEyes(event.partialTicks); Doesnt work because y-part +(double)this.getEyeHeight()

        GL11.glTranslated(-pos.xCoord, -pos.yCoord, -pos.zCoord);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);        // draw the line on top of the geometry

        //back bottom
        Vec3 posBB = new Vec3(position.xCoord - 0.5, position.yCoord - 0.5, position.zCoord - 0.5);
        Vec3 posBB1 = new Vec3(position.xCoord - 0.5, position.yCoord - 0.5, position.zCoord + 0.5);
        drawLineWithGL(posBB, posBB1);

        //back left
        Vec3 posBL = new Vec3(position.xCoord - 0.5, position.yCoord - 0.5, position.zCoord - 0.5);
        Vec3 posBL1 = new Vec3(position.xCoord - 0.5, position.yCoord + 0.5, position.zCoord - 0.5);
        drawLineWithGL(posBL, posBL1);

        //back right
        Vec3 posBR = new Vec3(position.xCoord - 0.5, position.yCoord - 0.5, position.zCoord + 0.5);
        Vec3 posBR1 = new Vec3(position.xCoord - 0.5, position.yCoord + 0.5, position.zCoord + 0.5);
        drawLineWithGL(posBR, posBR1);

        //back top

        Vec3 posBT = new Vec3(position.xCoord - 0.5, position.yCoord + 0.5, position.zCoord - 0.5);
        Vec3 posBT1 = new Vec3(position.xCoord - 0.5, position.yCoord + 0.5, position.zCoord + 0.5);
        drawLineWithGL(posBT, posBT1);

        //
        //

        //front bottom
        Vec3 posFB = new Vec3(position.xCoord + 0.5, position.yCoord - 0.5, position.zCoord - 0.5);
        Vec3 posFB1 = new Vec3(position.xCoord + 0.5, position.yCoord - 0.5, position.zCoord + 0.5);
        drawLineWithGL(posFB, posFB1);

        // front left
        Vec3 posFL = new Vec3(position.xCoord + 0.5, position.yCoord - 0.5, position.zCoord - 0.5);
        Vec3 posFL1 = new Vec3(position.xCoord + 0.5, position.yCoord + 0.5, position.zCoord - 0.5);
        drawLineWithGL(posFL, posFL1);

        //front right
        Vec3 posFR = new Vec3(position.xCoord + 0.5, position.yCoord - 0.5, position.zCoord + 0.5);
        Vec3 posFR1 = new Vec3(position.xCoord + 0.5, position.yCoord + 0.5, position.zCoord + 0.5);
        drawLineWithGL(posFR, posFR1);

        //front top
        Vec3 posFT = new Vec3(position.xCoord + 0.5, position.yCoord + 0.5, position.zCoord - 0.5);
        Vec3 posFT1 = new Vec3(position.xCoord + 0.5, position.yCoord + 0.5, position.zCoord + 0.5);
        drawLineWithGL(posFT, posFT1);

        //
        //

        //top back
        Vec3 posTB = new Vec3(position.xCoord - 0.5, position.yCoord - 0.5, position.zCoord - 0.5);
        Vec3 posTB1 = new Vec3(position.xCoord - 0.5, position.yCoord + 0.5, position.zCoord + 0.5);
        drawLineWithGL(posTB, posTB1);

        // top left
        Vec3 posTL = new Vec3(position.xCoord - 0.5, position.yCoord + 0.5, position.zCoord - 0.5);
        Vec3 posTL1 = new Vec3(position.xCoord + 0.5, position.yCoord + 0.5, position.zCoord - 0.5);
        drawLineWithGL(posTL, posTL1);

        //top right
        Vec3 posTR = new Vec3(position.xCoord - 0.5, position.yCoord + 0.5, position.zCoord + 0.5);
        Vec3 posTR1 = new Vec3(position.xCoord + 0.5, position.yCoord + 0.5, position.zCoord + 0.5);
        drawLineWithGL(posTR, posTR1);

        //front front
        Vec3 posTF = new Vec3(position.xCoord + 0.5, position.yCoord + 0.5, position.zCoord - 0.5);
        Vec3 posTF1 = new Vec3(position.xCoord + 0.5, position.yCoord + 0.5, position.zCoord + 0.5);
        drawLineWithGL(posTF, posTF1);

        //
        //

        //bottom back
        Vec3 posBB0 = new Vec3(position.xCoord - 0.5, position.yCoord - 0.5, position.zCoord - 0.5);
        Vec3 posBB01 = new Vec3(position.xCoord - 0.5, position.yCoord - 0.5, position.zCoord + 0.5);
        drawLineWithGL(posBB0, posBB01);

        // bottom left
        Vec3 posBL0 = new Vec3(position.xCoord - 0.5, position.yCoord - 0.5, position.zCoord - 0.5);
        Vec3 posBL01 = new Vec3(position.xCoord + 0.5, position.yCoord - 0.5, position.zCoord - 0.5);
        drawLineWithGL(posBL0, posBL01);

        //bottom right
        Vec3 posBR0 = new Vec3(position.xCoord - 0.5, position.yCoord - 0.5, position.zCoord + 0.5);
        Vec3 posBR01 = new Vec3(position.xCoord + 0.5, position.yCoord - 0.5, position.zCoord + 0.5);
        drawLineWithGL(posBR0, posBR01);

        //bottom front
        Vec3 posBF0 = new Vec3(position.xCoord + 0.5, position.yCoord - 0.5, position.zCoord - 0.5);
        Vec3 posBF01 = new Vec3(position.xCoord + 0.5, position.yCoord - 0.5, position.zCoord + 0.5);
        drawLineWithGL(posBF0, posBF01);

        //
        //


        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    private static void drawLineWithGL(Vec3 posA, Vec3 posB) {

        //255, 252, 0
        GL11.glColor3f(1F, 1F, 0F);  // change color an set alpha

        GL11.glBegin(GL11.GL_LINE_STRIP);

        GL11.glVertex3d(posA.xCoord, posA.yCoord, posA.zCoord);
        GL11.glVertex3d(posB.xCoord, posB.yCoord, posB.zCoord);

        GL11.glEnd();
    }

    private static void drawLineWithGL(Vec3 posA, Vec3 posB, float red, float green, float blue, float alpha) {

        //255, 252, 0
        GL11.glColor4f(red, green, blue, alpha);  // change color an set alpha

        GL11.glBegin(GL11.GL_LINE_STRIP);

        GL11.glVertex3d(posA.xCoord, posA.yCoord, posA.zCoord);
        GL11.glVertex3d(posB.xCoord, posB.yCoord, posB.zCoord);

        GL11.glEnd();
    }

}