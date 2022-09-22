package io.github.kingstefan26.stefans_util.core.clickGui.components.impl.subComponents;

import io.github.kingstefan26.stefans_util.core.Globals;
import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.clickGui.components.impl.moduleComponent;
import io.github.kingstefan26.stefans_util.core.clickGui.components.subComponent;
import io.github.kingstefan26.stefans_util.core.setting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;
import net.minecraft.client.Minecraft;

public class newCheckbox extends subComponent {

    private final CheckSetting op;

    public newCheckbox(CheckSetting option, moduleComponent button) {
        this.op = option;
        this.parent = button;
    }

    @Override
    public void renderComponent() {
        super.renderComponent();
        if (Globals.usestandartfontrendering) {
            Minecraft.getMinecraft().fontRendererObj.drawString(
                    this.op.getName(),
                    (parent.parent.getX() + 10),
                    (parent.parent.getY() + offset + 2),
                    -1);

        } else {
            ClickGui.p1.drawString(
                    this.op.getName(),
                    (parent.parent.getX() + 10 + 4) * 2,
                    (parent.parent.getY() + offset - 2) * 2,
                    -1);
        }
        hehe.drawRect(parent.parent.getX() + 3 + 4, parent.parent.getY() + offset + 3, parent.parent.getX() + 9 + 4, parent.parent.getY() + offset + 9, 0xFF999999);
        if (this.op.getValue()) {
            hehe.drawRect(parent.parent.getX() + 4 + 4,
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
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.op.setValue(!op.getValue());
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
