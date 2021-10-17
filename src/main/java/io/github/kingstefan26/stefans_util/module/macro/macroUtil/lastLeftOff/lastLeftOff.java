package io.github.kingstefan26.stefans_util.module.macro.macroUtil.lastLeftOff;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.module.macro.macroUtil.cropType;
import io.github.kingstefan26.stefans_util.module.macro.macroUtil.macroStages;
import io.github.kingstefan26.stefans_util.module.util.SBinfo;
import io.github.kingstefan26.stefans_util.util.CalendarUtils;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static io.github.kingstefan26.stefans_util.util.renderUtil.draw3Dline.draw3DLine;


public class lastLeftOff extends Module {
	public static lastleftoffObject LastLeftOff;
	public static lastLeftOff LastLeftOffinstance;

	public static lastLeftOff getLastLeftOff(){
		if(LastLeftOffinstance == null) LastLeftOffinstance = new lastLeftOff();
		return LastLeftOffinstance;
	}

	configObject x;
	configObject y;
	configObject z;
	configObject croptype;
	configObject macrostage;
	configObject time;

	public lastLeftOff() {
		super("lastLeftOff", "shows where you last left a macro in current session", ModuleManager.Category.MISC);
		LastLeftOffinstance = this;
		this.presistanceEnabled = true;
	}
	@Override
	public void onLoad(){
		x = new configObject("x", this.getName(), 0.0F);
		y = new configObject("y", this.getName(), 0.5F);
		z = new configObject("z", this.getName(), 0.5F);
		croptype= new configObject("cropType", this.getName(), "DEFAULT");
		macrostage= new configObject("macrostage", this.getName(), "DEFAULT");
		time = new configObject("time", this.getName(), 0.5F);

		if(x.getDoubleValue() != 0.0F){
			if(time.getDoubleValue() != 0.5F){
				lastleftoffObject temp = new lastleftoffObject(
						(float)x.getDoubleValue(),
						(float)y.getDoubleValue(),
						(float)z.getDoubleValue(),
						cropType.valueOf(croptype.getStringValue()),
						macroStages.valueOf(macrostage.getStringValue()),
						(long)time.getDoubleValue());
				registerLastLeftOff(temp);
			}else{
				lastleftoffObject temp2 = new lastleftoffObject(
						(float)x.getDoubleValue(),
						(float)y.getDoubleValue(),
						(float)z.getDoubleValue(),
						cropType.valueOf(croptype.getStringValue()),
						macroStages.valueOf(macrostage.getStringValue()));
				registerLastLeftOff(temp2);
			}

		}
		super.onLoad();
	}

	public void registerLastLeftOff(lastleftoffObject in) {
		LastLeftOff = in;
		x.setDoubleValue(in.x);
		y.setDoubleValue(in.y);
		z.setDoubleValue(in.z);
		croptype.setStringValue(in.getCropType().toString());
		macrostage.setStringValue(in.getMacroStage().toString());
		if(in.time != 0){
			time.setDoubleValue(in.time);
		}
		//float x,float y,float z,cropType type, macroStages macroStage, long time
	}

	public static lastleftoffObject getLastleftoffObject(){return LastLeftOff;}

	public static void nullLastLeftOff() {
		LastLeftOff = null;
	}


	private static final ResourceLocation beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");

