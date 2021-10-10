package io.github.kingstefan26.stefans_util.core.clickgui.newGui.components.subComponents;

import io.github.kingstefan26.stefans_util.core.clickgui.newGui.components.newModuleComponent;
import io.github.kingstefan26.stefans_util.core.clickgui.newGui.components.newComponent;
import io.github.kingstefan26.stefans_util.core.module.Module;

public class newPersistenceButton extends newComponent { // Remove this class if you don't want it (it's kinda useless)

	private boolean hovered;
	private newModuleComponent parent;
	private int offset;
	private int x;
	private int y;
	private Module mod;

	public newPersistenceButton(newModuleComponent button, Module mod, int offset) {
		this.parent = button;
		this.mod = mod;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void renderComponent() {
		newComponent.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered ? 0xFF222222 : 0xFF111111);
		newComponent.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xFF111111);

		this.p1.drawString(
				"presisnant: " + mod.isPresident(),
				(parent.parent.getX() + 7) * 2,
				(parent.parent.getY() + offset - 3) * 2,
				-1);

	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			mod.togglePresistance();
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
	}
}
