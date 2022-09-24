/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class guiClickTest extends BasicModule {
    public guiClickTest() {
        super("testingGui", "test", ModuleManager.Category.DEBUG);
    }

    boolean close;

    @SubscribeEvent
    public void onGuiScreen(GuiScreenEvent.BackgroundDrawnEvent event) {

        if (event.gui instanceof GuiChest && !close) {
            close = true;
            GuiChest inventory = (GuiChest) event.gui;
            Container containerChest = inventory.inventorySlots;
            if (containerChest instanceof ContainerChest) {

                Container playerContainer = mc.thePlayer.openContainer;
                IInventory chestInventory = ((ContainerChest)playerContainer).getLowerChestInventory();
                for (int i = 0; i <= 53; i++) {
                    ItemStack itemStack = chestInventory.getStackInSlot(i);
                    if (itemStack != null) {
                        logger.info(i);
                        logger.info(itemStack.getUnlocalizedName().toLowerCase());
                    }
                }
            }
        }
    }


    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if(mc.currentScreen == null){
            close = false;
        }
    }

    @SubscribeEvent
    public void DaDSAdisint(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.gui instanceof GuiChest)) return;

        event.buttonList.add(new GuiButton(1000,
                mc.currentScreen.width / 2,
                mc.currentScreen.height / 16,
                mc.fontRendererObj.getStringWidth("Up") + 5,
                12,
                "Up"));
        event.buttonList.add(new GuiButton(1001,
                mc.currentScreen.width / 2,
                mc.currentScreen.height / 16 + 14,
                mc.fontRendererObj.getStringWidth("Down") + 5,
                12, "Down"));
        event.buttonList.add(new GuiButton(1002,
                mc.currentScreen.width / 2,
                mc.currentScreen.height / 16 + 28,
                mc.fontRendererObj.getStringWidth("Reset") + 5,
                12, "Reset"));
        event.buttonList.add(new GuiButton(1003,
                mc.currentScreen.width / 2 - mc.currentScreen.width / 12,
                mc.currentScreen.height / 16 + 14,
                mc.fontRendererObj.getStringWidth("200ms delay") + 5,
                12, "200ms delay"));
        event.buttonList.add(new GuiButton(1004,
                mc.currentScreen.width / 2 - mc.currentScreen.width / 12,
                mc.currentScreen.height / 16 + 28, mc.fontRendererObj.getStringWidth("312ms delay") + 5,
                12,
                "312ms delay"));
        event.buttonList.add(new GuiButton(1005,
                mc.currentScreen.width / 2 - mc.currentScreen.width / 12,
                mc.currentScreen.height / 16, mc.fontRendererObj.getStringWidth("100ms delay") + 5,
                12,
                "100ms delay"));

    }

    @SubscribeEvent
    public void GuiOpenEvent(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (mc.currentScreen instanceof GuiChest) {





            switch (event.button.id) {
                case (1000):
                    chatService.queueClientChatMessage("added +10 to delay", chatService.chatEnum.PREFIX);
                    break;
                case (1001):
                    chatService.queueClientChatMessage("added -10 to delay", chatService.chatEnum.PREFIX);
                    break;
                case (1002):
                    chatService.queueClientChatMessage("delay was reset", chatService.chatEnum.PREFIX);
                    break;
                case (1003):
                    chatService.queueClientChatMessage("set delay to 200ms", chatService.chatEnum.PREFIX);
                    break;
                case (1004):
                    chatService.queueClientChatMessage("set delay to 312ms", chatService.chatEnum.PREFIX);
                    break;
                case (1005):
                    chatService.queueClientChatMessage("set delay to 100ms", chatService.chatEnum.PREFIX);
                    break;
            }
        }
    }




}