	private static void renderBeaconBeam(double x, double y, double z, int rgb, float alphaMult, float partialTicks) {
		int height = 300;
		int bottomOffset = 0;
		int topOffset = bottomOffset + height;

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		Minecraft.getMinecraft().getTextureManager().bindTexture(beaconBeam);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
		GlStateManager.disableLighting();
		GlStateManager.enableCull();
		GlStateManager.enableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

		double time = Minecraft.getMinecraft().theWorld.getTotalWorldTime() + (double) partialTicks;
		double d1 = MathHelper.func_181162_h(-time * 0.2D - (double) MathHelper.floor_double(-time * 0.1D));

		float r = ((rgb >> 16) & 0xFF) / 255f;
		float g = ((rgb >> 8) & 0xFF) / 255f;
		float b = (rgb & 0xFF) / 255f;
		double d2 = time * 0.025D * -1.5D;
		double d4 = 0.5D + Math.cos(d2 + 2.356194490192345D) * 0.2D;
		double d5 = 0.5D + Math.sin(d2 + 2.356194490192345D) * 0.2D;
		double d6 = 0.5D + Math.cos(d2 + (Math.PI / 4D)) * 0.2D;
		double d7 = 0.5D + Math.sin(d2 + (Math.PI / 4D)) * 0.2D;
		double d8 = 0.5D + Math.cos(d2 + 3.9269908169872414D) * 0.2D;
		double d9 = 0.5D + Math.sin(d2 + 3.9269908169872414D) * 0.2D;
		double d10 = 0.5D + Math.cos(d2 + 5.497787143782138D) * 0.2D;
		double d11 = 0.5D + Math.sin(d2 + 5.497787143782138D) * 0.2D;
		double d14 = -1.0D + d1;
		double d15 = (double) (height) * 2.5D + d14;
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(1.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
		worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
		worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
		worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(0.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
		worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(1.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
		worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
		worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
		worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(0.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
		worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(1.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
		worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
		worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
		worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(0.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
		worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(1.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
		worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
		worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
		worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(0.0D, d15).color(r, g, b, 1.0F * alphaMult).endVertex();
		tessellator.draw();

		GlStateManager.disableCull();
		double d12 = -1.0D + d1;
		double d13 = height + d12;

		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.2D).tex(1.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
		worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.2D).tex(1.0D, d12).color(r, g, b, 0.25F).endVertex();
		worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.2D).tex(0.0D, d12).color(r, g, b, 0.25F).endVertex();
		worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.2D).tex(0.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
		worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.8D).tex(1.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
		worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.8D).tex(1.0D, d12).color(r, g, b, 0.25F).endVertex();
		worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.8D).tex(0.0D, d12).color(r, g, b, 0.25F).endVertex();
		worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.8D).tex(0.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
		worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.2D).tex(1.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
		worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.2D).tex(1.0D, d12).color(r, g, b, 0.25F).endVertex();
		worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.8D).tex(0.0D, d12).color(r, g, b, 0.25F).endVertex();
		worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.8D).tex(0.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
		worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.8D).tex(1.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
		worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.8D).tex(1.0D, d12).color(r, g, b, 0.25F).endVertex();
		worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.2D).tex(0.0D, d12).color(r, g, b, 0.25F).endVertex();
		worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.2D).tex(0.0D, d13).color(r, g, b, 0.25F * alphaMult).endVertex();
		tessellator.draw();
	}

	@SubscribeEvent
	public void onRenderLast(RenderWorldLastEvent event) {
		if (LastLeftOff == null) return;
		if (!main.debug) {
			if (!SBinfo.isOnPrivateIsland()) return;
		}


		Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
		double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
		double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
		double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;

		int rgb;
		int textrgb;
		switch(lastLeftOff.LastLeftOff.getCropType()){
			case WART:
				rgb = 0xa02427;
				textrgb = 0x3b0c0d;
				break;
			case CANE:
				rgb = 0x51a42f;
				textrgb = 0x1a360f;
				break;
			case DEFAULT:
				rgb = 0xa839ce;
				textrgb = 0x41184f;
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + lastLeftOff.LastLeftOff.getCropType());
		}

		GlStateManager.disableDepth();
		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();

		//stolen from nue :)
		float xx = LastLeftOff.getX() -(float) viewerX;
		float yy = LastLeftOff.getY() -(float) viewerY;
		float zz = LastLeftOff.getZ() - (float)viewerZ;

		float distSq = xx*xx+ yy*yy + zz*zz;

		if(LastLeftOff.getTime() != 0){
			if(distSq > 4*4){
				hehe.drawTextAtWorld(CalendarUtils.ConvertMilliSecondsToFormattedDate(LastLeftOff.getTime()),
						LastLeftOff.getX() + 0.5F,
						LastLeftOff.getY() + 1,
						LastLeftOff.getZ() + 0.5F,
						rgb, 3F,
						true, true, event.partialTicks);
			}

		}



		double x = LastLeftOff.getX() - viewerX;
		double y = LastLeftOff.getY() - viewerY;
		double z = LastLeftOff.getZ() - viewerZ;

		AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);

//		GlStateManager.disableDepth();

		if(distSq < 4*4){
			drawFilledBoundingBox(bb, 1f,textrgb);
		}else{
//			//line one
//			Vec3 pos1;
//			Vec3 pos2;
//			pos1 = new Vec3(viewerX,viewerY,viewerZ);
//			pos2 = new Vec3(LastLeftOff.getX(),viewerY,LastLeftOff.getZ());
//
//			//line two
//			Vec3 pos3;
//			Vec3 pos4;
//			pos3 = new Vec3(LastLeftOff.getX(),viewerY,LastLeftOff.getZ());
//			pos4 = new Vec3(LastLeftOff.getX(),LastLeftOff.getY(),LastLeftOff.getZ());
//
//			draw3DLine(pos1,pos2, rgb, 5, false, event.partialTicks);
//
//			draw3DLine(pos1,pos2, rgb, 5, false, event.partialTicks);
			Vec3 pos3;
			Vec3 pos4;
			pos3 = new Vec3(LastLeftOff.getX(),LastLeftOff.getY(),LastLeftOff.getZ());
			pos4 = new Vec3(viewerX,viewerY + 1,viewerZ);
			draw3DLine(pos3,pos4, rgb, 5, false, event.partialTicks);
		}


		//renderBeaconBeam(x, y, z, textrgb, 1.0f, event.partialTicks);


		GlStateManager.disableLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
	}

	public static void drawFilledBoundingBox(AxisAlignedBB p_181561_0_, float alpha, int rgb) {
		Color c = new Color(rgb);

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.disableTexture2D();
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
//		GlStateManager.depthMask(true);

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		GlStateManager.color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f * alpha);

		//vertical
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		tessellator.draw();


		GlStateManager.color(c.getRed() / 255f * 0.8f, c.getGreen() / 255f * 0.8f, c.getBlue() / 255f * 0.8f, c.getAlpha() / 255f * alpha);

		//x
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		tessellator.draw();


		GlStateManager.color(c.getRed() / 255f * 0.9f, c.getGreen() / 255f * 0.9f, c.getBlue() / 255f * 0.9f, c.getAlpha() / 255f * alpha);
		//z
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		tessellator.draw();
	}

}
