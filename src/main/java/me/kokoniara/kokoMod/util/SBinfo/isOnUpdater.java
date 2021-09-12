package me.kokoniara.kokoMod.util.SBinfo;

import me.kokoniara.kokoMod.util.handelers.ScoreboardHandler;
import me.kokoniara.kokoMod.util.forgeEventClasses.*;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * this uses a sigleton lolz
 * An instance of {@code isOnUpdater} can be retrieved with the following code:
 * <pre>{@code
 * isOnUpdater isOnUpdater = isOnUpdater.getisOnUpdater();
 * }</pre>
 */
public class isOnUpdater {
    public static isOnUpdater isOnUpdater;

    private isOnUpdater() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static isOnUpdater getisOnUpdater() {
        if (isOnUpdater == null)
            isOnUpdater = new isOnUpdater();
        return isOnUpdater;
    }

    private boolean isOnHypixel, inSkyblock, inDungeons, inPrivateIsland;


    Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void eventFirer(WorldEvent.Load event) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;

        boolean temp0 = isInSkyblock();
        boolean temp1 = isInDungeons();
        boolean temp2 = isOnPrivateIsland();


        if (temp0 && !inSkyblock) {
            MinecraftForge.EVENT_BUS.post(new joinedSkyblockEvent());
            inSkyblock = true;
        } else if (!temp0 && inSkyblock) {
            inSkyblock = false;
            MinecraftForge.EVENT_BUS.post(new leftSkyblockEvent());
        }

        if (temp1 && !inDungeons) {
            MinecraftForge.EVENT_BUS.post(new joinedDungeonEvent());
            inDungeons = true;
        } else if (!temp1 && inDungeons) {
            inDungeons = false;
            MinecraftForge.EVENT_BUS.post(new leftDungeonEvent());
        }

        if (temp2 && !inPrivateIsland) {
            MinecraftForge.EVENT_BUS.post(new joinedPrivateIslandEvent());
            inPrivateIsland = true;
        } else if (!temp2 && inPrivateIsland) {
            inPrivateIsland = false;
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
