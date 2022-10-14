/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static io.github.kingstefan26.stefans_util.core.module.ModuleManager.Category.MISC;

public class heal9 extends BasicModule {
    ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    private boolean failfast = true;
    private boolean predictive;
    private boolean lastShoulddraw;
    private boolean shoulddraw;
    private long timer;

    static String[] harpInv = new String[54];

    public heal9() {
        super("heal9", "the true challenger of simon hypixel, some will call this a harp bot but i refuse", MISC);
        new CheckSetting("predictive clicking", this, true, n -> this.predictive = n);
        new CheckSetting("Fail fast", this, true, n -> this.failfast = n, "As soon as a click is missed close gui");

    }

    @SubscribeEvent
    public void onGuiScreen(GuiScreenEvent.DrawScreenEvent event) {
        if (lastShoulddraw != shoulddraw) {
            timer = System.currentTimeMillis() + 50000;
            logger.info("draw timre reset");
        }
        if (shoulddraw) {
            String left = "LEFT: " + (timer - System.currentTimeMillis());
            drawCenterString.drawStringWereeverIWant(mc, left, 0xFFFFFF, 100, 200);
        }
    }

    @SubscribeEvent
    public void onGuiScreen(GuiScreenEvent.BackgroundDrawnEvent event) {

        lastShoulddraw = shoulddraw;
        shoulddraw = false;
        if (!(event.gui instanceof GuiChest)) return;

        GuiChest inventory = (GuiChest) event.gui;
        Container containerChest = inventory.inventorySlots;

        if (!(containerChest instanceof ContainerChest)) return;


        String displayName = ((ContainerChest) containerChest).getLowerChestInventory().getDisplayName().getUnformattedText().trim();
        if (!displayName.contains("Harp")) return;
        shoulddraw = true;

        String[] currentInv = new String[54];
        Container playerContainer = mc.thePlayer.openContainer;
        IInventory chestInventory = ((ContainerChest) playerContainer).getLowerChestInventory();

        if (predictive) {
            for (int i = 28; i <= 34; i++) {
                ItemStack itemStack = chestInventory.getStackInSlot(i);
                if (itemStack == null) continue;

                if (!itemStack.getUnlocalizedName().toLowerCase().contains("cloth")) continue;

                for (int j = 0; j <= 53; j++) {
                    ItemStack name = chestInventory.getStackInSlot(j);
                    if (name != null)
                        currentInv[j] = name.toString();
                }

                if (!Arrays.toString(currentInv).equals(Arrays.toString(harpInv))) {
                    int finalI = i;
                    // when playing La Vie en Rose there are two consequtive blocks on 32 i added
                    // this small patch bc i am not waiting + more clicks != worse
                    if (finalI == 32) {

                        ItemStack pussystack = chestInventory.getStackInSlot(25);
                        if (pussystack != null && pussystack.getUnlocalizedName().toLowerCase().contains("cloth")) {
                            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, finalI, 2, 3, mc.thePlayer);
                        }

                    }
                    executor.submit(() -> {
                        try {
                            // wait a tick
                            Thread.sleep(50);
                            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, finalI, 2, 3, mc.thePlayer);
//                            chatService.queueCleanChatMessage("clicked");
                            logger.info("clicked");
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    harpInv = currentInv;
                }

            }
        } else {
            for (int i = 37; i <= 43; i++) {
                ItemStack itemStack = chestInventory.getStackInSlot(i);
                if (itemStack != null) {
                    if (itemStack.getUnlocalizedName().toLowerCase().contains("quartz")) {
                        for (int j = 0; j <= 53; j++) {
                            ItemStack name = chestInventory.getStackInSlot(j);
                            if (name != null)
                                currentInv[j] = name.toString();
                        }
                        if (!Arrays.toString(currentInv).equals(Arrays.toString(harpInv))) {
                            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, 2, 3, mc.thePlayer);
//                            chatService.queueCleanChatMessage("clicked");
                            logger.info("clicked");
                            harpInv = currentInv;
                        }
                    }
                }
            }


        }

        if (failfast) {
            for (int i = 46; i <= 53; i++) {
                if (i == 51) continue;
                ItemStack itemStack = chestInventory.getStackInSlot(i);
                if (itemStack == null) continue;
                if (itemStack.getUnlocalizedName().toLowerCase().contains("cloth")) {

                    logger.info("closing inventory");
                    mc.thePlayer.closeScreen();
                }
            }
        }


    }
}
