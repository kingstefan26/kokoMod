package me.kokoniara.kokoMod;

import me.kokoniara.kokoMod.config.configMenager;
import org.lwjgl.input.Keyboard;

import me.kokoniara.kokoMod.clickgui.ClickGui;
import me.kokoniara.kokoMod.module.Module;
import me.kokoniara.kokoMod.module.ModuleManager;
import me.kokoniara.kokoMod.settings.SettingsManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class kokoMod
{
    public static kokoMod instance;
    public ModuleManager moduleManager;
    public SettingsManager settingsManager;
    public ClickGui clickGui;
    public configMenager configMenagerINSTANCE;

    
    public void init() {
        
    	MinecraftForge.EVENT_BUS.register(this);
    	settingsManager = new SettingsManager();
    	moduleManager = new ModuleManager();
    	clickGui = new ClickGui();
    }
    
    @SubscribeEvent
    public void key(KeyInputEvent e) {
    	if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null)
    		return; 
    	try {
             if (Keyboard.isCreated()) {
                 if (Keyboard.getEventKeyState()) {
                     int keyCode = Keyboard.getEventKey();
                     if (keyCode <= 0)
                    	 return;
                     for (Module m : moduleManager.modules) {
                    	 if (m.getKey() == keyCode && keyCode > 0) {
                    		 m.toggle();
                    	 }
                     }
                 }
             }
         } catch (Exception q) { q.printStackTrace(); }
    }
}
