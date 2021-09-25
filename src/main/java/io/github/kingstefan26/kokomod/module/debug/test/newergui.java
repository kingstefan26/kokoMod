package io.github.kingstefan26.kokomod.module.debug.test;

import io.github.kingstefan26.kokomod.util.CustomFont;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class newergui extends GuiScreen {
    CustomFont c;
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - (this.height / 4) + 10, "fuck you in the ass"));
        c = new CustomFont(mc, new Font("JetBrains Mono", Font.BOLD, 17), 17);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            mc.thePlayer.closeScreen();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        c.drawStringS(this, "fuck raw you in tha ass", this.width, this.height, 0xFFFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
