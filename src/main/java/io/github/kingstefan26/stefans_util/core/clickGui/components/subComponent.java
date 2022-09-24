/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.clickGui.components;

import io.github.kingstefan26.stefans_util.core.clickGui.components.impl.moduleComponent;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;

public abstract class subComponent implements IsubComponent {
    protected boolean hovered;
    public moduleComponent parent;
    protected int offset;
    protected int x;
    protected int y;

    public void setOff(int newOff) {
        offset = newOff;
    }

    @Override
    public void destroyComponent() {}

    @Override
    public void renderComponent() {
        hehe.drawRect(parent.parent.getX() + 2,
                parent.parent.getY() + offset,
                parent.parent.getX() + (parent.parent.getWidth()),
                parent.parent.getY() + offset + 12,
                this.hovered ? 0xFF222222 : 0xFF111111);
        hehe.drawRect(parent.parent.getX(),
                parent.parent.getY() + offset,
                parent.parent.getX() + 2,
                parent.parent.getY() + offset + 12,
                0xFF111111);
    }

    @Override
    public void resetPosition(int offset) {
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char typedChar, int key) {

    }
}
