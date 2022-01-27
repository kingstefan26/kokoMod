/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.util;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.util.Collections.emptyList;

public class TabListUtil {
    private static final Ordering<NetworkPlayerInfo> playerInfoOrdering = new Ordering<NetworkPlayerInfo>() {
        @Override
        public int compare(@Nullable NetworkPlayerInfo left, @Nullable NetworkPlayerInfo right) {
            if (left != null && right != null) {
                ScorePlayerTeam playerTeam = left.getPlayerTeam();
                ScorePlayerTeam playerTeam1 = right.getPlayerTeam();
                return ComparisonChain.start().compareTrueFirst(
                        left.getGameType() != WorldSettings.GameType.SPECTATOR,
                        right.getGameType() != WorldSettings.GameType.SPECTATOR
                ).compare(
                        playerTeam != null ? playerTeam.getRegisteredName() : "",
                        playerTeam1 != null ? playerTeam1.getRegisteredName() : ""
                ).compare(left.getGameProfile().getName(), right.getGameProfile().getName()).result();
            }

            return -1;
        }
    };


    private TabListUtil() {
    }

    public static List<NetworkPlayerInfo> fetchTabEntires() {
        return Minecraft.getMinecraft().thePlayer == null ? emptyList() : playerInfoOrdering.sortedCopy(Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());
    }

    public static String getTabList() {
        NetHandlerPlayClient nethandlerplayclient = Minecraft.getMinecraft().thePlayer.sendQueue;
        NetworkPlayerInfo[] huh = nethandlerplayclient.getPlayerInfoMap().toArray(new NetworkPlayerInfo[0]);
        StringBuilder s = new StringBuilder();
        for (NetworkPlayerInfo networkPlayerInfo : huh) {
            s.append(networkPlayerInfo.getDisplayName().getUnformattedText());
        }
        return s.toString();
    }
}
