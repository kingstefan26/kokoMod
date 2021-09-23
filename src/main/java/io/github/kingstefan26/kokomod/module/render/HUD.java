package io.github.kingstefan26.kokomod.module.render;

import com.google.common.base.Throwables;
import io.github.kingstefan26.kokomod.core.config.confgValueType;
import io.github.kingstefan26.kokomod.core.module.Category;
import io.github.kingstefan26.kokomod.core.module.blueprints.Module;
import io.github.kingstefan26.kokomod.core.module.blueprints.UtilModule;
import io.github.kingstefan26.kokomod.module.moduleIndex;
import io.github.kingstefan26.kokomod.core.setting.Setting;
import io.github.kingstefan26.kokomod.core.setting.SettingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.List;

public class HUD extends Module {

	int r,g,b;
	int rgb;

	public HUD() {
		super("HUD", "Draws the module list on your screen", Category.RENDER);
		SettingsManager.getSettingsManager().rSetting(new Setting("R",this, 255, 0, 255,true));
		SettingsManager.getSettingsManager().rSetting(new Setting("G",this, 255, 0, 255,true));
		SettingsManager.getSettingsManager().rSetting(new Setting("B",this, 255, 0, 255,true));
		SettingsManager.getSettingsManager().rSetting(new Setting("PERSISTENCE", this, confgValueType.PERSISTENT));
		if(SettingsManager.getSettingsManager().getSettingByName("PERSISTENCE", this).getValBoolean()) this.toggle();

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
	
	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent e) {
		if (!e.type.equals(RenderGameOverlayEvent.ElementType.CROSSHAIRS)|| !this.isToggled()) {
			return;
		}
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int y = 2;
		for (Module mod : moduleIndex.getmoduleIndex().getAllModules()) {
			if (!mod.getName().equalsIgnoreCase("HUD") && mod.isToggled() && mod.getVisibility()) {
				FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
				fr.drawString(mod.getName(), sr.getScaledWidth() - fr.getStringWidth(mod.getName()) - 1, y, rgb, true);
				y += fr.FONT_HEIGHT;
			}
		}
	}
}
