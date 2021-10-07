package io.github.kingstefan26.stefans_util.core.clickgui.newGui.components;

import io.github.kingstefan26.stefans_util.core.config.configMenager;
import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.util.CustomFont;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.ArrayList;

public class newFrame {

    public ArrayList<newComponent> components;
    public ModuleManager.Category category;
    private boolean open;
    private final int width;
    private int y;
    private int x;
    private int translatedX;
    private int translatedY;
    private int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;
    CustomFont c = new CustomFont(Minecraft.getMinecraft(), new Font("JetBrains Mono", Font.BOLD, 20), 20);

    configObject xConfig;
    configObject yConfig;
    int tY = this.barHeight;

    public newFrame(ModuleManager.Category cat) {

        this.components = new ArrayList<>();
        this.category = cat;
        this.width = 84;
        this.barHeight = 13;
        this.dragX = 0;
        this.open = false;
        this.isDragging = false;

        xConfig = new configObject(cat + "frameX", "frame x", x);
        yConfig = new configObject(cat + "frameY", "frame y", y);
        configMenager.getConfigManager().createConfigObject(xConfig);
        configMenager.getConfigManager().createConfigObject(yConfig);

        this.x = xConfig.getIntValue();
        this.y = yConfig.getIntValue();
    }

    public ArrayList<newComponent> getComponents() {
        return components;
    }

    public void resetFramePosition() {
        xConfig.setIntValue(0);
        yConfig.setIntValue(0);
        x = 0;
        y = 0;
    }

    public void registerComponent(Module m) {
//		Button modButton = new Button(m, this, tY);
        newModuleComponent modButton = new newModuleComponent(m, this, tY);
        this.components.add(modButton);
        tY += 12;
    }

    public void setX(int newX) {
        this.x = newX;
        xConfig.setIntValue(newX);
    }

    public void setY(int newY) {
        this.y = newY;
        yConfig.setIntValue(newY);
    }

    public void setDrag(boolean drag) {
        this.isDragging = drag;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void renderFrame() {
        newComponent.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, 0xFF222200);
        c.drawString(this.category.name(),
                this.x * 2,
                this.y * 2 - (this.barHeight / 2),
                0xFFFFFFFF);
        c.drawString(this.open ? "-" : "+",
                (this.x + this.width - 10) * 2 + 5,
                this.y * 2 - (this.barHeight / 2),
                -1);

        if (this.open) {
            if (!this.components.isEmpty()) {
                for (newComponent component : components) {
                    component.renderComponent();
                }
            }
        }
    }

    public void refresh() {
        int off = this.barHeight;
        for (newComponent comp : components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
    }

    public int getX() {
        return xConfig.getIntValue();
    }

    public int getY() {
        return yConfig.getIntValue();
    }

    public int getWidth() {
        return width;
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - dragX);
            this.setY(mouseY - dragY);
        }
    }

    public boolean isWithinHeader(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }

}
