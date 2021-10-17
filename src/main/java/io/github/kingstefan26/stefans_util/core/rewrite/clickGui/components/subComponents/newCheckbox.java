package io.github.kingstefan26.stefans_util.core.rewrite.clickGui.components.subComponents;

import io.github.kingstefan26.stefans_util.core.rewrite.clickGui.components.newModuleComponent;
import io.github.kingstefan26.stefans_util.core.rewrite.clickGui.components.newComponent;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.CheckSetting;

public class newCheckbox extends newComponent {

	private boolean hovered;
	private CheckSetting op;
	private newModuleComponent parent;
	private int offset;
	private int x;
	private int y;
	
	public newCheckbox(CheckSetting option, newModuleComponent button, int offset) {
		this.op = option;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	@Override
	public void renderComponent() {
		newComponent.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered ? 0xFF222222 : 0xFF111111);
		newComponent.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xFF111111);
		this.p1.drawString(
				this.op.getName(),
				(parent.parent.getX() + 10 + 4) * 2,
				(parent.parent.getY() + offset - 2) * 2,
				-1);
		newComponent.drawRect(parent.parent.getX() + 3 + 4, parent.parent.getY() + offset + 3, parent.parent.getX() + 9 + 4, parent.parent.getY() + offset + 9, 0xFF999999);
		if(this.op.getValue()) {
			newComponent.drawRect(parent.parent.getX() + 4 + 4,
					parent.parent.getY() + offset + 4,
					parent.parent.getX() + 8 + 4,
					parent.parent.getY() + offset + 8,
					0xFF666666);
		}
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
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
			this.op.setValue(!op.getValue());
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
	}
}
