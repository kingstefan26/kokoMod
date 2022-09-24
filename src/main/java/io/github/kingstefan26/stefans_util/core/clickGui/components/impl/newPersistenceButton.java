package io.github.kingstefan26.stefans_util.core.clickGui.components.impl;

import io.github.kingstefan26.stefans_util.core.Globals;
import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.clickGui.components.moduleComponent;
import io.github.kingstefan26.stefans_util.core.clickGui.components.subComponent;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import net.minecraft.client.Minecraft;

public class newPersistenceButton extends subComponent {

    private final presistanceDecorator decorator;

    public newPersistenceButton(moduleComponent button, presistanceDecorator decorator) {
        this.parent = button;
        this.decorator = decorator;
    }


    @Override
    public void renderComponent() {
        super.renderComponent();
        String text = "presisnant: " + decorator.isPresidentaceEnabled();

        if (Globals.usestandartfontrendering) {
            Minecraft.getMinecraft().fontRendererObj.drawString(
                    text,
                    parent.parent.getX() + 7,
                    parent.parent.getY() + offset + 3,
                    0xFFFFFFFF
            );

        } else {
            ClickGui.c.drawString(
                    text,
                    parent.parent.getX() + 7,
                    parent.parent.getY() + offset - 3,
                    0xFFFFFFFF,
                    0.35F
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
            decorator.toggleParentModState();
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
