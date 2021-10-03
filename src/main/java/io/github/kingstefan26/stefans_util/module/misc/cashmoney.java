package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.Module;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class cashmoney extends Module {
    final static String regex = "§6\\[Auction\\] §7[a-zA-Z0-9_]{3,16} §ebought .+ §efor §[0-9,]+ coins §lCLICK";
    final static String sellRegex = "You sold .+ x[0-9]+ for [0-9,]+ Coins!";
    final static Pattern BinPattern = Pattern.compile(regex, Pattern.UNICODE_CASE);
    final static Pattern SellToNpcPattern = Pattern.compile(sellRegex);
    final static String[] sounds = {"stefan_util:cash", "stefan_util:win"};
    private String Match;

    public cashmoney() {
        super("cash money", "plays money sound on auction receive", ModuleManager.Category.PLAYER);
    }
    //§6[Auction] §7paradaox_63 §ebought §fStone Bricks §efor §65 coins §lCLICK
    //ah
    //You collected 1 coins from selling Stone Bricks to paradaox_63 in an auction!
    //bin
    //You collected 5 coins from selling Stone Bricks to paradaox_63 in an auction!


    @Override
    public void onTick(TickEvent.ClientTickEvent e){
        if(this.Match != null){
            mc.thePlayer.playSound("stefan_util:cash", 1F, 1F);
            Match = null;
        }
    }


    @Override
    public void onChat(ClientChatReceivedEvent e){
        if(e.type != 2){

            final Matcher matcher = BinPattern.matcher(e.message.getUnformattedText());
            final Matcher Npcmatcher = SellToNpcPattern.matcher(e.message.getUnformattedText());
            this.logger.info(e.message.getUnformattedText());

            while (matcher.find()) {
                this.logger.info("Full match: " + matcher.group(0));
                this.Match = matcher.group(0);
            }
            while (Npcmatcher.find()) {
                this.logger.info("Full match: " + Npcmatcher.group(0));
                this.Match = Npcmatcher.group(0);
            }
        }
    }
}
