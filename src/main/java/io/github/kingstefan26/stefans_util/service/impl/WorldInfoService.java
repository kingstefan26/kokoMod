package io.github.kingstefan26.stefans_util.service.impl;

import io.github.kingstefan26.stefans_util.service.Service;
import io.github.kingstefan26.stefans_util.util.StefanutilEvents;
import io.github.kingstefan26.stefans_util.util.handelers.ScoreboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

/**
 * i reallyyyy want to use /locraw for this PLEASE GIVE ME LOCRAW ITS SO MUCH EASIER
 */
public class WorldInfoService extends Service {
    public WorldInfoService() {
        super("SBinfo");
    }


    public static boolean inSkyblock, inDungeons, inPrivateIsland;

    int counter;
    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.START) return;

        counter++;
        if((counter % 20) != 0) return;
        counter = 0;

        if(mc.theWorld == null) return;

        List playerList = mc.theWorld.playerEntities;
        if (playerList.isEmpty()) return;


        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {

            boolean temp0 = isInSkyblock();
            boolean temp1 = isInDungeons();
            boolean temp2 = isOnPrivateIsland();
            boolean temp3 = isOnHypixel();
//            if (authmenager.getInstance().getCashedAuthObject().status.equals("dev")) {
//                chatService.queueClientChatMessage("hypixel:" + temp3 + " skyblock: " + temp0 + " dungeons: " + temp1 + " personal island: " + temp2, chatService.chatEnum.DEBUG);
//            }


            if (temp0 && !inSkyblock) {
                MinecraftForge.EVENT_BUS.post(new StefanutilEvents.joinedSkyblockEvent());
                //if(main.debug) chat.queueClientChatMessage("joined Skyblock", chat.chatEnum.DEBUG);

                inSkyblock = true;
            } else if (!temp0 && inSkyblock) {
                inSkyblock = false;
                //if(main.debug) chat.queueClientChatMessage("left Skyblock", chat.chatEnum.DEBUG);

                MinecraftForge.EVENT_BUS.post(new StefanutilEvents.leftSkyblockEvent());
            }

            if (temp1 && !inDungeons) {
                MinecraftForge.EVENT_BUS.post(new StefanutilEvents.joinedDungeonEvent());
                //if(main.debug) chat.queueClientChatMessage("joined dungeons", chat.chatEnum.DEBUG);

                inDungeons = true;
            } else if (!temp1 && inDungeons) {
                inDungeons = false;
                //if(main.debug) chat.queueClientChatMessage("left dungeons", chat.chatEnum.DEBUG);

                MinecraftForge.EVENT_BUS.post(new StefanutilEvents.leftDungeonEvent());
            }

            if (temp2 && !inPrivateIsland) {
                MinecraftForge.EVENT_BUS.post(new StefanutilEvents.joinedPrivateIslandEvent());

                //if(main.debug) chat.queueClientChatMessage("joined players private island", chat.chatEnum.DEBUG);
                inPrivateIsland = true;
            } else if (!temp2 && inPrivateIsland) {
                inPrivateIsland = false;
                // if(main.debug) chat.queueClientChatMessage("left players private island", chat.chatEnum.DEBUG);

                MinecraftForge.EVENT_BUS.post(new StefanutilEvents.leftPrivateIslandEvent());
            }


        }

    }

    @SubscribeEvent
    public void eee(WorldEvent.Load event) {

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
        if (mc.theWorld != null && !mc.isSingleplayer()) {
            ScoreObjective scoreboardObj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreboardObj != null) {
                String scObjName = ScoreboardHandler.cleanSB(scoreboardObj.getDisplayName());
                return scObjName.toLowerCase().contains("skyblock");
            }
        }
        return false;
    }

    public static boolean isInDungeons() {
        if (isInSkyblock()) {
//            List<String> scoreboard = ScoreboardHandler.getSidebarLines();
//            for (String s : scoreboard) {
//                String sCleaned = ScoreboardHandler.cleanSB(s);
//                if (sCleaned.contains("The Catacombs")) {
//                    return true;
//                }
//            }

            return ScoreboardHandler.getScoreboardAsCleanString().contains("The Catacombs");
        }
        return false;
    }

    public static boolean isOnPrivateIsland() {
        if (isInSkyblock()) {
//            List<String> scoreboard = ScoreboardHandler.getSidebarLines();
//            for (String s : scoreboard) {
//                String sCleaned = ScoreboardHandler.cleanSB(s);
//                System.out.println(sCleaned);
//                return sCleaned.toLowerCase().contains("your island");
//            }

            return ScoreboardHandler.getScoreboardAsCleanString().contains("Your Island");
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
