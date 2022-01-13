/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.DecimalFormat;

public class macroSessionTracker extends basicModule {
    public macroSessionTracker() {
        super("macroSessionTracker", "traks ur macro session, aka crops money etc", moduleManager.Category.RENDER);
    }

    final String[] GuiScreenText = new String[]{"null", "null", "null", "null"};

    static int cropsMinedThisSession;
    static int totalMinedCropsWithKokomod;

    Tuple<Integer, String> originalCropValue;

    int lastUpdateCounter;


    long thisSessionFarmingTime;
    long lastUpdate;
    long thisUpdate;

    configObject totalCrops;
    @Override
    public void onLoad() {
        totalCrops = new configObject("totalCropsByKokomod", this.getName(), 0);
        totalMinedCropsWithKokomod = totalCrops.getIntValue();
        super.onLoad();
    }


    boolean shouldRender = true;

    String getCurrentItemUuid() {
        if (Minecraft.getMinecraft().thePlayer == null) return null;

        ItemStack stack = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();

            //if there are ExtraAttributes in nbt tags
            if (tag.hasKey("ExtraAttributes", 10)) {
                //get the ExtraAttributes
                NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");

                if (ea.hasKey("uuid")) {
                    return ea.getString("uuid");
                }
            }
        }
        return null;
    }


    int getCurrentItemCrops() {
        if (Minecraft.getMinecraft().thePlayer == null) return 0;

        ItemStack stack = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();

            //if there are ExtraAttributes in nbt tags
            if (tag.hasKey("ExtraAttributes", 10)) {
                //get the ExtraAttributes
                NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");

                if (ea.hasKey("mined_crops", 99)) {
                    //get the value of mined_crops from ExtraAttributes
                    //and add it into counterqueue
                    return ea.getInteger("mined_crops");

                    //do the same for farmed_cultivating
                }
            }
        }
        return 0;
    }


    int tickCouter;

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START) return;
        tickCouter++;
        if (tickCouter % 20 != 0) return;
        tickCouter = 0;

        if(!WorldInfoService.isInSkyblock() && mc.thePlayer == null) return;


        String temp = getCurrentItemUuid();
        if(originalCropValue == null){
            originalCropValue = new Tuple<>(getCurrentItemCrops(), temp);
        }

        if(!originalCropValue.getSecond().equals(temp) || mc.currentScreen != null) {
            shouldRender = false;
            return;
        }else{
            shouldRender = true;
        }

        if (mc.thePlayer.getHeldItem() != null) {

            ItemStack hand = mc.thePlayer.getHeldItem();
            if (hand.getItem().getRegistryName().toLowerCase().contains("hoe")) {
                // update this here

                int currentTotal = getCurrentItemCrops() - originalCropValue.getFirst();

                if(currentTotal != 0){

                    int change = currentTotal - lastUpdateCounter;
                    cropsMinedThisSession += change;
                    logger.info("mined since last update " + change);
                    lastUpdateCounter = currentTotal;



                    totalCrops.setIntValue(totalMinedCropsWithKokomod + cropsMinedThisSession);

                    shouldRender = true;
                    GuiScreenText[0] = "Ø crops/this session " + coolFormat(cropsMinedThisSession);
                    GuiScreenText[1] = "Ø money/this session " + coolFormat(cropsMinedThisSession * 3);
                    GuiScreenText[2] = "Ø crops/total " + coolFormat(totalMinedCropsWithKokomod + cropsMinedThisSession);
                    GuiScreenText[3] = "Ø money/total " + coolFormat(totalMinedCropsWithKokomod + cropsMinedThisSession * 3);
                }
            } else {
                shouldRender = false;
            }
        }

    }


    private static final String[] suffix = new String[]{"","k", "m", "b", "t"};
    private static final int MAX_LENGTH = 4;

    private static String coolFormat(double number) {
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while(r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")){
            r = r.substring(0, r.length()-2) + r.substring(r.length() - 1);
        }
        return r;
    }


    @Override
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        if (mc.currentScreen != null) return;
        if (shouldRender) {

            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            int y = 2 + (fr.FONT_HEIGHT * 3);
            for (String s : GuiScreenText) {
                fr.drawString(s, 1, y, 0xffff00, true);

                y += fr.FONT_HEIGHT;
            }
        }
    }

}
