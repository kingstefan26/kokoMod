package io.github.kingstefan26.stefans_util.core.clickGui.components.impl;

import io.github.kingstefan26.stefans_util.core.Globals;
import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.clickGui.components.moduleComponent;
import io.github.kingstefan26.stefans_util.core.clickGui.components.subComponent;
import io.github.kingstefan26.stefans_util.core.setting.impl.MultichoiseSetting;
import net.minecraft.client.Minecraft;

public class newModeButton extends subComponent {

    private final MultichoiseSetting set;

    private int modeIndex;
    private final int maxIndex;

    public newModeButton(MultichoiseSetting set, moduleComponent button) {
        this.set = set;
        this.parent = button;
        modeIndex = set.getAllPossibleValues().indexOf(set.getValue());
        maxIndex = set.getAllPossibleValues().size() - 1;
    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
    }

    @Override
    public void renderComponent() {
        super.renderComponent();
        if (Globals.usestandartfontrendering) {
            Minecraft.getMinecraft().fontRendererObj.drawString(
                    "Mode: " + set.getValue(),
                    (parent.parent.getX() + 7),
                    (parent.parent.getY() + offset + 3),
                    -1);

        } else {

            ClickGui.c.drawString(
                    "Mode: " + set.getValue(),
                    (parent.parent.getX() + 7) * 2,
                    (parent.parent.getY() + offset - 3) * 2,
                    -1);
        }

    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = isMouseOnButton(mouseX, mouseY);
        this.y = parent.parent.getY() + offset;
        this.x = parent.parent.getX();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            if (modeIndex >= maxIndex) {
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
