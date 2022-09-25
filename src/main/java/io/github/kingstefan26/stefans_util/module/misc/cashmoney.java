package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class cashmoney extends BasicModule {
    static final String regex = "§6\\[Auction\\] §7[a-zA-Z0-9_]{3,16} §ebought .+ §efor §[0-9,]+ coins §lCLICK";
    static final String sellRegex = "You sold .+ x[0-9]+ for [0-9,]+ Coins!";
    static final Pattern BinPattern = Pattern.compile(regex, Pattern.UNICODE_CASE);
    static final Pattern SellToNpcPattern = Pattern.compile(sellRegex);
    static final String[] sounds = {"stefan_util:cash", "stefan_util:win"};
    private String match;

    public cashmoney() {
        super("cash money", "plays money sound on auction receive", ModuleManager.Category.MISC);
    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (this.match != null) {
            mc.thePlayer.playSound("stefan_util:cash", 1F, 1F);
            match = null;
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
                this.match = matcher.group(0);
            }
            while (Npcmatcher.find()) {
                this.logger.info("Full match: " + Npcmatcher.group(0));
                this.match = Npcmatcher.group(0);
            }
        }
    }
}
