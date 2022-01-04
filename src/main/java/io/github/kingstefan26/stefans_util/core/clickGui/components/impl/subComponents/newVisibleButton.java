package io.github.kingstefan26.stefans_util.core.clickGui.components.impl.subComponents;

import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.clickGui.components.impl.moduleComponent;
import io.github.kingstefan26.stefans_util.core.clickGui.components.subComponent;
import io.github.kingstefan26.stefans_util.core.globals;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.visibleDecorator;
import net.minecraft.client.Minecraft;

public class newVisibleButton extends subComponent {

    final visibleDecorator decorator;

    public newVisibleButton(moduleComponent button, visibleDecorator decorator) {
        this.parent = button;
        this.decorator = decorator;
    }

    @Override
    public void renderComponent() {
        super.renderComponent();

        if (globals.usestandartfontrendering) {
            Minecraft.getMinecraft().fontRendererObj.drawString(
                    "Visible: " + decorator.isVisibilityEnabled(),
                    (parent.parent.getX() + 7),
                    (parent.parent.getY() + offset + 3),
                    -1
            );

        } else {
            ClickGui.p1.drawString(
                    "Visible: " + decorator.isVisibilityEnabled(),
                    (parent.parent.getX() + 7) * 2,
                    (parent.parent.getY() + offset - 3) * 2,
                    -1
            );
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
            decorator.toggleVisibility();
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
