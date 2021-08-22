package me.kokoniara.kokoMod;

import me.kokoniara.kokoMod.config.configMenager;
import me.kokoniara.kokoMod.util.isOnUpdater;
import me.kokoniara.kokoMod.util.sendChatMessage;
import me.kokoniara.kokoMod.util.teleportListener;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import me.kokoniara.kokoMod.clickgui.ClickGui;
import me.kokoniara.kokoMod.module.Module;
import me.kokoniara.kokoMod.module.ModuleManager;
import me.kokoniara.kokoMod.settings.SettingsManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

import static me.kokoniara.kokoMod.main.VERSION;

public class kokoMod {
    public static kokoMod instance;

    public boolean firstStartup = true;
    public ModuleManager moduleManager;
    public SettingsManager settingsManager;
    public ClickGui clickGui;
    teleportListener teleportListenerOBJ;
    public isOnUpdater isOnUpdaterINSTASNCE;

    @SubscribeEvent
    public void onfirstWorldJoin(TickEvent.PlayerTickEvent e){
        if(firstStartup){
            if( Minecraft.getMinecraft() == null || Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null ){
                return;
            }else{
                firstStartup = false;
                sendChatMessage.sendClientMessage("§bThis version of Cock Mod™ "+ VERSION +"-alpha does not contain stds nor aids! §oall rights reserved§o§r Session id steal-er privacy policy §nhere§n", false);
            }
        }
    }

    public void init() {
    	MinecraftForge.EVENT_BUS.register(this);
    	settingsManager = new SettingsManager();
    	moduleManager = new ModuleManager();
    	clickGui = new ClickGui();
    	teleportListenerOBJ = teleportListener.getTeleportListner();
    	isOnUpdaterINSTASNCE = isOnUpdater.getisOnUpdater();
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
