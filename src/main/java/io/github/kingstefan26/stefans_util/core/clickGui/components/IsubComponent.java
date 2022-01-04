/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.clickGui.components;

public interface IsubComponent {
    void destroyComponent();
    void renderComponent();
    void resetPosition(int offset);
    void updateComponent(int mouseX, int mouseY);
    void mouseClicked(int mouseX, int mouseY, int button);
    void mouseReleased(int mouseX, int mouseY, int mouseButton);
    void keyTyped(char typedChar, int key);
}
