package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.visibleDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.module.modulemenagers.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.modulemenagers.moduleRegistery;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.util.CustomFont;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;
import java.util.ArrayList;

public class HUD extends BasicModule {

    int rgb, r, g, b;
    CustomFont c = new CustomFont(new Font("JetBrains Mono", Font.PLAIN, 15), 15);
    public static ArrayList<visibleDecorator> visibleDecorators = new ArrayList<>();

    public HUD() {
        super("HUD", "Draws the module list on your screen", ModuleManager.Category.RENDER);
    }

    @Override
    public void onLoad() {
        new SliderNoDecimalSetting("R", this, 10, 1, 50, (newvalue) -> {
            this.r = Math.toIntExact(Math.round(newvalue));
        });
        new SliderNoDecimalSetting("G", this, 10, 1, 50, (newvalue) -> {
            this.g = Math.toIntExact(Math.round(newvalue));
        });
        new SliderNoDecimalSetting("B", this, 10, 1, 50, (newvalue) -> {
            this.b = Math.toIntExact(Math.round(newvalue));
        });
        super.onLoad();
    }

    private void updateVals() {
        rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;
        logger.info("rgb: " + rgb + " hex: " + 0xFFccFFFF);
    }

    @Override
    public void onEnable() {
        this.updateVals();
        super.onEnable();
    }

    @Override
    public void onGuiRender(RenderGameOverlayEvent e) {
        if (this.isClosed() || !this.isToggled()) return;
        if (e.type == RenderGameOverlayEvent.ElementType.TEXT) {
            ScaledResolution sraka = new ScaledResolution(mc);
            int temp = 2;
            for (BasicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
                if (!m.isToggled()) continue;
                if (m.getName().equals("HUD")) continue;
                if (m.getLocalDecoratorManager().visibleDecorator != null) {
                    if (m.getLocalDecoratorManager().visibleDecorator.isVisibilityEnabled()) {
                        c.drawString(m.getName(), (sraka.getScaledWidth() * 2) - c.getStringWidth(m.getName()) - 1, temp, -1);
                        temp += c.getStringHeight(m.getName()) + 1;
                    }
                } else {
                    c.drawString(m.getName(), (sraka.getScaledWidth() * 2) - c.getStringWidth(m.getName()) - 1, temp, -1);
                    temp += c.getStringHeight(m.getName()) + 1;
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
