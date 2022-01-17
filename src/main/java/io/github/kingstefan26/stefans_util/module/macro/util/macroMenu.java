package io.github.kingstefan26.stefans_util.module.macro.util;

import io.github.kingstefan26.stefans_util.module.macro.macro;
import net.minecraft.client.gui.GuiScreen;


public class macroMenu extends GuiScreen {
	private final macro parent;

	public macroMenu(macro parent){
		this.parent = parent;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		this.allowUserInput = true;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}