package io.github.kingstefan26.stefans_util.util;

import net.minecraft.client.Minecraft;

public class ModuleUtil {
    public static boolean isOnWorldCheck() {
        return Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null;
    }
}