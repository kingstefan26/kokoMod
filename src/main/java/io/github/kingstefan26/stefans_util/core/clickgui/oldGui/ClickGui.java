package io.github.kingstefan26.stefans_util.core.clickgui.oldGui;

import io.github.kingstefan26.stefans_util.core.clickgui.oldGui.component.Component;
import io.github.kingstefan26.stefans_util.core.clickgui.oldGui.component.Frame;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.Module;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;


public class ClickGui extends GuiScreen {

	public static ClickGui ClickGui;
	public static ClickGui getClickGui()  {
		if(ClickGui == null) ClickGui = new ClickGui();
		return ClickGui;
	}

	public static ArrayList<Frame> frames;
	//public CustomFont customFont;
	//public static int color = 0x2e2e2eFF;
	public static int mainColor = new Color(34,34,34,180).getRGB();
	public static int accentColor = new Color(0,200,20,150).getRGB();
	
	public ClickGui() {
		//customFont = new CustomFont(mc, new Font("JetBrains Mono", Font.BOLD, 20), 20);
		frames = new ArrayList<>();
		int frameX = 0;

		for(ModuleManager.Category c : ModuleManager.Category.values()) {
			//if(c == UtilModule) return;
			//if(c == DEBUG && !main.debug) return;
			Frame frame = new Frame(c);
			frame.setX(frameX);
			frames.add(frame);
			frameX += frame.getWidth() + 1;
		}
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
		this.buttonList.add(new GuiButton(1, this.width / 2 - 40, this.height - (this.height / 4) + 10, "reset positions"));
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
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button){
		if (button.id == 1) {
			for(Frame f : frames){
				f.resetFramePosition();
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
