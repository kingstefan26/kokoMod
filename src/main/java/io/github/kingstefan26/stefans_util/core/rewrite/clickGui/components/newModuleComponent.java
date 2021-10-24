package io.github.kingstefan26.stefans_util.core.rewrite.clickGui.components;


import io.github.kingstefan26.stefans_util.core.rewrite.clickGui.components.subComponents.*;
import io.github.kingstefan26.stefans_util.core.rewrite.clickGui.newClickGui;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.visibleDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.general.SettingsCore;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.MultichoiseSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.SliderSetting;

import java.awt.*;
import java.util.ArrayList;

public class newModuleComponent extends newComponent {

    public basicModule mod;
    public newFrame parent;
    public int offset;
    private boolean isHovered;
    private final ArrayList<newComponent> subcomponents;
    public boolean open;
    private final int height;

    public newModuleComponent(basicModule mod, newFrame parent, int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList<>();
        this.open = false;
        height = 12;
        int opY = offset + 12;
        if (SettingsCore.getSettingsCore().getSettingsByMod(mod) != null) {
            for (AbstractSetting s : SettingsCore.getSettingsCore().getSettingsByMod(mod)) {
                switch (s.getType()) {
                    case check:
                        this.subcomponents.add(new newCheckbox((CheckSetting) s, this, opY));
                        opY += 12;
                        break;
                    case multiChoise:
                        this.subcomponents.add(new newModeButton((MultichoiseSetting) s, this, mod, opY));
                        opY += 12;
                        break;
                    case sliderNoDecimal:
                        this.subcomponents.add(new newSliderNoDecimal((SliderNoDecimalSetting) s, this, opY));
                        opY += 12;
                        break;
                    case slider:
                        this.subcomponents.add(new newSliderDouble((SliderSetting) s, this, opY));
                        opY += 12;
                        break;
                }

            }
        }
        int finalOpY = opY;
        mod.localDecoratorManager.decoratorArrayList.forEach(decorator -> {
            if(decorator.getClass().getName().equals(keyBindDecorator.class.getName())){
                this.subcomponents.add(new newKeybind(this, finalOpY));
            }
            if(decorator.getClass().getName().equals(presistanceDecorator.class.getName())){
                this.subcomponents.add(new newPersistenceButton(this,(presistanceDecorator) decorator, finalOpY));
            }
            if(decorator.getClass().getName().equals(visibleDecorator.class.getName())){
                this.subcomponents.add(new newVisibleButton(this,(visibleDecorator) decorator, finalOpY));
            }
        });

//        this.subcomponents.add(new newVisibleButton(this, mod, opY));
//        if (mod.presistanceEnabled) {
//            this.subcomponents.add(new newPersistenceButton(this, mod, opY));
//        }
    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
        int opY = offset + 12;
        for (newComponent comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 12;
        }
    }

    @Override
    public void renderComponent() {
        if (closed) return;
        newComponent.drawRect(parent.getX(),
                this.parent.getY() + this.offset,
                parent.getX() + parent.getWidth(),
                this.parent.getY() + 12 + this.offset,
                this.isHovered ? (this.mod.isToggled() ? new Color(0, 200, 20, 150).getRGB() : 0xFF222222) :
                        (this.mod.isToggled() ? new Color(14, 14, 14).getRGB() : 0xFF111111));
        this.c.drawString(
                this.mod.getName(),
                (parent.getX() + 2) * 2,
                (parent.getY() + offset - (this.height / 2)) * 2,
				-1);

        if (this.subcomponents.size() > 2) {
            this.c.drawString(
                    this.open ? "-" : "+",
                    (parent.getX() + parent.getWidth() - 10) * 2,
                    (parent.getY() + offset - this.height / 2) * 2,
                    -1);
        }
        if (this.open) {
            if (!this.subcomponents.isEmpty()) {
                for (newComponent comp : this.subcomponents) {
                    comp.renderComponent();
                }
                newComponent.drawRect(parent.getX() + 2, parent.getY() + this.offset + 12, parent.getX() + 3, parent.getY() + this.offset + ((this.subcomponents.size() + 1) * 12), newClickGui.mainColor);
            }
        }
    }

    @Override
    public int getHeight() {
        if (this.open) {
            return (12 * (this.subcomponents.size() + 1));
        }
        return 12;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        if (closed) return;
        this.isHovered = isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (newComponent comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (closed) return;
        if (isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
        }
        if (isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (newComponent comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (closed) return;
        for (newComponent comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if (closed) return;
        for (newComponent comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
    }
}
