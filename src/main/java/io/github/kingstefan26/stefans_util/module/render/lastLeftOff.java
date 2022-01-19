package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.module.macro.util.cropType;
import io.github.kingstefan26.stefans_util.module.macro.util.macroStages;
import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import io.github.kingstefan26.stefans_util.util.CalendarUtils;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class lastLeftOff extends basicModule {
	private static lastleftoffObject LastLeftOff;
	public static lastLeftOff LastLeftOffinstance;

	public static lastLeftOff getLastLeftOff(){
		if(LastLeftOffinstance == null) LastLeftOffinstance = new lastLeftOff();
		return LastLeftOffinstance;
	}

	public static class lastleftoffObject {
		float x;
		float y;
		float z;
		long time;
		macroStages lastStage;
		cropType type;
		public lastleftoffObject(float x,float y,float z,cropType type, macroStages macroStage){
			this.x = x;
			this.y = y;
			this.z = z;
			this.type = type;
			this.lastStage = macroStage;
		}
		public lastleftoffObject(float x,float y,float z,cropType type, macroStages macroStage, long time){
			this.x = x;
			this.y = y;
			this.z = z;
			this.time = time;
			this.type = type;
			this.lastStage = macroStage;
		}
		public float getX() {return this.x;}
		public float getY() {return this.y;}
		public float getZ() {return this.z;}
		public long getTime() {return this.time;}
		public cropType getCropType() {return this.type;}
		public macroStages getMacroStage(){return this.lastStage;}
	}

	configObject x;
	configObject y;
	configObject z;
	configObject croptype;
	configObject macrostage;
	configObject time;

	public lastLeftOff() {
		super("lastLeftOff", "shows where you last left a macro in current session", moduleManager.Category.MISC, new presistanceDecorator());
		LastLeftOffinstance = this;
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
				 final lastleftoffObject temp = new lastleftoffObject(
						(float) x.getDoubleValue(),
						(float) y.getDoubleValue(),
						(float) z.getDoubleValue(),
						cropType.valueOf(croptype.getStringValue()),
						macroStages.valueOf(macrostage.getStringValue()),
						(long) time.getDoubleValue());
				registerLastLeftOff(temp);
			}else{
				final lastleftoffObject temp2 = new lastleftoffObject(
						(float) x.getDoubleValue(),
						(float) y.getDoubleValue(),
						(float) z.getDoubleValue(),
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

	public lastleftoffObject getLastleftoffObject(){
		return LastLeftOff;
	}

	public static void nullLastLeftOff() {
		LastLeftOff = null;
	}



	@SubscribeEvent
	public void onRenderLast(RenderWorldLastEvent event) {
		if (LastLeftOff == null) return;
		if (!main.debug) {
			if (!WorldInfoService.isOnPrivateIsland()) return;
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
 		float xx = LastLeftOff.getX() - (float) viewerX;
		float yy = LastLeftOff.getY() - (float) viewerY;
		float zz = LastLeftOff.getZ() - (float)viewerZ;

		float distSq = xx*xx+ yy*yy + zz*zz;

		if(LastLeftOff.getTime() != 0){
			if(distSq > 4*4) {

				String text = (LastLeftOff.type == cropType.WART ? "wart" : "deafult") + " " + CalendarUtils.ConvertMilliSecondsToFormattedDate(LastLeftOff.getTime());

				hehe.drawTextAtWorld(text,
						LastLeftOff.getX() + 0.5F,
						LastLeftOff.getY() + 1,
						LastLeftOff.getZ() + 0.5F,
						rgb, 2F,
						true, true, event.partialTicks);
			}
		}

		double x = LastLeftOff.getX() - viewerX;
		double y = LastLeftOff.getY() - viewerY;
		double z = LastLeftOff.getZ() - viewerZ;
		AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);

		if (distSq < 4 * 4) {
			hehe.drawFilledBoundingBox(bb, 1f, textrgb);
		}
//		else{
//			Vec3 pos3;
//			Vec3 pos4;
//			pos3 = new Vec3(LastLeftOff.getX(),LastLeftOff.getY(),LastLeftOff.getZ());
//			pos4 = new Vec3(viewerX,viewerY + 1,viewerZ);
////			draw3DLine(pos3,pos4, rgb, 5, false, event.partialTicks);
//		}

		GlStateManager.disableLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
	}

}