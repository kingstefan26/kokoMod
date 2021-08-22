package me.kokoniara.kokoMod.util;

import me.kokoniara.kokoMod.handelers.ScoreboardHandler;
import me.kokoniara.kokoMod.kokoMod;
import me.kokoniara.kokoMod.util.forgeEventClasses.*;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;



/**
 * this uses a sigleton lolz
 *An instance of {@code isOnUpdater} can be retrieved with the following code:
 *<pre>{@code
 *isOnUpdater isOnUpdater = isOnUpdater.getisOnUpdater();
 *}</pre>
 */
public class isOnUpdater {
    private static isOnUpdater isOnUpdaterINSTASNCE;

    private isOnUpdater() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static isOnUpdater getisOnUpdater() {
        if(isOnUpdaterINSTASNCE == null)
            isOnUpdaterINSTASNCE = new isOnUpdater();
        return isOnUpdaterINSTASNCE;
    }

    private boolean isOnHypixel, inSkyblock, inDungeons, inPrivateIsland;


    Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void eventFireer(WorldEvent.Load event) {
        if( mc == null || mc.theWorld == null || mc.thePlayer == null ) return;

        boolean temp0 = isInSkyblock();
        boolean temp1 = isInDungeons();
        boolean temp2 = isOnPrivateIsland();


        if(temp0 && !inSkyblock){
            MinecraftForge.EVENT_BUS.post(new joinedSkyblock());
            inSkyblock = true;
        } else if(!temp0 && inSkyblock){
            inSkyblock = false;
            MinecraftForge.EVENT_BUS.post(new leftSkyblock());
        }

        if(temp1 && !inDungeons){
            MinecraftForge.EVENT_BUS.post(new joinedDungeon());
            inDungeons = true;
        } else if(!temp1 && inDungeons){
            inDungeons = false;
            MinecraftForge.EVENT_BUS.post(new leftDungeon());
        }

        if(temp2 && !inPrivateIsland){
            MinecraftForge.EVENT_BUS.post(new joinedPrivateIsland());
            inPrivateIsland = true;
        } else if(!temp2 && inPrivateIsland){
            inPrivateIsland = false;
            MinecraftForge.EVENT_BUS.post(new leftPrivateIsland());
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
                if (scObjName.contains("SKYBLOCK")) {
                    return true;
                }
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
