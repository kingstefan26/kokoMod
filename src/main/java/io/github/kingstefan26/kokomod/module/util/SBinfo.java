package io.github.kingstefan26.kokomod.module.util;

import io.github.kingstefan26.kokomod.core.module.blueprints.UtilModule;
import io.github.kingstefan26.kokomod.main;
import io.github.kingstefan26.kokomod.util.forgeEventClasses.*;
import io.github.kingstefan26.kokomod.util.handelers.ScoreboardHandler;
import io.github.kingstefan26.kokomod.util.sendChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class SBinfo extends UtilModule {
    public static SBinfo SBinfo;
    public static SBinfo getSBinfo(){
        if(SBinfo == null) SBinfo = new SBinfo();
        return SBinfo;
    }

    public SBinfo(){
        super("SBinfo");
    }

    public boolean inSkyblock, inDungeons, inPrivateIsland;

    @SubscribeEvent
    public void eventFirer(WorldEvent.Load event) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;

        boolean temp0 = isInSkyblock();
        boolean temp1 = isInDungeons();
        boolean temp2 = isOnPrivateIsland();


        if (temp0 && !inSkyblock) {
            MinecraftForge.EVENT_BUS.post(new joinedSkyblockEvent());
            if(main.debug) sendChatMessage.sendClientDebugMessage("joined Skyblock");
            inSkyblock = true;
        } else if (!temp0 && inSkyblock) {
            inSkyblock = false;
            if(main.debug) sendChatMessage.sendClientDebugMessage("left Skyblock");
            MinecraftForge.EVENT_BUS.post(new leftSkyblockEvent());
        }

        if (temp1 && !inDungeons) {
            MinecraftForge.EVENT_BUS.post(new joinedDungeonEvent());
            if(main.debug) sendChatMessage.sendClientDebugMessage("joined dungeons");
            inDungeons = true;
        } else if (!temp1 && inDungeons) {
            inDungeons = false;
            if(main.debug) sendChatMessage.sendClientDebugMessage("left Dungeons");
            MinecraftForge.EVENT_BUS.post(new leftDungeonEvent());
        }

        if (temp2 && !inPrivateIsland) {
            MinecraftForge.EVENT_BUS.post(new joinedPrivateIslandEvent());
            if(main.debug) sendChatMessage.sendClientDebugMessage("joined players private island");
            inPrivateIsland = true;
        } else if (!temp2 && inPrivateIsland) {
            inPrivateIsland = false;
            if(main.debug) sendChatMessage.sendClientDebugMessage("left players private island");
            MinecraftForge.EVENT_BUS.post(new leftPrivateIslandEvent());
        }
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


}
