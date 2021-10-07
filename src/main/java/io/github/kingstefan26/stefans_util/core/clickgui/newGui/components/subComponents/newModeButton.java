package io.github.kingstefan26.stefans_util.core.clickgui.newGui.components.subComponents;

import io.github.kingstefan26.stefans_util.core.clickgui.newGui.components.newModuleComponent;
import io.github.kingstefan26.stefans_util.core.clickgui.newGui.components.newComponent;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.impl.MultichoiseSetting;

public class newModeButton extends newComponent {

	private boolean hovered;
	private newModuleComponent parent;
	private MultichoiseSetting set;
	private int offset;
	private int x;
	private int y;
	private Module mod;

	private int modeIndex;
	
	public newModeButton(MultichoiseSetting set, newModuleComponent button, Module mod, int offset) {
		this.set = set;
		this.parent = button;
		this.mod = mod;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
		modeIndex = set.getAllPossibleValues().indexOf(set.getValue());
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void renderComponent() {
		newComponent.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered ? 0xFF222222 : 0xFF111111);
		newComponent.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xFF111111);
//		GL11.glPushMatrix();
//		GL11.glScalef(0.5f,0.5f, 0.5f);
//		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
//				"Mode: " + set.getValString(),
//				(parent.parent.getX() + 7) * 2,
//				(parent.parent.getY() + offset + 2) * 2 + 5,
//				-1);
		this.c.drawString(
				"Mode: " + set.getValue(),
				(parent.parent.getX()+ 7) * 2,
				(parent.parent.getY()+ offset - 3) * 2,
				-1);
//		GL11.glPopMatrix();
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
			int maxIndex = set.getAllPossibleValues().size() - 1;
			if(modeIndex >= maxIndex) {
				modeIndex = 0;
			} else {
				modeIndex++;
			}

			set.setValue(set.getAllPossibleValues().get(modeIndex));
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
	}
}
