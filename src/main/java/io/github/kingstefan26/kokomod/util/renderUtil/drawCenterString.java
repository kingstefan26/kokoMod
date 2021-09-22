package io.github.kingstefan26.kokomod.util.renderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class drawCenterString extends Gui {

    private static drawCenterString drawCenterStringINSTANCE;

    private drawCenterString() {
    }

    public static drawCenterString getdrawCenterString() {
        if (drawCenterStringINSTANCE == null)
            drawCenterStringINSTANCE = new drawCenterString();
        return drawCenterStringINSTANCE;
    }

    public void GuiNotif(Minecraft mc, String text) {
        ScaledResolution scaled = new ScaledResolution(mc);
        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();
        drawCenteredString(mc.fontRendererObj, text, width / 2, (height / 2) - 4, Integer.parseInt("47a8ed", 16));
    }

    public void drawCenterStringOnScreen(Minecraft mc, String text, String hexcolor) {
        ScaledResolution scaled = new ScaledResolution(mc);
        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();
        drawCenteredString(mc.fontRendererObj, text, width / 2, (height / 2) - 4, Integer.parseInt(hexcolor, 16));
    }

    public void drawCenterStringOnScreenLittleToDown(Minecraft mc, String text, String hexcolor) {
        ScaledResolution scaled = new ScaledResolution(mc);
        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();
        drawCenteredString(mc.fontRendererObj, text, width / 2, (height / 2) + 4, Integer.parseInt(hexcolor, 16));
    }

    public void drawStringWereeverIWant(Minecraft mc, String text, int hexcolor, int x, int z) {
        drawString(mc.fontRendererObj, text, x, z, hexcolor);
    }

}