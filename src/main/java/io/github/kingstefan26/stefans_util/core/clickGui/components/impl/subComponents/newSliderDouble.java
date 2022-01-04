package io.github.kingstefan26.stefans_util.core.clickGui.components.impl.subComponents;

import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.clickGui.components.impl.moduleComponent;
import io.github.kingstefan26.stefans_util.core.clickGui.components.subComponent;
import io.github.kingstefan26.stefans_util.core.globals;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderSetting;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;
import net.minecraft.client.Minecraft;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class newSliderDouble extends subComponent {

    private final SliderSetting set;
    private boolean dragging;

    private double renderWidth;

    public newSliderDouble(SliderSetting value, moduleComponent button) {
        this.set = value;
        this.parent = button;
    }

    @Override
    public void renderComponent() {

        hehe.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 12, this.hovered ? 0xFF222222 : 0xFF111111);
        final int drag = (int) (this.set.getValue() / this.set.getMax() * this.parent.parent.getWidth());

        hehe.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (int) renderWidth,
                parent.parent.getY() + offset + 12, hovered ? 0xFF555555 : 0xFF444444);

        hehe.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2,
                parent.parent.getY() + offset + 12, 0xFF111111);


        if (globals.usestandartfontrendering) {
            Minecraft.getMinecraft().fontRendererObj.drawString(
                    this.set.getName() + ": " + this.set.getValue(),
                    (parent.parent.getX() + 7),
                    (parent.parent.getY() + offset + 3),
                    -1);
        } else {
            ClickGui.p1.drawString(
                    this.set.getName() + ": " + this.set.getValue(),
                    (parent.parent.getX() * 2 + 15),
                    (parent.parent.getY() + offset - 3) * 2,
                    -1);
        }

    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY);
        this.y = parent.parent.getY() + offset;
        this.x = parent.parent.getX();

        double diff = Math.min(88, Math.max(0, mouseX - this.x));

        double min = set.getMin();
        double max = set.getMax();

        renderWidth = (88) * (set.getValue() - min) / (max - min);

        if (dragging) {
            if (diff == 0) {
                set.setValue((double) set.getMin());
            } else {
                double newValue = roundToPlace(((diff / 88) * (max - min) + min));
                set.setValue(newValue);
            }
        }
    }

    private static double roundToPlace(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
            dragging = true;
        }
        if (isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        dragging = false;
    }

    public boolean isMouseOnButtonD(int x, int y) {
        return x > this.x && x < this.x + (parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 12;
    }

    public boolean isMouseOnButtonI(int x, int y) {
        return x > this.x + parent.parent.getWidth() / 2 && x < this.x + parent.parent.getWidth() && y > this.y && y < this.y + 12;
    }
}
