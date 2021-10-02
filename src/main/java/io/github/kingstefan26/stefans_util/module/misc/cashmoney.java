package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.Category;
import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cashmoney extends Module {
    final static String regex = "§6\\[Auction\\] §7[a-zA-Z0-9_]{3,16} §ebought .+ §efor §[0-9,]+ coins §lCLICK";
    final static String sellRegex = "You sold .+ x[0-9]+ for [0-9,]+ Coins!";
    final static Pattern BinPattern = Pattern.compile(regex, Pattern.UNICODE_CASE);
    final static Pattern SellToNpcPattern = Pattern.compile(sellRegex);
    final static String[] sounds = {"stefan_util:cash", "stefan_util:win"};
    private String Match;

    public cashmoney() {
        super("cash money", "plays money sound on auction receive", Category.PLAYER);
    }
    //§6[Auction] §7paradaox_63 §ebought §fStone Bricks §efor §65 coins §lCLICK
    //ah
    //You collected 1 coins from selling Stone Bricks to paradaox_63 in an auction!
    //bin
    //You collected 5 coins from selling Stone Bricks to paradaox_63 in an auction!


// TextComponent{text='§6[Auction] §7paradaox_63 §ebought §fStone Bricks §efor §65 coins §lCLICK', siblings=[], style=Style{hasParent=false, color=null, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=ClickEvent{action=RUN_COMMAND, value='/viewauction 6d6aef8a-eaa9-4be5-bb47-b59444dc1bc9'}, hoverEvent=HoverEvent{action=SHOW_TEXT, value='TextComponent{text='Click to inspect!', siblings=[], style=Style{hasParent=false, color=§e, bold=false, italic=false, underlined=false, obfuscated=false, clickEvent=null, hoverEvent=null, insertion=null}}'}, insertion=null}}
// §6[Auction] §7paradaox_63 §ebought §fStone Bricks §efor §65 coins §lCLICK
// TextComponent{text='', siblings=[TextComponent{text='You collected ', siblings=[], style=Style{hasParent=true, color=§e, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}, TextComponent{text='5 coins ', siblings=[], style=Style{hasParent=true, color=§6, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}, TextComponent{text='from selling ', siblings=[], style=Style{hasParent=true, color=§e, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}, TextComponent{text='', siblings=[], style=Style{hasParent=true, color=§f, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}, TextComponent{text='Stone Bricks ', siblings=[], style=Style{hasParent=true, color=§f, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}, TextComponent{text='to ', siblings=[], style=Style{hasParent=true, color=§e, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}, TextComponent{text='paradaox_63 ', siblings=[], style=Style{hasParent=true, color=§7, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}, TextComponent{text='in an auction!', siblings=[], style=Style{hasParent=true, color=§e, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}], style=Style{hasParent=false, color=null, bold=null, italic=false, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}
// You collected 5 coins from selling Stone Bricks to paradaox_63 in an auction!

//    @Override
//    public void onEnable(){
//        super.onEnable();
//        try{
//            String a = sounds[ThreadLocalRandom.current().nextInt(0, 2)];
//            mc.thePlayer.playSound(a, 1F, 1F);
//        }catch(Exception ignored){}
//        //mc.thePlayer.getEntityWorld().playSoundAtEntity(mc.thePlayer, a, 1.0F, 1.0F);
//        //this.toggle();
//    }

    @Override
    public void onTick(TickEvent.Type type, Side side, TickEvent.Phase phase){
        if(this.Match != null){
            mc.thePlayer.playSound("stefan_util:cash", 1F, 1F);
            Match = null;
        }
    }


    @SubscribeEvent
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
