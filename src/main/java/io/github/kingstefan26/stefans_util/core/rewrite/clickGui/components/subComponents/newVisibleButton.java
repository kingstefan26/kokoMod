package io.github.kingstefan26.stefans_util.core.rewrite.clickGui.components.subComponents;

import io.github.kingstefan26.stefans_util.core.rewrite.clickGui.components.newModuleComponent;
import io.github.kingstefan26.stefans_util.core.rewrite.clickGui.components.newComponent;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.visibleDecorator;

public class newVisibleButton extends newComponent {

	private boolean hovered;
	private final newModuleComponent parent;
	private int offset;
	private int x;
	private int y;
	final visibleDecorator decorator;

	public newVisibleButton(newModuleComponent button, visibleDecorator decorator, int offset) {
		this.parent = button;
		this.decorator = decorator;
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
				"Visible: " + decorator.isVisibilityEnabled(),
				(parent.parent.getX() + 7) * 2,
				(parent.parent.getY() + offset - 3) * 2,
				-1
		);
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
			decorator.toggleVisibility();
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
	}
}
