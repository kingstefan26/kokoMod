package io.github.kingstefan26.kokomod.core.clickgui.component;

import io.github.kingstefan26.kokomod.core.clickgui.ClickGui;
import io.github.kingstefan26.kokomod.core.clickgui.component.components.Button;
import io.github.kingstefan26.kokomod.core.config.configMenager;
import io.github.kingstefan26.kokomod.core.config.configObject;
import io.github.kingstefan26.kokomod.core.module.Category;
import io.github.kingstefan26.kokomod.core.module.blueprints.Module;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class Frame {

	public ArrayList<Component> components;
	public Category category;
	private boolean open;
	private int width;
	private int y;
	private int x;
	private int translatedX;
	private int translatedY;
	private int barHeight;
	private boolean isDragging;
	public int dragX;
	public int dragY;

	configObject xConfig;
	configObject yConfig;
	int tY = this.barHeight;
	public Frame(Category cat)  {

		this.components = new ArrayList<>();
		this.category = cat;
		this.width = 84;
		this.barHeight = 13;
		this.dragX = 0;
		this.open = false;
		this.isDragging = false;

		xConfig = new configObject(cat + "frameX", "frame x", x);
		yConfig = new configObject(cat + "frameY", "frame y", y);
		configMenager.getConfigManager().createConfigObject(xConfig);
		configMenager.getConfigManager().createConfigObject(yConfig);

		this.x = xConfig.getIntValue();
		this.y = yConfig.getIntValue();

//		for(Module mod : ModuleManager.getModuleManager().getModulesInCategory(category)) {
//			Button modButton = new Button(mod, this, tY);
//			this.components.add(modButton);
//			tY += 12;
//		}
//		for(Module mod : moduleRegistery.getModuleRegistery().getModulesInCategory(category)) {
//			Button modButton = new Button(mod, this, tY);
//			this.components.add(modButton);
//			tY += 12;
//		}
	}
	
	public ArrayList<Component> getComponents() {
		return components;
	}

	public void clearComponents() {
		components.forEach(a -> a.closed = true);
		components.clear();
		tY = this.barHeight;
	}
	public void registerComponent(Module m){
		Button modButton = new Button(m, this, tY);
		this.components.add(modButton);
		tY += 12;
	}
	
	public void setX(int newX) {
		this.x = newX;
		xConfig.setIntValue(newX);
	}
	
	public void setY(int newY) {
		this.y = newY;
		yConfig.setIntValue(newY);
	}
	
	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public void renderFrame(FontRenderer fontRenderer) {
		Gui.drawRect(this.x + 5, this.y + 5, this.x + this.width + 5, this.y + this.barHeight + 5, 0xFF222222);
		Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, ClickGui.mainColor);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f,0.5f, 0.5f);
		this.translatedX = this.x * 2;
		this.translatedY = this.y * 2;

//		fontRenderer.drawStringWithShadow("example text", 50, 50, -1);
//		ClickGui.getClickGui().customFont.drawStringS(ClickGui.getClickGui(),"example text", 100, 100, 0xFFFFFFFF);

		ClickGui.getClickGui().customFont.drawStringS(ClickGui.getClickGui(),this.category.name(), (translatedX + 2) * 2 + 5, (int)(translatedY + 2.5f) * 2 + 5, 0xFFFFFFFF);
		//fontRenderer.drawStringWithShadow(this.category.name(), (this.x + 2) * 2 + 5, (this.y + 2.5f) * 2 + 5, 0xFFFFFFFF);
		//fontRenderer.drawStringWithShadow(this.open ? "-" : "+", (this.x + this.width - 10) * 2 + 5, (this.y + 2.5f) * 2 + 5, -1);
		ClickGui.getClickGui().customFont.drawStringS(ClickGui.getClickGui(),this.open ? "-" : "+", (translatedX + this.width - 10) * 2 + 5, (int)(translatedY + 2.5f) * 2 + 5, -1);

		GL11.glPopMatrix();
		if(this.open) {
			if(!this.components.isEmpty()) {
				Gui.drawRect(this.x, this.y + this.barHeight, this.x + 1, this.y + this.barHeight + (12 * components.size()), new Color(0, 200, 20, 150).getRGB());
				Gui.drawRect(this.x, this.y + this.barHeight + (12 * components.size()), this.x + this.width, this.y + this.barHeight + (12 * components.size()) + 1, new Color(0, 200, 20, 150).getRGB());
				Gui.drawRect(this.x + this.width, this.y + this.barHeight, this.x + this.width - 1, this.y + this.barHeight + (12 * components.size()), new Color(0, 200, 20, 150).getRGB());
				for(Component component : components) {
					component.renderComponent();
				}
			}
		}
	}
	
	public void refresh() {
		int off = this.barHeight;
		for(Component comp : components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}
	
	public int getX() {
		return xConfig.getIntValue();
	}
	
	public int getY() {
		return yConfig.getIntValue();
	}
	
	public int getWidth() {
		return width;
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if(this.isDragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
		}
	}
	
	public boolean isWithinHeader(int x, int y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
	}
	
}
