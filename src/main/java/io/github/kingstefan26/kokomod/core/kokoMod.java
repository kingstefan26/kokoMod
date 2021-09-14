package io.github.kingstefan26.kokomod.core;

import io.github.kingstefan26.kokomod.core.clickgui.ClickGui;
import io.github.kingstefan26.kokomod.module.moduleUtil.config.configMenager;
import io.github.kingstefan26.kokomod.module.moduleUtil.module.Module;
import io.github.kingstefan26.kokomod.module.moduleUtil.module.ModuleManager;
import io.github.kingstefan26.kokomod.module.moduleUtil.settings.SettingsManager;
import io.github.kingstefan26.kokomod.util.SBinfo.isOnUpdater;
import io.github.kingstefan26.kokomod.util.sendChatMessage;
import io.github.kingstefan26.kokomod.util.teleportListener;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import static io.github.kingstefan26.kokomod.main.VERSION;

public class kokoMod {
    public static kokoMod instance;

    public static kokoMod getkokoMod(){
    	if(instance == null) instance = new kokoMod();
    	return instance;
    }

    public boolean firstStartup = true;

    @SubscribeEvent
    public void onFirstWorldJoin(TickEvent.ClientTickEvent e){
        if(firstStartup){
        	Minecraft mc = Minecraft.getMinecraft();
        	if(mc != null){
        		if(mc.thePlayer != null || mc.theWorld != null){
			        firstStartup = false;

			        sendChatMessage.sendClientMessageWithRickRoll(
					        "§bThis version of Cock Mod™ "
							        + VERSION +
							        " does not contain stds nor aids! §oall rights reserved§o§r Session id steal-er privacy policy §nhere§n"
					        , false);
		        }
	        }
        }
    }

    public void init() {
    	MinecraftForge.EVENT_BUS.register(this);
	    configMenager.configMenager = configMenager.getConfigManager();
	    SettingsManager.SettingsManager = SettingsManager.getSettingsManager();
	    ModuleManager.ModuleManager = ModuleManager.getModuleManager();
	    ClickGui.ClickGui = ClickGui.getClickGui();
	    teleportListener.teleportListener = teleportListener.getTeleportListner();
	    isOnUpdater.isOnUpdater = isOnUpdater.getisOnUpdater();
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
                     for (Module m : ModuleManager.getModuleManager().modules) {
                    	 if (m.getKey() == keyCode) {
                    		 m.toggle();
                    	 }
                     }
                 }
             }
         } catch (Exception q) { q.printStackTrace(); }
    }
}
