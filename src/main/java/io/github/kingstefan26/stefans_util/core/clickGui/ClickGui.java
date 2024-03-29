package io.github.kingstefan26.stefans_util.core.clickGui;

import com.google.common.collect.Lists;
import io.github.kingstefan26.stefans_util.core.clickGui.components.component;
import io.github.kingstefan26.stefans_util.core.clickGui.components.moduleComponent;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.util.CustomFont;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;

import static io.github.kingstefan26.stefans_util.util.FileUtils.getFileFromResourceAsStream;


public class ClickGui extends GuiScreen {
	public static ClickGui ClickGui;
	public static ClickGui getClickGui()  {
		if(ClickGui == null) ClickGui = new ClickGui();
		return ClickGui;
	}

	public static ArrayList<frame> frames;

	public static CustomFont c = new CustomFont(getFileFromResourceAsStream("assets/stefan_util/textures/font/Teko-Light.ttf"), 20);
	public static CustomFont p1 = new CustomFont(getFileFromResourceAsStream("assets/stefan_util/textures/font/Teko-Light.ttf"), 17);


	public ClickGui() {
		frames = new ArrayList<>();
		registerFrames();
		resetFramePositions();
	}

    public List<String> list = Lists.newArrayList();


	/**
	 * this will reset every y and x cord of every frame component and subcomponent
	 */
	public void resetAllPositions(){
		// resetting the frames
		int frameX = 0;
		for(frame frame: frames){
			frame.setX(frameX);
			frameX += frame.getWidth() + 1;
		}

		// resetting every component in every frame
		for(frame frame: frames){
			int totalY = frame.getHeight();
			for(component modButtons : frame.components){
				moduleComponent t = (moduleComponent) modButtons;

				t.setOffset(totalY);

				totalY += 12;
			}
		}

		// resetting every subcomponent in every component
        for (frame frame : frames) {
            for (component component : frame.components) {
                moduleComponent com = (moduleComponent) component;
                com.resetSubComponentsPositions(com.offset);
            }
        }


    }

    void registerFrames() {
        for (ModuleManager.Category c : ModuleManager.Category.values()) {
//			if(moduleRegistery.getModuleRegistery().getModulesInCategory(c).isEmpty()) continue;
            if (c == ModuleManager.Category.UTIL_MODULE) continue;
            frames.add(new frame(c));
        }
    }

    void resetFramePositions() {
        int frameX = 0;
        for (frame frame : frames) {
            frame.setX(frameX);
            frameX += frame.getWidth() + 1;
        }
    }

    public void registerComponent(BasicModule m) {
        for (frame f : frames) {
            if (f.category == m.getCategory()) {
                f.registerComponent(m);
            }
        }
    }

    public void removeComponent(BasicModule m) {
        for (frame f : frames) {
            if (f.category == m.getCategory()) {
                f.removeComponent(m);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();


        if (ModuleManager.getInstance().getLoadedModules().size() == 0) {
            ScaledResolution scaled = new ScaledResolution(mc);
            int width = scaled.getScaledWidth();
            int height = scaled.getScaledHeight();
            String text = "there are no modules loaded, if you bealve this is a issue report it on discord";
            mc.fontRendererObj.drawStringWithShadow(text, (float) (width / 2 - mc.fontRendererObj.getStringWidth(text) / 2), (float) (height / 2) - 4, 0xFFFFFF);
        }

		list.clear();
		for (frame frame : frames) {
			frame.renderFrame();
			frame.updatePosition(mouseX, mouseY);
			for (component comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}

		if(!list.isEmpty()) {
			this.drawHoveringText(list, mouseX, mouseY);
		}



		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	
	@Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
		for(frame frame : frames) {
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDragging(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(component component : frame.getComponents()) {
						component.mouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for(frame frame : frames) {
			if(frame.isOpen() && keyCode != 1) {
				if(!frame.getComponents().isEmpty()) {
					for(component component : frame.getComponents()) {
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
		for(frame frame : frames) {
			frame.setDragging(false);
		}
		for(frame frame : frames) {
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(component component : frame.getComponents()) {
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
