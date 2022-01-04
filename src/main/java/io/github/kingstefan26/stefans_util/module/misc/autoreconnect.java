/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

public class autoreconnect extends basicModule {
    private GuiScreen guiCache;

    public static boolean onGuiDisconnected = false;
    public static boolean onGuiConnecting = false;
    public static boolean isEnabled = false;
    public static byte reconnectDelayUpdater = 20;
    public static int tryNumber = 0;
    public static int reconnectDelay = 5;
    public static int connectingTimer = 300;
    public static String host;

    Minecraft mc = Minecraft.getMinecraft();

    public autoreconnect() {
        super("autorecorrect", "automagically reconnects", moduleManager.Category.MISC, new presistanceDecorator());
    }

    boolean preventSpam;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (host == null && mc.theWorld != null && mc.thePlayer != null) {


            if (!Minecraft.getMinecraft().isSingleplayer()) {
                autoreconnect.tryNumber = 0;
                autoreconnect.connectingTimer = 300;
                autoreconnect.onGuiConnecting = false;
                host = Minecraft.getMinecraft().getCurrentServerData().serverIP;
                preventSpam = true;
                chatService.queueClientChatMessage("Auto Reconnect enabled. IP/Port: " + host);
            } else {
                if(preventSpam){
                    chatService.queueClientChatMessage("Auto Reconnect does not work in singleplayer");
                    preventSpam = true;
                }
            }
            autoreconnect.isEnabled = true;

        }


        if (event.phase == Phase.START) {
            // this part of the code ticks down the delay, its happening every tick
            if (onGuiDisconnected && reconnectDelay != 0) {
                reconnectDelayUpdater -= 1;
            }
            if (onGuiConnecting) {
                connectingTimer -= 1;
                if (connectingTimer <= 0) {
                    onGuiDisconnected = true;
                    mc.displayGuiScreen(new GuiScreen() {
                        public void drawScreen(int par1, int par2, float par3) {
                            this.drawDefaultBackground();
                            this.drawCenteredString(this.fontRendererObj, "The connection time has expired. Retrying", this.width / 2, this.height / 2 - 50, 16777215);
                            if (autoreconnect.connectingTimer <= -80) {
                                autoreconnect.connectingTimer = 300;
                                autoreconnect.onGuiConnecting = false;
                                FMLClientHandler.instance().connectToServer(new GuiMainMenu(), new ServerData("server", host, false));
                            }
                            super.drawScreen(par1, par2, par3);
                        }
                    });
                }
            }
            if (mc.currentScreen != guiCache) {
                guiCache = mc.currentScreen;
                if (guiCache instanceof GuiDisconnected) {
                    onGuiConnecting = false;
                    if (host != null) {
                        DisconnectedScreen reconnectEvent = new DisconnectedScreen((GuiDisconnected) guiCache);
                        onGuiDisconnected = true;
                        mc.displayGuiScreen(reconnectEvent);
                    }
                }
                if (guiCache instanceof GuiConnecting && host != null) {
                    onGuiConnecting = true;
                }
                if (guiCache instanceof GuiMainMenu)
                    isEnabled = false;
            }
        }
    }

    @SubscribeEvent
    public void onJoinServerMessage(EntityJoinWorldEvent event) {
        if (!autoreconnect.isEnabled) {
            if (event.entity instanceof EntityPlayer) {
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                    EntityPlayer player = (EntityPlayer) event.entity;
                    if (!Minecraft.getMinecraft().isSingleplayer()) {
                        autoreconnect.tryNumber = 0;
                        autoreconnect.connectingTimer = 300;
                        autoreconnect.onGuiConnecting = false;
                        host = Minecraft.getMinecraft().getCurrentServerData().serverIP;
                        player.addChatMessage(new ChatComponentText("Auto Reconnect enabled. IP/Port: " + host));
                    } else {
                        player.addChatMessage(new ChatComponentText("Auto Reconnect does not work in singleplayer"));
                    }
                    autoreconnect.isEnabled = true;
                }
            }
        }
    }

    public static class DisconnectedScreen extends GuiScreen {

        public String errorMessage;
        public IChatComponent errorDetail;
        @SuppressWarnings("rawtypes")
        public List list;

        public final GuiScreen parent;

        @SuppressWarnings("rawtypes")
        public DisconnectedScreen(GuiDisconnected disconnected) throws RuntimeException {
            try {
                Field[] fields = GuiDisconnected.class.getDeclaredFields();
                for (Field f : fields)
                    f.setAccessible(true);
                errorMessage = (String) fields[0].get(disconnected);
                errorDetail = (IChatComponent) fields[1].get(disconnected);
                list = (List) fields[2].get(disconnected);
                parent = (GuiScreen) fields[3].get(disconnected);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void initGui() {
            autoreconnect.tryNumber += 1;
            autoreconnect.isEnabled = false;
            this.buttonList.clear();
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Go back to menu"));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 162, "Cancel"));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 141, "Reconnect in" + autoreconnect.reconnectDelay + "..."));
            this.list = this.fontRendererObj.listFormattedStringToWidth(errorDetail.getFormattedText(), this.width - 50);
        }

        protected void actionPerformed(GuiButton button) {
            if (button.id == 0) {
                autoreconnect.onGuiDisconnected = false;
                autoreconnect.tryNumber = 0;
                host = null;
                GuiMultiplayer multiplayer = new GuiMultiplayer(parent);
                mc.displayGuiScreen(multiplayer);
            }
            if (button.id == 1) {
                autoreconnect.onGuiDisconnected = false;
                autoreconnect.tryNumber = 0;
                host = null;
                GuiDisconnected disconnected = new GuiDisconnected(parent, errorMessage, errorDetail);
                mc.displayGuiScreen(disconnected);
            }
            if (button.id == 2) {
                autoreconnect.onGuiDisconnected = false;
                FMLClientHandler.instance().connectToServer(new GuiMainMenu(), new ServerData("server", host, false));
            }
        }

        @SuppressWarnings("rawtypes")
        public void drawScreen(int par1, int par2, float par3) {
            if (autoreconnect.reconnectDelay == 0) {
                autoreconnect.onGuiDisconnected = false;
                FMLClientHandler.instance().connectToServer(new GuiMainMenu(), new ServerData("server", host, false));
            }
            if (autoreconnect.reconnectDelayUpdater <= 0) {
                autoreconnect.reconnectDelay -= 1;
                autoreconnect.reconnectDelayUpdater = 20;
                this.buttonList.remove(2);
                this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 141, "Reconnecting in " + autoreconnect.reconnectDelay + "..."));
            }
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, this.errorMessage, this.width / 2, this.height / 2 - 50, 11184810);
            this.drawCenteredString(this.fontRendererObj, "Attemp #" + autoreconnect.tryNumber, this.width / 2, 10, 11184810);
            int k = this.height / 2 - 30;
            if (list != null) {
                for (Iterator iterator = list.iterator(); iterator.hasNext(); k += this.fontRendererObj.FONT_HEIGHT) {
                    String s = (String) iterator.next();
                    this.drawCenteredString(this.fontRendererObj, s, this.width / 2, k, 16777215);
                }
            }
            super.drawScreen(par1, par2, par3);
        }
    }
}