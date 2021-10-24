package io.github.kingstefan26.stefans_util.service.impl;

import io.github.kingstefan26.stefans_util.service.Service;
import io.github.kingstefan26.stefans_util.util.handelers.ScoreboardHandler;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class WorldInfoService extends Service {
    public WorldInfoService() {
        super("SBinfo");
    }


    public boolean inSkyblock, inDungeons, inPrivateIsland;

    @SubscribeEvent
    public void eee(WorldEvent.Load event) {
        if (mc.thePlayer == null) return;
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                //recheck();
                boolean temp0 = isInSkyblock();
                boolean temp1 = isInDungeons();
                boolean temp2 = isOnPrivateIsland();
                chatService.queueClientChatMessage("this is sent 1 second after worldload", chatService.chatEnum.DEBUG);
                chatService.queueClientChatMessage("skyblock: " + temp0 + " dungeons: " + temp1 + " personal island: " + temp2, chatService.chatEnum.DEBUG);


                if (temp0 && !inSkyblock) {
                    MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.joinedSkyblockEvent());
                    //if(main.debug) chat.queueClientChatMessage("joined Skyblock", chat.chatEnum.DEBUG);
                    chatService.queueClientChatMessage("joined Skyblock", chatService.chatEnum.DEBUG);

                    inSkyblock = true;
                } else if (!temp0 && inSkyblock) {
                    inSkyblock = false;
                    //if(main.debug) chat.queueClientChatMessage("left Skyblock", chat.chatEnum.DEBUG);
                    chatService.queueClientChatMessage("left Skyblock", chatService.chatEnum.DEBUG);
                    MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.leftSkyblockEvent());
                }

                if (temp1 && !inDungeons) {
                    MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.joinedDungeonEvent());
                    //if(main.debug) chat.queueClientChatMessage("joined dungeons", chat.chatEnum.DEBUG);
                    chatService.queueClientChatMessage("joined dungeons", chatService.chatEnum.DEBUG);
                    inDungeons = true;
                } else if (!temp1 && inDungeons) {
                    inDungeons = false;
                    //if(main.debug) chat.queueClientChatMessage("left dungeons", chat.chatEnum.DEBUG);
                    chatService.queueClientChatMessage("left dungeons", chatService.chatEnum.DEBUG);

                    MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.leftDungeonEvent());
                }

                if (temp2 && !inPrivateIsland) {
                    MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.joinedPrivateIslandEvent());
                    //if(main.debug) chat.queueClientChatMessage("joined players private island", chat.chatEnum.DEBUG);
                    chatService.queueClientChatMessage("joined players private island", chatService.chatEnum.DEBUG);
                    inPrivateIsland = true;
                } else if (!temp2 && inPrivateIsland) {
                    inPrivateIsland = false;
                    // if(main.debug) chat.queueClientChatMessage("left players private island", chat.chatEnum.DEBUG);
                    chatService.queueClientChatMessage("left players private island", chatService.chatEnum.DEBUG);
                    MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.leftPrivateIslandEvent());
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static boolean isOnHypixel() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.theWorld != null && !mc.isSingleplayer()) {
            return mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel");
        }
        return false;
    }

    public static boolean isInSkyblock() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.theWorld != null && !mc.isSingleplayer()) {
            ScoreObjective scoreboardObj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreboardObj != null) {
                String scObjName = ScoreboardHandler.cleanSB(scoreboardObj.getDisplayName());
                return scObjName.contains("SKYBLOCK");
            }
        }
        return false;
    }

    public static boolean isInDungeons() {
        if (isInSkyblock()) {
            List<String> scoreboard = ScoreboardHandler.getSidebarLines();
            for (String s : scoreboard) {
                String sCleaned = ScoreboardHandler.cleanSB(s);
                if (sCleaned.contains("The Catacombs")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isOnPrivateIsland() {
        if (isInSkyblock()) {
            List<String> scoreboard = ScoreboardHandler.getSidebarLines();
            for (String s : scoreboard) {
                String sCleaned = ScoreboardHandler.cleanSB(s);
                if (sCleaned.contains("Your Island")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void start() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void stop() {

    }
}
