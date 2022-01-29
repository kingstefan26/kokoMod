/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.prototypeModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NbtItemTest extends prototypeModule {
    static final String regex = "\\+[0-9\\.\\,]+";


    //  ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
//    §3§lBESTIARY §b§lPigman §bI
//
//               REWARDS
//     §7§8+§a1 Pigman §b✯ Magic Find
//     §7§8+§a1 Pigman §c❁ Strength
//     §7§8+§61% §aPigman §7coins
//     §7§8+§a20% §7chance for extra XP orbs
//     §7§fCommon Loot §7Info
// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
    static final Pattern mobexpregx = Pattern.compile(regex, Pattern.MULTILINE);

    //84/112+6Combat(88/125)103/103Mana
    //+6Combat(88/125)

    //2537/2237+22.7Combat(47.39%)1546/1546Mana
    //+22.7Combat(47.39%)

    // \\+[0-9\\.\\,]+
    String lastXp = null;
    long lastUpdate;

    public NbtItemTest() {
        super("NbtItemTest");
    }

    @Override
    protected void PROTOTYPETEST() {
        ItemStack stack = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();

            //if there are ExtraAttributes in nbt tags
            if (tag.hasKey("ExtraAttributes", 10)) {
                //get the ExtraAttributes
                NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");
                chatService.queueCleanChatMessage(ea.toString());
            }
        }
    }

    @SubscribeEvent
    public void livingDeath(LivingDeathEvent event) {
        if (/* event.source.getSourceOfDamage() == mc.thePlayer &&*/ mc.thePlayer.getDistanceToEntity(event.entity) < 5) {
            chatService.queueCleanChatMessage(event.entity.toString() /*+ " died by:" + event.source.getSourceOfDamage().toString()*/);
            chatService.queueCleanChatMessage(event.entity.getDisplayName().getFormattedText());
        }
    }

    @Override
    public void onChat(ClientChatReceivedEvent e) {
        if (e.type == 2) {
            final Matcher matcher = mobexpregx.matcher(StringUtils.stripControlCodes(e.message.getUnformattedText()).replaceAll("[^\\x00-\\x7F]", "").replaceAll(" ", ""));
//            this.logger.info(e.message.getUnformattedText());
            String is = null;
            while (matcher.find()) {
                is = matcher.group(0);
                this.logger.info("Full match: " + is);
            }


            if (is == null && lastXp != null) {
                lastXp = null;
                lastUpdate = 0;
            }

            if (is != null && lastXp == null) lastUpdate = System.currentTimeMillis();


            lastXp = is;

//            chatService.queueClientChatMessage(StringUtils.stripControlCodes(e.message.getUnformattedText()).replaceAll("[^\\x00-\\x7F]", "").replaceAll(" ", ""));
        }
    }
}
