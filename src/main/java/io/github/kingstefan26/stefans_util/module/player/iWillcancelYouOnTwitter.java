package io.github.kingstefan26.stefans_util.module.player;

import io.github.kingstefan26.stefans_util.core.preRewrite.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.preRewrite.module.Module;
import io.github.kingstefan26.stefans_util.core.preRewrite.setting.Setting;
import io.github.kingstefan26.stefans_util.core.preRewrite.setting.SettingsManager;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.ThreadLocalRandom;

public class iWillcancelYouOnTwitter extends Module {
    private long lastSpam;
    private double speed;
    String[] cancelTexts = {"Breathing oxygen is a cannceble offence!",
            "Party finder is a cancerous offence!",
            "Cwassh is gay!"};
    String randomText;
    public iWillcancelYouOnTwitter(){
        super("twitterWhiteGirls", "white twitter girls are the downfall on society", ModuleManager.Category.PLAYER, true);
        this.enableMessage = "iwillcancelyou enabled";
        this.disableMessage = "iwillcancelyou disabled";

    }
    @Override
    public void onLoad(){
        SettingsManager.getSettingsManager().rSetting(new Setting("cancel speed", this, 12, 1, 1000, true));
        super.onLoad();
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e){
        if(System.currentTimeMillis() - lastSpam > speed * 1000){
            randomText = cancelTexts[ThreadLocalRandom.current().nextInt(0, cancelTexts.length - 1)];
            chatService.queueClientChatMessage("§4[WATCHDOG ANNOUNCEMENT]" + "\n"
                    + "§fWatchdog has cannceled §l§c100§r players in the last 7 days." + "\n"
                    + "§fStaff have cannceled an additional §l§c100§r in the last 7 days." + "\n"
                    + "§r§l§c" + randomText, chatService.chatEnum.CHATNOPREFIX);
            lastSpam = System.currentTimeMillis();
        }
    }
    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent e){
        if(mc == null || mc.thePlayer == null || mc.theWorld == null) return;
        Minecraft mc = Minecraft.getMinecraft();
        hehe.drawTextAtWorld(randomText, (float)mc.thePlayer.posX + 25, (float)mc.thePlayer.posY, (float)mc.thePlayer.posZ, Integer.parseInt("ff0000", 16), 0.10F, false, true, e.partialTicks);
    }

    @Override
    public void onEnable(){
        super.onEnable();
        speed = SettingsManager.getSettingsManager().getSettingByName("cancel speed").getValDouble();
        lastSpam = System.currentTimeMillis();
    }

}
