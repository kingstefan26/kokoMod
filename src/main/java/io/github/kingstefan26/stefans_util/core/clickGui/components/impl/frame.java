/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.clickGui.components.impl;

import io.github.kingstefan26.stefans_util.core.clickGui.components.component;
import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.globals;
import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.util.CustomFont;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class frame {
    @Getter
    public ArrayList<component> components;

    public moduleManager.Category category;

    @Setter
    @Getter
    private boolean open = false;

    @Getter
    private final int width = 84;
    @Getter
    private final int height = 13;
    private int y;
    private int x;

    @Setter
    private boolean isDragging = false;
    public int dragX;
    public int dragY;

    static CustomFont c = new CustomFont(new Font("JetBrains Mono", Font.BOLD, 20), 20);

    configObject xConfig;
    configObject yConfig;

    int totalY = this.height;

    public frame(moduleManager.Category cat) {
        this.components = new ArrayList<>();
        this.category = cat;

        xConfig = new configObject(cat + "newframeX", "new frame x", x);
        yConfig = new configObject(cat + "newframeY", "new frame y", y);

        this.x = xConfig.getIntValue();
        this.y = yConfig.getIntValue();
    }


    public void registerComponent(basicModule m) {
        for(component modButtons : this.components){
            moduleComponent t = (moduleComponent) modButtons;
            if(t.getClass().getName().equals(m.getClass().getName())) return;
        }
        final moduleComponent component = new moduleComponent(m, this);

        component.setOffset(totalY);

        this.components.add(component);

        totalY += 12;
    }

    public void removeComponent(basicModule m) {
        Iterator<component> iter = this.components.iterator();

        while (iter.hasNext()) {
            component a = iter.next();
            moduleComponent t = (moduleComponent) a;
            if(m.getName().equals(t.mod.getName())) {
                iter.remove();
                t.destroy();
            }
        }
        totalY -= 12;
    }


    public void setX(int newX) {
        this.x = newX;
        xConfig.setIntValue(newX);
    }

    public void setY(int newY) {
        this.y = newY;
        yConfig.setIntValue(newY);
    }


    public void renderFrame() {
        if(this.getComponents().size() == 0) return;

        hehe.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, 0xFF00010a);

        if(globals.usestandartfontrendering){
            Minecraft.getMinecraft().fontRendererObj.drawString(this.category.name(),
                    this.x,
                    this.y - (this.height / 2) + 10,
                    0xFFFFFFFF);

            Minecraft.getMinecraft().fontRendererObj.drawString(this.open ? "-" : "+",
                    (this.x + this.width - 10) + 3,
                    this.y - (this.height / 2) + 10,
                    -1);
        }else{
            c.drawString(this.category.name(),
                    this.x * 2,
                    this.y * 2 - (this.height / 2),
                    0xFFFFFFFF);
            c.drawString(this.open ? "-" : "+",
                    (this.x + this.width - 10) * 2 + 5,
                    this.y * 2 - (this.height / 2),
                    -1);
        }

        if (this.open) {
            if (!this.components.isEmpty()) {
                for (component component : components) {
                    component.renderComponent();
                }
            }
        }


    }

    public void refresh() {
        int heightOffset = this.height;
        for (component comp : components) {
            comp.setOff(heightOffset);
            heightOffset += comp.getHeight();
        }
    }

    public int getX() {
        return xConfig.getIntValue();
    }

    public int getY() {
        return yConfig.getIntValue();
    }


    public void updatePosition(int mouseX, int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - dragX);
            this.setY(mouseY - dragY);
        }
    }

    public boolean isWithinHeader(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

}
