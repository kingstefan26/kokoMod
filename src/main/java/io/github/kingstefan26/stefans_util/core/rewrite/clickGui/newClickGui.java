package io.github.kingstefan26.stefans_util.core.rewrite.clickGui;

import io.github.kingstefan26.stefans_util.core.rewrite.clickGui.components.newComponent;
import io.github.kingstefan26.stefans_util.core.rewrite.clickGui.components.newFrame;
import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleRegistery;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;


public class newClickGui extends GuiScreen {

	public static newClickGui ClickGui;
	public static newClickGui getClickGui()  {
		if(ClickGui == null) ClickGui = new newClickGui();
		return ClickGui;
	}

	public static ArrayList<newFrame> frames;
	public static int mainColor = new Color(34,34,34,180).getRGB();
	public static int accentColor = new Color(0,200,20,150).getRGB();
	
	public newClickGui() {
		frames = new ArrayList<>();
		int frameX = 0;

		for(moduleManager.Category c : moduleManager.Category.values()) {
//			if(moduleRegistery.getModuleRegistery().getModulesInCategory(c).isEmpty()) continue;
			if(c == moduleManager.Category.UtilModule) continue;
			newFrame frame = new newFrame(c);
			frame.setX(frameX);
			frames.add(frame);
			frameX += frame.getWidth() + 1;
		}
	}

	public void registerComponent(basicModule m){
		for(newFrame f : frames){
			if(f.category == m.getCategory()){
				f.registerComponent(m);
			}
		}
	}



	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		for(newFrame frame : frames) {
			frame.renderFrame();
			frame.updatePosition(mouseX, mouseY);
			for(newComponent comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	
	@Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
		for(newFrame frame : frames) {
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(newComponent component : frame.getComponents()) {
						component.mouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for(newFrame frame : frames) {
			if(frame.isOpen() && keyCode != 1) {
				if(!frame.getComponents().isEmpty()) {
					for(newComponent component : frame.getComponents()) {
						component.keyTyped(typedChar, keyCode);
					}
				}
			}
		}
		if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
	}

	
	@Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
		for(newFrame frame : frames) {
			frame.setDrag(false);
		}
		for(newFrame frame : frames) {
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(newComponent component : frame.getComponents()) {
						component.mouseReleased(mouseX, mouseY, state);
					}
				}
			}
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
}
