/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.module.modulemenagers.ModuleManager;
import io.github.kingstefan26.stefans_util.core.setting.attnotationSettings.attnotaions.slidernodecimalsetting;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.ThreadLocalRandom;

public class iWillcancelYouOnTwitter extends BasicModule {
    private long lastSpam;

    @slidernodecimalsetting(name = "cancel speed", defaultValue = 12, min = 1, max = 100)
    private double speed;

    String[] cancelTexts = {"Breathing oxygen is a cannceble offence!",
            "Party finder is a cancerous offence!",
            "Shady addons are actually gray!"};
    String randomText;

    public iWillcancelYouOnTwitter() {
        super("twitterWhiteGirls", "white twitter girls are the downfall on society", ModuleManager.Category.MISC, new presistanceDecorator());
    }


    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e){
        if(System.currentTimeMillis() - lastSpam > speed * 1000){
            randomText = cancelTexts[ThreadLocalRandom.current().nextInt(0, cancelTexts.length - 1)];
            chatService.queueClientChatMessage("§4[WATCHDOG ANNOUNCEMENT]" + "\n"
                    + "§fWatchdog has cannceled §l§c" + ThreadLocalRandom.current().nextInt(10000, 50000) + "§r players in the last 7 days." + "\n"
                    + "§fStaff have cannceled an additional §l§c" + ThreadLocalRandom.current().nextInt(5000, 20000) + "§r in the last 7 days." + "\n"
                    + "§r§l§c" + randomText, chatService.chatEnum.NOPREFIX);
            lastSpam = System.currentTimeMillis();
        }
    }


    @Override
    public void onWorldRender(RenderWorldLastEvent e) {
        super.onWorldRender(e);
        if (mc == null || mc.thePlayer == null || mc.theWorld == null) return;
        hehe.drawTextAtWorld(randomText == null ? cancelTexts[0] : randomText, (float) mc.thePlayer.posX + 25, (float) mc.thePlayer.posY, (float) mc.thePlayer.posZ, Integer.parseInt("ff0000", 16), 0.10F, false, true, e.partialTicks);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        lastSpam = System.currentTimeMillis();
    }

}
