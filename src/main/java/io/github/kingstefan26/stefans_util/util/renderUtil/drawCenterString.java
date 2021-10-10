package io.github.kingstefan26.stefans_util.util.renderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class drawCenterString{
    public static void GuiNotif(Minecraft mc, String text) {
        ScaledResolution scaled = new ScaledResolution(mc);
        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();
        mc.fontRendererObj.drawStringWithShadow(text, (float)(width / 2 - mc.fontRendererObj.getStringWidth(text) / 2), (float)(height / 2) - 4, 0x47a8ed);
    }

    public static void drawCenterStringOnScreen(Minecraft mc, String text, String hexcolor) {
        ScaledResolution scaled = new ScaledResolution(mc);
        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();
        mc.fontRendererObj.drawStringWithShadow(text, (float)(width / 2 - mc.fontRendererObj.getStringWidth(text) / 2), (float)(height / 2) - 4, Integer.parseInt(hexcolor, 16));
    }

    public static void drawCenterStringOnScreenLittleToDown(Minecraft mc, String text, String hexcolor) {
        ScaledResolution scaled = new ScaledResolution(mc);
        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();
        mc.fontRendererObj.drawStringWithShadow(text, (float)(width / 2 - mc.fontRendererObj.getStringWidth(text) / 2), (float)(height / 2) + 4, Integer.parseInt(hexcolor, 16));
    }

    public static void drawStringWereeverIWant(Minecraft mc, String text, int hexcolor, int x, int z) {
        mc.fontRendererObj.drawStringWithShadow(text, (float)x, (float)z, hexcolor);
    }

}