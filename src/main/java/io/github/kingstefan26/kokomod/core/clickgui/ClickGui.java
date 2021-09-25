package io.github.kingstefan26.kokomod.core.clickgui;

import io.github.kingstefan26.kokomod.core.clickgui.component.Component;
import io.github.kingstefan26.kokomod.core.clickgui.component.Frame;
import io.github.kingstefan26.kokomod.core.module.Category;
import io.github.kingstefan26.kokomod.core.module.blueprints.Module;
import io.github.kingstefan26.kokomod.main;
import io.github.kingstefan26.kokomod.util.CustomFont;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;

import static io.github.kingstefan26.kokomod.core.module.Category.DEBUG;
import static io.github.kingstefan26.kokomod.core.module.Category.UtilModule;

public class ClickGui extends GuiScreen {

	public static ClickGui ClickGui;
	public static ClickGui getClickGui()  {
		if(ClickGui == null) ClickGui = new ClickGui();
		return ClickGui;
	}

	public static ArrayList<Frame> frames;
	public CustomFont customFont;
	//public static int color = 0x2e2e2eFF;
	public static int mainColor = new Color(34,34,34,180).getRGB();
	public static int accentColor = new Color(0,200,20,150).getRGB();
	
	public ClickGui() {
		customFont = new CustomFont(mc, new Font("JetBrains Mono", Font.BOLD, 20), 20);
		frames = new ArrayList<>();
		int frameX = 0;
		for(Category category : Category.values()) {
			if(category == UtilModule){
				return;
			}
			if(category == DEBUG && !main.debug){
				return;
			}
			Frame frame = new Frame(category);
			frame.setX(frameX);
			frames.add(frame);
			frameX += frame.getWidth() + 1;
		}
	}

	public void clearComponents(){
		frames.forEach(Frame::clearComponents);
	}
	public void registerComponent(Module m){
		for(Frame f : frames){
			if(f.category == m.getCategory()){
				f.registerComponent(m);
			}
		}
	}



	
	@Override
	public void initGui() {
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		for(Frame frame : frames) {
			frame.renderFrame(this.fontRendererObj);
			frame.updatePosition(mouseX, mouseY);
			for(Component comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}
	
	@Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
		for(Frame frame : frames) {
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
					for(Component component : frame.getComponents()) {
						component.mouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for(Frame frame : frames) {
			if(frame.isOpen() && keyCode != 1) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
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
		for(Frame frame : frames) {
			frame.setDrag(false);
		}
		for(Frame frame : frames) {
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
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
