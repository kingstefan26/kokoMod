/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderNoDecimalSetting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.TimeUnit;

import static io.github.kingstefan26.stefans_util.core.module.modulemenagers.ModuleManager.Category.MISC;

public class autoEmptyMinions extends BasicModule {
    public autoEmptyMinions() {
        super("autoEmptyMinions", "empties the minion when you open its gui", MISC);
    }

    int closeDelay = 500;
    int clickDelay = 500;

    @Override
    public void onLoad() {
        new SliderNoDecimalSetting("closeGuiDelay", this, 500, 1, 1000, (newvalue) -> closeDelay = Math.toIntExact(Math.round(newvalue)));
        new SliderNoDecimalSetting("clickDelay", this, 500, 1, 1000, (newvalue) -> clickDelay = Math.toIntExact(Math.round(newvalue)));
        super.onLoad();
    }

    // 0 nothin; 1 clicking chest; 2 clicking hopper; 4 waiting to exit
    int state;
    int slotToClick;


    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        if(event.type != 2) {
            if(state == 1){
                switch(event.message.getUnformattedText()){
                    case "Your inventory does not have enough free space to add all items!":
                    case "This Minion does not have any items stored!":
                        state = 4;
                        closeGui();
                        break;
                }
            }
        }
    }

    long hold;


    @SubscribeEvent
    public void onGuiScreen(GuiScreenEvent.BackgroundDrawnEvent event) {
        if(state == 4) return;

        if(hold != 0 && slotToClick != 0){
            if(System.currentTimeMillis() >= hold){
                mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, slotToClick, 2, 0, mc.thePlayer);
                slotToClick = 0;
                hold = 0;

            }
            return;
        }

        if (event.gui instanceof GuiChest) {
            GuiChest inventory = (GuiChest) event.gui;
            Container containerChest = inventory.inventorySlots;
            if (containerChest instanceof ContainerChest) {

                String displayName = ((ContainerChest) containerChest).getLowerChestInventory().getDisplayName().getUnformattedText().trim();

                if (displayName.contains("Minion")) {
                    Container playerContainer = mc.thePlayer.openContainer;
                    IInventory chestInventory = ((ContainerChest)playerContainer).getLowerChestInventory();
                    for (int i = 37; i <= 50; i++) {
                        ItemStack itemStack = chestInventory.getStackInSlot(i);
                        if (itemStack != null) {
                            if (itemStack.getUnlocalizedName().toLowerCase().contains("chest")) {
                                hold = System.currentTimeMillis() + clickDelay;
                                state = 1;
                                slotToClick = i;
                            }
                            if (itemStack.getUnlocalizedName().toLowerCase().contains("hopper")) {
                                hold = System.currentTimeMillis() + clickDelay;
                                state = 2;
                                slotToClick = i;
                            }
                        }
                    }
                }
            }
        }
    }


    void closeGui(){

        (new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(closeDelay);
                if(mc.currentScreen != null){
                    mc.thePlayer.closeScreen();
                }
                hold = 0;
                state = 0;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        })).start();

    }

    @Override
    public void onDisable() {
        state = 0;
        slotToClick = 0;
        hold = 0;
        super.onDisable();
    }
}
