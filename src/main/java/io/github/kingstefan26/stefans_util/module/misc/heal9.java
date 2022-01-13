/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

import static io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager.Category.MISC;

public class heal9 extends basicModule {
    public heal9() {
        super("heal9", "the true challenger of simon hypixel, some will call this a harp bot but i refuse", MISC);
    }

    static String[] harpInv = new String[54];


    @SubscribeEvent
    public void onGuiScreen(GuiScreenEvent.BackgroundDrawnEvent event) {

        if (event.gui instanceof GuiChest) {

            GuiChest inventory = (GuiChest) event.gui;
            Container containerChest = inventory.inventorySlots;
            if (containerChest instanceof ContainerChest) {

                String displayName = ((ContainerChest) containerChest).getLowerChestInventory().getDisplayName().getUnformattedText().trim();

                if (displayName.contains("Harp")) {
                    String[] currentInv = new String[54];
                    Container playerContainer = mc.thePlayer.openContainer;
                    IInventory chestInventory = ((ContainerChest)playerContainer).getLowerChestInventory();
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
                                    mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, 2, 0, mc.thePlayer);
                                    chatService.queueCleanChatMessage("clicked");
                                    harpInv = currentInv;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
