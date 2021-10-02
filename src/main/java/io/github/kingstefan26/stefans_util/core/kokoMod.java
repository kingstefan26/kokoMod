package io.github.kingstefan26.stefans_util.core;

import io.github.kingstefan26.stefans_util.core.clickgui.ClickGui;
import io.github.kingstefan26.stefans_util.core.config.configMenager;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import io.github.kingstefan26.stefans_util.module.util.chat;
import io.github.kingstefan26.stefans_util.util.handelers.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import static io.github.kingstefan26.stefans_util.main.VERSION;

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

					ChatComponentText a = new ChatComponentText("§bThis version of Cock Mod™ "
							+ VERSION +
							" does not contain stds nor aids! §oall rights reserved§o§r Session id steal-er privacy policy §nhere§n");
					a.getChatStyle().setChatClickEvent(new ClickEvent(net.minecraft.event.ClickEvent.Action.OPEN_URL,
					"https://youtu.be/dQw4w9WgXcQ"));

					chat.queueClientChatMessage(a, chat.chatEnum.CHATCOMPONENT);

		        }
	        }
        }
    }

	@SubscribeEvent
	public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		event.manager.channel().pipeline().addBefore("packet_handler", "kingstefan26_packet_handler", new PacketHandler());
		System.out.println("Added packet handler to channel pipeline.");
	}

    public void init() {
    	MinecraftForge.EVENT_BUS.register(this);
	    configMenager.configMenager = configMenager.getConfigManager();
	    SettingsManager.SettingsManager = SettingsManager.getSettingsManager();
	    ModuleManager.ModuleManager = ModuleManager.getModuleManager();
	    ClickGui.ClickGui = ClickGui.getClickGui();
    }
}
