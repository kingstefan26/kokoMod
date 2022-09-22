package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.Main;
import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.module.modulemenagers.ModuleManager;
import io.github.kingstefan26.stefans_util.module.macro.util.cropType;
import io.github.kingstefan26.stefans_util.module.macro.util.macroStages;
import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import io.github.kingstefan26.stefans_util.util.StefanutilUtil;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class LastLeftOff extends BasicModule {
	public static LastLeftOff lastLeftOffinstance;
	private static lastleftoffObject lastLeftOff;

	public LastLeftOff() {
		super("lastLeftOff",
				"shows where you last left a macro in current session",
				ModuleManager.Category.MISC,
				new presistanceDecorator());
		lastLeftOffinstance = this;
	}

	public static LastLeftOff getLastLeftOff() {
		if (lastLeftOffinstance == null) lastLeftOffinstance = new LastLeftOff();
		return lastLeftOffinstance;
	}

	configObject x;
	configObject y;
	configObject z;
	configObject croptype;
	configObject macrostage;
	configObject time;

	public static void nullLastLeftOff() {
		lastLeftOff = null;
	}

	@Override
	public void onLoad() {
		x = new configObject("x", this.getName(), 0.0F);
		y = new configObject("y", this.getName(), 0.5F);
		z = new configObject("z", this.getName(), 0.5F);
		croptype = new configObject("cropType", this.getName(), "DEFAULT");
		macrostage = new configObject("macrostage", this.getName(), "DEFAULT");
		time = new configObject("time", this.getName(), 0.5F);

		if (x.getDoubleValue() != 0.0F) {
			if (time.getDoubleValue() != 0.5F) {
				final lastleftoffObject temp = new lastleftoffObject(
						(float) x.getDoubleValue(),
						(float) y.getDoubleValue(),
						(float) z.getDoubleValue(),
						cropType.valueOf(croptype.getStringValue()),
						macroStages.valueOf(macrostage.getStringValue()),
						(long) time.getDoubleValue());
				registerLastLeftOff(temp);
			} else {
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
		lastLeftOff = in;
		x.setDoubleValue(in.x);
		y.setDoubleValue(in.y);
		z.setDoubleValue(in.z);
		croptype.setStringValue(in.getCropType().toString());
		macrostage.setStringValue(in.getMacroStage().toString());
		if (in.time != 0) {
			time.setDoubleValue(in.time);
		}
		//float x,float y,float z,cropType type, macroStages macroStage, long time
	}

	public lastleftoffObject getLastleftoffObject(){
		return lastLeftOff;
	}

	@SubscribeEvent
	public void onRenderLast(RenderWorldLastEvent event) {
		if (lastLeftOff == null) return;
		if (!Main.isDebug()) {
			if (!WorldInfoService.isOnPrivateIsland()) return;
		}


		Entity viewer = mc.getRenderViewEntity();
		double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
		double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
		double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;

		int rgb;
		int textrgb;
		switch (io.github.kingstefan26.stefans_util.module.render.LastLeftOff.lastLeftOff.getCropType()) {
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
				throw new IllegalStateException("Unexpected value: " + io.github.kingstefan26.stefans_util.module.render.LastLeftOff.lastLeftOff.getCropType());
		}

		GlStateManager.disableDepth();
		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();

		//stolen from nue :)
		float xx = lastLeftOff.getX() - (float) viewerX;
		float yy = lastLeftOff.getY() - (float) viewerY;
		float zz = lastLeftOff.getZ() - (float) viewerZ;

		float distSq = xx * xx + yy * yy + zz * zz;

		if (lastLeftOff.getTime() != 0) {
			if (distSq > 4 * 4) {

				String text = (lastLeftOff.type == cropType.WART ? "wart" : "deafult") + " " + StefanutilUtil.ConvertMilliSecondsToFormattedDate(lastLeftOff.getTime());

				hehe.drawTextAtWorld(text,
						lastLeftOff.getX() + 0.5F,
						lastLeftOff.getY() + 1,
						lastLeftOff.getZ() + 0.5F,
						rgb, 2F,
						true, true, event.partialTicks);
			}
		}

		double x = lastLeftOff.getX() - viewerX;
		double y = lastLeftOff.getY() - viewerY;
		double z = lastLeftOff.getZ() - viewerZ;
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

	public static class lastleftoffObject {
		float x;
		float y;
		float z;
		long time;
		macroStages lastStage;
		cropType type;

		public lastleftoffObject(float x, float y, float z, cropType type, macroStages macroStage) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.type = type;
			this.lastStage = macroStage;
		}

		public lastleftoffObject(float x, float y, float z, cropType type, macroStages macroStage, long time) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.time = time;
			this.type = type;
			this.lastStage = macroStage;
		}

		public float getX() {
			return this.x;
		}

		public float getY() {
			return this.y;
		}

		public float getZ() {
			return this.z;
		}

		public long getTime() {
			return this.time;
		}

		public cropType getCropType() {
			return this.type;
		}

		public macroStages getMacroStage() {
			return this.lastStage;
		}
	}

}