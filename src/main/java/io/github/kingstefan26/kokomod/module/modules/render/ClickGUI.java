package io.github.kingstefan26.kokomod.module.modules.render;

import io.github.kingstefan26.kokomod.core.clickgui.ClickGui;
import io.github.kingstefan26.kokomod.module.moduleUtil.module.Category;
import io.github.kingstefan26.kokomod.module.moduleUtil.module.Module;
import org.lwjgl.input.Keyboard;


public class ClickGUI extends Module {

	public ClickGUI() {
		super("ClickGUI", "Allows you to enable and disable modules", Category.RENDER);
		this.setKey(Keyboard.KEY_RSHIFT);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		mc.displayGuiScreen(ClickGui.getClickGui());
		this.setToggled(false);
	}
}
