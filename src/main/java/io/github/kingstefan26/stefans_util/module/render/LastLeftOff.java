package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.Main;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.module.modulemenagers.ModuleManager;
import io.github.kingstefan26.stefans_util.core.newconfig.attotations.impl.DoubleConfigValue;
import io.github.kingstefan26.stefans_util.core.newconfig.attotations.impl.StringConfigValue;
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
	private static LastLeftOff lastLeftOffinstance;
	@DoubleConfigValue(name = "lastleftoffx", defaultValue = 0)
	double lastleftoffx;
	@DoubleConfigValue(name = "lastleftoffy", defaultValue = 0)
	double lastleftoffy;
	@DoubleConfigValue(name = "lastleftoffz", defaultValue = 0)
	double lastleftoffz;
	@StringConfigValue(name = "lastleftoffcroptype", defaultValue = "DEFAULT")
	String lastleftoffcroptype;
	@StringConfigValue(name = "lastleftoffmacrostage", defaultValue = "DEFAULT")
	String lastleftoffmacrostage;
	@DoubleConfigValue(name = "lastleftofftime", defaultValue = 0)
	double lastleftofftime;
	private LastleftoffObject obj;

	public LastLeftOff() {
		super("lastLeftOff",
				"shows where you last left a macro in current session",
				ModuleManager.Category.MISC,
				new presistanceDecorator());
		setLastLeftOffinstance(this);
	}

	public static LastLeftOff getLastLeftOff() {
		if (getLastLeftOffinstance() == null) setLastLeftOffinstance(new LastLeftOff());
		return getLastLeftOffinstance();
	}

	public static void nullLastLeftOff() {
		getLastLeftOffinstance().obj = null;
	}

	public static LastLeftOff getLastLeftOffinstance() {
		return lastLeftOffinstance;
	}

	public static void setLastLeftOffinstance(LastLeftOff lastLeftOffinstance) {
		LastLeftOff.lastLeftOffinstance = lastLeftOffinstance;
	}

	@Override
	public void onLoad() {

		if (lastleftoffx != 0.0F) {
			if (lastleftofftime != 0.5F) {
				final LastleftoffObject temp = new LastleftoffObject(
						(float) lastleftoffx,
						(float) lastleftoffy,
						(float) lastleftoffz,
						cropType.valueOf(lastleftoffcroptype),
						macroStages.valueOf(lastleftoffmacrostage),
						(long) lastleftofftime);
				registerLastLeftOff(temp);
			} else {
				final LastleftoffObject temp2 = new LastleftoffObject(
						(float) lastleftoffx,
						(float) lastleftoffy,
						(float) lastleftoffz,
						cropType.valueOf(lastleftoffcroptype),
						macroStages.valueOf(lastleftoffmacrostage));
				registerLastLeftOff(temp2);
			}

		}
		super.onLoad();
	}

	public void registerLastLeftOff(LastleftoffObject in) {
		obj = in;
		lastleftoffx = in.x;
		lastleftoffy = in.y;
		lastleftoffz = in.z;
		lastleftoffcroptype = in.getCropType().toString();
		lastleftoffmacrostage = in.getMacroStage().toString();
		if (in.time != 0) {
			lastleftofftime = in.time;
		}
		//float x,float y,float z,cropType type, macroStages macroStage, long time
	}

	public LastleftoffObject getLastleftoffObject() {
		return obj;
	}

	@SubscribeEvent
	public void onRenderLast(RenderWorldLastEvent event) {
		if (obj == null) return;
		if (!Main.isDebug() && !WorldInfoService.isOnPrivateIsland()) {
			return;
		}


		Entity viewer = mc.getRenderViewEntity();
		double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
		double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
		double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;

		int rgb;
		int textrgb;
		switch (obj.getCropType()) {
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
				throw new IllegalStateException("Unexpected value: " + obj.getCropType());
		}

		GlStateManager.disableDepth();
		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();

		//stolen from nue :)
		float xx = obj.getX() - (float) viewerX;
		float yy = obj.getY() - (float) viewerY;
		float zz = obj.getZ() - (float) viewerZ;

		float distSq = xx * xx + yy * yy + zz * zz;

		if (obj.getTime() != 0 && distSq > 4 * 4) {


			String text = (obj.type == cropType.WART ? "wart" : "deafult") + " " + StefanutilUtil.ConvertMilliSecondsToFormattedDate(obj.getTime());

			hehe.drawTextAtWorld(text,
					obj.getX() + 0.5F,
					obj.getY() + 1,
					obj.getZ() + 0.5F,
					rgb, 2F,
					true, true, event.partialTicks);

		}

		double x = obj.getX() - viewerX;
		double y = obj.getY() - viewerY;
		double z = obj.getZ() - viewerZ;
		AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);

		if (distSq < 4 * 4) {
			hehe.drawFilledBoundingBox(bb, 1f, textrgb);
		}

		GlStateManager.disableLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
	}

	public static class LastleftoffObject {
		float x;
		float y;
		float z;
		long time;
		macroStages lastStage;
		cropType type;

		public LastleftoffObject(float x, float y, float z, cropType type, macroStages macroStage) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.type = type;
			this.lastStage = macroStage;
		}

		public LastleftoffObject(float x, float y, float z, cropType type, macroStages macroStage, long time) {
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