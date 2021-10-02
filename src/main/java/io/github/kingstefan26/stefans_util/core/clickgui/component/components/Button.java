package io.github.kingstefan26.stefans_util.core.clickgui.component.components;

import io.github.kingstefan26.stefans_util.core.clickgui.ClickGui;
import io.github.kingstefan26.stefans_util.core.clickgui.component.Component;
import io.github.kingstefan26.stefans_util.core.clickgui.component.Frame;
import io.github.kingstefan26.stefans_util.core.clickgui.component.components.sub.Checkbox;
import io.github.kingstefan26.stefans_util.core.clickgui.component.components.sub.*;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;

import java.awt.*;
import java.util.ArrayList;

public class Button extends Component {

	public Module mod;
	public Frame parent;
	public int offset;
	private boolean isHovered;
	private ArrayList<Component> subcomponents;
	public boolean open;
	private int height;
	
	public Button(Module mod, Frame parent, int offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<>();
		this.open = false;
		height = 12;
		int opY = offset + 12;
		if(SettingsManager.getSettingsManager().getSettingsByMod(mod) != null) {
			for(Setting s : SettingsManager.getSettingsManager().getSettingsByMod(mod)){
				if(s.isCombo()){
					this.subcomponents.add(new ModeButton(s, this, mod, opY));
					opY += 12;
				}
				if(s.isSlider()){
					this.subcomponents.add(new Slider(s, this, opY));
					opY += 12;
				}
				if(s.isCheck()){
					this.subcomponents.add(new Checkbox(s, this, opY));
					opY += 12;
				}
			}
		}
		this.subcomponents.add(new Keybind(this, opY));
		this.subcomponents.add(new VisibleButton(this, mod, opY));
		if(mod.presistanceEnabled){
			this.subcomponents.add(new PersistenceButton(this, mod, opY));
		}
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
		int opY = offset + 12;
		for(Component comp : this.subcomponents) {
			comp.setOff(opY);
			opY += 12;
		}
	}
	
	@Override
	public void renderComponent() {
		if(closed) return;
		Component.drawRect(parent.getX(),
				this.parent.getY() + this.offset,
				parent.getX() + parent.getWidth(),
				this.parent.getY() + 12 + this.offset,
				this.isHovered ? (this.mod.isToggled() ? new Color(0, 200, 20, 150).getRGB() : 0xFF222222) :
						(this.mod.isToggled() ? new Color(14,14,14).getRGB() : 0xFF111111));
//		GL11.glPushMatrix();
//		GL11.glScalef(0.5f,0.5f, 0.5f);
//		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.mod.getName(),
//				(parent.getX() + 2) * 2,
//				(parent.getY() + offset + 2) * 2 + 4,
//				this.mod.isToggled() ? -1 : 0x999999);
		this.c.drawStringS(
				this.mod.getName(),
				(parent.getX()+ 2) * 2,
//				parent.getY(),
				(parent.getY() + offset - (this.height / 2)) * 2,
				this.mod.isToggled() ? -1 : -1);

		if(this.subcomponents.size() > 2) {
//			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.open ? "-" : "+",
//					(parent.getX() + parent.getWidth() - 10) * 2,
//					(parent.getY() + offset + 2) * 2 + 4,
//					-1);
			this.c.drawStringS(
					this.open ? "-" : "+",
					(parent.getX() + parent.getWidth() - 10) * 2,
					(parent.getY() + offset - this.height / 2) * 2,
					-1);
		}
//		GL11.glPopMatrix();
		if(this.open) {
			if(!this.subcomponents.isEmpty()) {
				for(Component comp : this.subcomponents) {
					comp.renderComponent();
				}
				Component.drawRect(parent.getX() + 2, parent.getY() + this.offset + 12, parent.getX() + 3, parent.getY() + this.offset + ((this.subcomponents.size() + 1) * 12), ClickGui.mainColor);
			}
		}
	}
	
	@Override
	public int getHeight() {
		if(this.open) {
			return (12 * (this.subcomponents.size() + 1));
		}
		return 12;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		if(closed) return;
		this.isHovered = isMouseOnButton(mouseX, mouseY);
		if(!this.subcomponents.isEmpty()) {
			for(Component comp : this.subcomponents) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(closed) return;
		if(isMouseOnButton(mouseX, mouseY) && button == 0) {
			this.mod.toggle();
		}
		if(isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			this.parent.refresh();
		}
		for(Component comp : this.subcomponents) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		if(closed) return;
		for(Component comp : this.subcomponents) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int key) {
		if(closed) return;
		for(Component comp : this.subcomponents) {
			comp.keyTyped(typedChar, key);
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		return x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
	}
}
