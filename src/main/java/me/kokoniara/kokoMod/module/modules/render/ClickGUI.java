package me.kokoniara.kokoMod.module.modules.render;

import org.lwjgl.input.Keyboard;

import me.kokoniara.kokoMod.module.moduleUtil.module.Category;
import me.kokoniara.kokoMod.module.moduleUtil.module.Module;


public class ClickGUI extends Module {

	public ClickGUI() {
		super("ClickGUI", "Allows you to enable and disable modules", Category.RENDER);
		this.setKey(Keyboard.KEY_RSHIFT);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		mc.displayGuiScreen(me.kokoniara.kokoMod.core.clickgui.ClickGui.getClickGui());
		this.setToggled(false);
	}
}
