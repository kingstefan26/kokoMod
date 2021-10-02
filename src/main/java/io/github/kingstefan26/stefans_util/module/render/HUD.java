package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.core.config.confgValueType;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.module.moduleRegistery;
import io.github.kingstefan26.stefans_util.module.moduleIndex;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import io.github.kingstefan26.stefans_util.util.CustomFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;

public class HUD extends Module {

	int r,g,b;
	int rgb;
	CustomFont c = new CustomFont(Minecraft.getMinecraft(), new Font("JetBrains Mono", Font.PLAIN, 15), 15);
	CustomFont aaa = new CustomFont(Minecraft.getMinecraft(), "JetBrains Mono",2020);

	public HUD() {
		super("HUD", "Draws the module list on your screen", ModuleManager.Category.RENDER);
		this.presistanceEnabled = true;
	}

	@Override
	public void onLoad(){
		SettingsManager.getSettingsManager().rSetting(new Setting("R",this, 255, 0, 255,true));
		SettingsManager.getSettingsManager().rSetting(new Setting("G",this, 255, 0, 255,true));
		SettingsManager.getSettingsManager().rSetting(new Setting("B",this, 255, 0, 255,true));
		super.onLoad();
	}

	private void updateVals(){
		this.r = SettingsManager.getSettingsManager().getSettingByName("R",this).getValInt();
		this.g = SettingsManager.getSettingsManager().getSettingByName("G",this).getValInt();
		this.b = SettingsManager.getSettingsManager().getSettingByName("B",this).getValInt();
		rgb = r;
		rgb = (rgb << 8) + g;
		rgb = (rgb << 8) + b;
	}

	@Override
	public void onEnable(){
		super.onEnable();
		this.updateVals();
	}

	@Override
	public void onGuiRender(float partialTicks, ScaledResolution resolution, RenderGameOverlayEvent.ElementType type) {
		if (this.closed || !this.isToggled()) return;
		if (type == RenderGameOverlayEvent.ElementType.TEXT) {
			ScaledResolution sraka = new ScaledResolution(Minecraft.getMinecraft());
			int temp = 2;
			for (Module mod : moduleIndex.getmoduleIndex().getAllModules()) {
				if (mod.isToggled()) {
					c.drawStringS(mod.getName(), (sraka.getScaledWidth() * 2) - c.getStringWidth(mod.getName()) - 1, temp, 0xFFccFFFF);
					temp += c.getStringHeight(mod.getName()) + 1;
				}
			}
			for (Module mod : moduleRegistery.getModuleRegistery().loadedModules) {
				if (mod.isToggled()) {
					c.drawStringS(mod.getName(), (sraka.getScaledWidth() * 2) - c.getStringWidth(mod.getName()) - 1, temp, 0xFFccFFFF);
					temp += c.getStringHeight(mod.getName()) + 1;
				}
			}
		}
	}


//	@SubscribeEvent
//	public void onRender(RenderGameOverlayEvent e) {
//		if(this.closed || !this.isToggled()) return;
//		if(e.type == RenderGameOverlayEvent.ElementType.TEXT){
//			ScaledResolution sraka = new ScaledResolution(Minecraft.getMinecraft());
//			int temp = 2;
//			for (Module mod : moduleIndex.getmoduleIndex().getAllModules()) {
//				if(mod.isToggled()){
//					aaa.drawStringS(mod.getName(), (sraka.getScaledWidth() * 2) - aaa.getStringWidth(mod.getName()) - 1, temp, 0xFFccFFFF);
//					temp += aaa.getStringHeight(mod.getName()) + 1;
//				}
//			}
//			for(Module mod : moduleRegistery.getModuleRegistery().loadedModules){
//				if(mod.isToggled()){
//					aaa.drawStringS(mod.getName(), (sraka.getScaledWidth() * 2) - aaa.getStringWidth(mod.getName()) - 1, temp, 0xFFccFFFF);
//					temp += aaa.getStringHeight(mod.getName()) + 1;
//				}
//			}
//			//c.drawStringS("omg ur so fab!", 1, 20, -1);
//
//		}
////
////
////		if(e.type == RenderGameOverlayEvent.ElementType.TEXT|| !this.isToggled()){
////			//CustomFont c = new CustomFont(Minecraft.getMinecraft(), new Font("JetBrains Mono", Font.BOLD, 20), 20);
////			ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
////			FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
////			int y = 2;
////			int y1 = 2;
////			for (Module mod : moduleIndex.getmoduleIndex().getAllModules()) {
////				if (!mod.getName().equalsIgnoreCase("HUD") && mod.isToggled() && mod.getVisibility()) {
////					fr.drawString(mod.getName(), sr.getScaledWidth() - fr.getStringWidth(mod.getName()) - 1, y, rgb, true);
////					c.drawStringS(mod.getName(), sr.getScaledWidth() - c.getStringWidth(mod.getName()) - 1, y1, rgb);
////					y += fr.FONT_HEIGHT;
////					y1 += c.getStringHeight(mod.getName());
////				}
////			}
////			for(Module mod : moduleRegistery.getModuleRegistery().loadedModules){
////				if (!mod.getName().equalsIgnoreCase("HUD") && mod.isToggled() && mod.getVisibility()) {
////					fr.drawString(mod.getName(), sr.getScaledWidth() - fr.getStringWidth(mod.getName()) - 1, y, rgb, true);
////					c.drawStringS(mod.getName(), sr.getScaledWidth() - c.getStringWidth(mod.getName()) - 1, y1, rgb);
////					y += fr.FONT_HEIGHT;
////					y1 += c.getStringHeight(mod.getName());
////				}
////			}
////
////		}
//////		if (!e.type.equals(RenderGameOverlayEvent.ElementType.CROSSHAIRS)|| !this.isToggled()) {
//////			return;
//////		}
//
//	}
}
