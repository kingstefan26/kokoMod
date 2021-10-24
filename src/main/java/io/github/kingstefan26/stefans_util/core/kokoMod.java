package io.github.kingstefan26.stefans_util.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.kingstefan26.stefans_util.core.rewrite.clickGui.newClickGui;
import io.github.kingstefan26.stefans_util.core.clickgui.ClickGui;
import io.github.kingstefan26.stefans_util.core.config.configMenager;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.general.SettingsCore;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import io.github.kingstefan26.stefans_util.util.handelers.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static io.github.kingstefan26.stefans_util.main.VERSION;
import static io.github.kingstefan26.stefans_util.main.debug;

public class kokoMod {
	public static JsonObject data = null;
	public static HashMap<String, String> alowedUsers = new HashMap<>();
	static boolean isAllowedToPlay = false;

	public static kokoMod instance;
    public static kokoMod getkokoMod(){
    	if(instance == null) instance = new kokoMod();
    	return instance;
    }



	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent ev) throws Throwable {
		try {
			if (ev.side == Side.SERVER) return;
			if (ev.phase == TickEvent.Phase.START) {
				if(!isAllowedToPlay){
					if (Minecraft.getMinecraft().currentScreen instanceof GuiErrorScreen) return;

                    Minecraft.getMinecraft().displayGuiScreen(new GuiErrorScreen("you fool are not allowed to use this mod.",
							"smh") {
                        @Override
                        public void initGui() {
                            super.initGui();
                            this.buttonList.clear();
                            this.buttonList.add(new GuiButton(0, width / 2 - 50, height - 50, 100, 20, "close"));
                        }

                        @Override
                        protected void actionPerformed(GuiButton button){
                            FMLCommonHandler.instance().exitJava(-1, true);
                        }
                    });
                }
			}
		} catch (Exception ignored){
			FMLCommonHandler.instance().exitJava(-1, true);
		}
	}


    public boolean firstStartup = true;
	boolean lockfirstStartup = false;

    @SubscribeEvent
    public void onFirstWorldJoin(TickEvent.ClientTickEvent e){
		if(main.firstStartup && !lockfirstStartup){
			Minecraft mc = Minecraft.getMinecraft();
			if(mc != null){
				if(mc.thePlayer != null || mc.theWorld != null){
					String[] temp = {
							"Hi its seems like its first time using kokoMod",
							"so welcome...",
							"you will quickly regret using this mod (｀ڼ´)",
							"§4this is gonna be a fun time "
					};
					for(String t : temp){
						chatService.queueCleanChatMessage(t);
					}
					lockfirstStartup = true;
				}
			}
		}else{
			if(firstStartup){
				Minecraft mc = Minecraft.getMinecraft();
				if(mc != null){
					if(mc.thePlayer != null || mc.theWorld != null){
						firstStartup = false;
						ChatComponentText b = new ChatComponentText("§bThis version of Cock Mod™ "
								+ VERSION);
						b.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "KokoModPrivacyPolicy"));

						chatService.queueClientChatMessage(b, chatService.chatEnum.CHATCOMPONENT);

						ChatComponentText a = new ChatComponentText(
								" does not contain stds nor aids! §oall rights reserved§o§r Session id steal-er privacy policy §nhere§n");
						a.getChatStyle().setChatClickEvent(new ClickEvent(net.minecraft.event.ClickEvent.Action.OPEN_URL,
								"https://youtu.be/dQw4w9WgXcQ"));
						chatService.queueClientChatMessage(a, chatService.chatEnum.CHATCOMPONENT);

						chatService.lockEnableMessages = false;
					}
				}
			}
		}
    }

	public static void refreshRepo(Logger logger, ProgressManager.ProgressBar progressBar) {
		alowedUsers.clear();
		progressBar.step("(1/2) Downloading allowed users");
		data = APIHandler.getResponse("https://raw.githubusercontent.com/kingstefan26/cockmod-data/main/data.json");
		if(debug) logger.info(data != null && data.has("alowed") ? "loaded data from github" : "failed to load data from github");
		progressBar.step("(2/2) Parsing allowed users");
		Set<Map.Entry<String, JsonElement>> entrySet = data.get("alowed").getAsJsonObject().entrySet();
		for(Map.Entry<String,JsonElement> entry : entrySet){
			alowedUsers.put(entry.getKey(), entry.getValue().getAsString());
		}

		if(debug) logger.info("current user uuid: " + Minecraft.getMinecraft().getSession().getProfile().getId().toString().replace("-", ""));

		isAllowedToPlay = alowedUsers.containsValue(Minecraft.getMinecraft().getSession().getProfile().getId().toString().replace("-", ""));

		if(debug){
			logger.info("Whitelisted users:");
			alowedUsers.forEach((K, V)  -> logger.info(K + " uuid:" + V));
		}
	}


//	public static void checkWhitelist() {
//		JsonObject data = APIHandler.getResponse("http://auth.kingstefan26.workers.dev/?uuid=" + Minecraft.getMinecraft().getSession().getProfile().getId().toString().replace("-", ""));
//		try{
//			System.out.println("AAAAAAAAAAAAA " + data.toString());
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//	}


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

		moduleManager.getModuleManager();
		SettingsCore.getSettingsCore();
		newClickGui.getClickGui();
    }
}