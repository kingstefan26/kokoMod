package io.github.kingstefan26.stefans_util.core.clickGui.components.impl;

import io.github.kingstefan26.stefans_util.core.Globals;
import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.clickGui.components.moduleComponent;
import io.github.kingstefan26.stefans_util.core.clickGui.components.subComponent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class newKeybind extends subComponent {

    private boolean binding;


    public newKeybind(moduleComponent button) {
        this.parent = button;
    }

    @Override
    public void renderComponent() {
        super.renderComponent();
        if (Globals.usestandartfontrendering) {
            Minecraft.getMinecraft().fontRendererObj.drawString(
                    binding ? "Press a key..." : ("Key: " + Keyboard.getKeyName(this.parent.mod.getLocalDecoratorManager().keyBindDecorator.keybind.getKeyCode())),
                    (parent.parent.getX() + 7),
                    (parent.parent.getY() + offset + 3),
                    -1);
        } else {
            ClickGui.p1.drawString(
                    binding ? "Press a key..." : ("Key: " + Keyboard.getKeyName(this.parent.mod.getLocalDecoratorManager().keyBindDecorator.keybind.getKeyCode())),
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
            this.binding = !this.binding;
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if (this.binding) {
            if (key == 1) {
                this.binding = false;
            }
            this.parent.mod.getLocalDecoratorManager().keyBindDecorator.keybind.setKeyCode(key);
            this.binding = false;
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
