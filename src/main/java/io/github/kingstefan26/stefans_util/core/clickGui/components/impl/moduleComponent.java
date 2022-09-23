/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.clickGui.components.impl;


import io.github.kingstefan26.stefans_util.core.Globals;
import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.clickGui.components.IsubComponent;
import io.github.kingstefan26.stefans_util.core.clickGui.components.component;
import io.github.kingstefan26.stefans_util.core.clickGui.components.impl.subComponents.*;
import io.github.kingstefan26.stefans_util.core.clickGui.components.subComponent;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.decoratorInterface;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.visibleDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsCore;
import io.github.kingstefan26.stefans_util.core.setting.impl.*;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.ArrayList;

public class moduleComponent extends component {

    /**
     * reference to the {@link BasicModule} this component is representing
     */
    public BasicModule mod;

    /**
     * reference to its parent {@link frame}
     */
    public frame parent;

    /**
     * how tall each of the subcomponents will be,
     * rn its set to 12 which is also the height of this component
     */
    @Setter
    public int offset;

    /**
     * this tell if the module component is being hovered over with a mouse,
     * used to change the components color when hovered
     */
    @Getter
    private boolean isHovered;

    /**
     * array list of subcomponents like {@link newCheckbox}
     */
    private final ArrayList<IsubComponent> subcomponents;

    public boolean open = false;

    /**
     * height in pixles, is always set to 12
     */
    private final static int height = 12;

    /**
     * this constructor is getting called for every module
     *
     * @param mod    reference to parent module
     * @param parent reference to parent frame
     */
    public moduleComponent(BasicModule mod, frame parent) {
        this.mod = mod;
        this.parent = parent;
        this.subcomponents = new ArrayList<>();

        // here we go by all the settings associated with the module and ass subcomponents based on them
        if (SettingsCore.getSettingsCore().getSettingsByMod(mod) != null) {
            for (AbstractSetting s : SettingsCore.getSettingsCore().getSettingsByMod(mod)) {
                switch (s.getType()) {
                    case CHECK:
                        this.subcomponents.add(new newCheckbox((CheckSetting) s, this));
                        break;
                    case MULTI_CHOICE:
                        this.subcomponents.add(new newModeButton((MultichoiseSetting) s, this));
                        break;
                    case SLIDER_NO_DECIMAL:
                        this.subcomponents.add(new sliderNoDecimal((SliderNoDecimalSetting) s, this));
                        break;
                    case SLIDER:
                        this.subcomponents.add(new newSliderDouble((SliderSetting) s, this));
                        break;
                    case KEY:
                        this.subcomponents.add(new newDetachedKeybind((ChoseAKeySetting) s, this));
                        break;
                }

            }
        }

        for (decoratorInterface decorator : mod.getLocalDecoratorManager().decoratorArrayList) {

            if (decorator.getClass().getName().equals(keyBindDecorator.class.getName())) {
                this.subcomponents.add(new newKeybind(this));
            }
            if (decorator.getClass().getName().equals(presistanceDecorator.class.getName())) {
                this.subcomponents.add(new newPersistenceButton(this, (presistanceDecorator) decorator));
            }
            if (decorator.getClass().getName().equals(visibleDecorator.class.getName())) {
                this.subcomponents.add(new newVisibleButton(this, (visibleDecorator) decorator));
            }
        }

        resetSubComponentsPositions(offset);
    }


    public void resetSubComponentsPositions(int offset){
        int opY = offset + 12;

        for(IsubComponent subComponent: subcomponents){
            subComponent.resetPosition(opY);
            opY += 12;
        }
    }

    @Override
    public void destroy() {
        for(IsubComponent component : this.subcomponents) {
            component.destroyComponent();
            closed = true;
            open = false;
        }
    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
        int opY = offset + 12;
        for (IsubComponent comp : this.subcomponents) {
            subComponent com = (subComponent) comp;
            com.setOff(opY);
            opY += 12;
        }
    }

    @Override
    public void renderComponent() {
        if (closed) return;
        hehe.drawRect(parent.getX(),
                this.parent.getY() + this.offset,
                parent.getX() + parent.getWidth(),
                this.parent.getY() + 12 + this.offset,
                this.isHovered ? (this.mod.isToggled() ? new Color(0, 200, 20, 150).getRGB() : 0xFF222222) :
                        (this.mod.isToggled() ? new Color(14, 14, 14).getRGB() : 0xFF111111));

        if (Globals.usestandartfontrendering) {
            Minecraft.getMinecraft().fontRendererObj.drawString(this.mod.getName(),
                    (parent.getX() + 2),
                    (parent.getY() + offset) + 3,
                    this.mod.isToggled() ? -1 : 0x808080);

        } else {
            ClickGui.c.drawString(
                    this.mod.getName(),
                    (parent.getX() + 2) * 2,
                    (parent.getY() + offset - (height / 2)) * 2,
                    -1);
        }


        if (this.subcomponents.size() != 0) {

            if (Globals.usestandartfontrendering) {
                Minecraft.getMinecraft().fontRendererObj.drawString(this.open ? "-" : "+",
                        (parent.getX() + parent.getWidth() - 10),
                        (parent.getY() + offset) + 3,
                        -1);
            } else {
                ClickGui.c.drawString(
                        this.open ? "-" : "+",
                        (parent.getX() + parent.getWidth() - 10) * 2,
                        (parent.getY() + offset - height / 2) * 2,
                        -1);
            }

        }

        if (this.open) {
            if (!this.subcomponents.isEmpty()) {
                for (IsubComponent comp : this.subcomponents) {
                    comp.renderComponent();
                }
                hehe.drawRect(parent.getX() + 2, parent.getY() + this.offset + 12, parent.getX() + 3, parent.getY() + this.offset + ((this.subcomponents.size() + 1) * 12), Globals.mainColor);
            }
        }

        if (isHovered) {
            if (mod.getDescription() != null) {
                ClickGui.getClickGui().list.add(EnumChatFormatting.WHITE + mod.getDescription());
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
            for (IsubComponent comp : this.subcomponents) {
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
        for (IsubComponent comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (closed) return;
        for (IsubComponent comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if (closed) return;
        for (IsubComponent comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
    }
}
