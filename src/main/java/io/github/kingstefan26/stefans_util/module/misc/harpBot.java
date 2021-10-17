package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class harpBot extends Module {
    public harpBot() {
        super("harpbot", "a bot that bots harp", ModuleManager.Category.DEBUG);
    }

    @Override
    public void onLoad() {
        SettingsManager.getSettingsManager().rSetting(new Setting("deley", this, 0, 1000, 100, true));
        super.onLoad();
    }

    List<Slot> slots = new ArrayList<>();

    int TickCounter;

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            TickCounter++;
            if (TickCounter % 20 == 0) {
                TickCounter = 0;
                if (mc.theWorld != null && mc.currentScreen instanceof GuiChest) {
                    GuiChest chest = (GuiChest) mc.currentScreen;
                    slots.clear();
                    slots.addAll(chest.inventorySlots.inventorySlots);
                }
            }
        }
    }

//    public boolean SlotItemNameEquals(int p_SlotItemNameEquals_1_, String p_SlotItemNameEquals_2_) {
//        return this.getSlotAt(p_SlotItemNameEquals_1_).getStack().getDisplayName().equalsIgnoreCase(p_SlotItemNameEquals_2_);
//    }


    public boolean ChestNameContainsReflect(String value, GuiChest chestobject) {
        boolean flag1 = false;
        try {
            Field chestName = GuiChest.class.getDeclaredField("lowerChestInventory");
            chestName.setAccessible(true);
            IInventory lowerChestInventory = (IInventory) chestName.get(chestobject);
            String name = lowerChestInventory.getDisplayName().getUnformattedText();
            flag1 = (name.toLowerCase().contains(value.toLowerCase()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return flag1;
    }

    public static Object genericInvokeMethod(Object obj, String methodName,
                                             Object... params) {
        int paramCount = params.length;
        Method method;
        Object requiredObj = null;
        Class<?>[] classArray = new Class<?>[paramCount];
        for (int i = 0; i < paramCount; i++) {
            classArray[i] = params[i].getClass();
        }
        try {
            method = obj.getClass().getDeclaredMethod(methodName, classArray);
            method.setAccessible(true);
            requiredObj = method.invoke(obj, params);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return requiredObj;
    }

    @SubscribeEvent
    public void DaDSAdisint(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiChest) {
            event.buttonList.add(new GuiButton(1000, mc.currentScreen.width / 2, mc.currentScreen.height / 16, mc.fontRendererObj.getStringWidth("Up") + 5, 12, "Up"));
            event.buttonList.add(new GuiButton(1001, mc.currentScreen.width / 2, mc.currentScreen.height / 16 + 14, mc.fontRendererObj.getStringWidth("Down") + 5, 12, "Down"));
            event.buttonList.add(new GuiButton(1002, mc.currentScreen.width / 2, mc.currentScreen.height / 16 + 28, mc.fontRendererObj.getStringWidth("Reset") + 5, 12, "Reset"));
            event.buttonList.add(new GuiButton(1003, mc.currentScreen.width / 2 - mc.currentScreen.width / 12, mc.currentScreen.height / 16 + 14, mc.fontRendererObj.getStringWidth("200ms delay") + 5, 12, "200ms delay"));
            event.buttonList.add(new GuiButton(1004, mc.currentScreen.width / 2 - mc.currentScreen.width / 12, mc.currentScreen.height / 16 + 28, mc.fontRendererObj.getStringWidth("312ms delay") + 5, 12, "312ms delay"));
            event.buttonList.add(new GuiButton(1005, mc.currentScreen.width / 2 - mc.currentScreen.width / 12, mc.currentScreen.height / 16, mc.fontRendererObj.getStringWidth("100ms delay") + 5, 12, "100ms delay"));
        }
    }

    //
    @SubscribeEvent
    public void GuiOpenEvent(GuiOpenEvent event) {
        if (event.gui instanceof GuiChest) {
            (new Thread(() -> {
                while (ChestNameContainsReflect("Harp", (GuiChest) event.gui)) {
                    if (mc.currentScreen == null || !this.isToggled()) break;

                    try {
                        if (slots.size() == 0) continue;
                        for (int i3 = 28; i3 < 35; ++i3) {
                            Slot slot6 = slots.get(i3);

                            ItemStack stack = slot6.getStack();
                            if (stack == null) continue;

                            logger.info(stack.getItem().getUnlocalizedName());

                            if (slot6.getStack().getItem() == Item.getItemById(155)) {
                                logger.info("Slot " + i3 + " is qwartz block");
//                                        Slot slot7 = chestInv.inventorySlots.inventorySlots.get(i3 + 9);

                                // Thread.sleep(2);

                                //genericInvokeMethod(chestInv, "handleMouseClick", slot7, slot7.slotNumber, 0, 1);
                                //chestInv.handleMouseClick(slot7, slot7.slotNumber, 0, ClickType.PICKUP_ALL);


                            }

                        }
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            })).start();
//                }


//                Field chestName = null;
//                chestName = GuiChest.class.getDeclaredField("lowerChestInventory");
//                chestName.setAccessible(true);
//                logger.info(chestName.get(chestInv).getClass());
//                //net.minecraft.client.player.inventory.ContainerLocalMenu
//                InventoryBasic lowerChestInventory = (InventoryBasic) chestName.get(chestInv);
//                ContainerLocalMenu lowermenu = (ContainerLocalMenu) chestName.get(chestInv);
//
//
//                genericInvokeMethod(chestInv, "handleMouseClick", lowermenu.getGuiID(), chestInv.inventorySlots.inventorySlots.get(27), 0, 1);
//
//
//                logger.info(lowerChestInventory.getFieldCount());
//
//
//                for (int i3 = 1; i3 < 35; ++i3) {
//                    logger.info(String.format("trying to find a stack in slot %s", i3));
//                    if(lowerChestInventory.getStackInSlot(i3) != null){
//                        logger.info(lowerChestInventory.getStackInSlot(i3).getItem().getUnlocalizedName());
//                    }
//                }


//                if(chestInv.inventorySlots.inventorySlots != null){
//                    for (Slot inventorySlot : chestInv.inventorySlots.inventorySlots) {
//                        if(inventorySlot.getStack() != null){
//                            if(inventorySlot.getStack().getItem() != null){
//                                logger.info(inventorySlot.slotNumber + " is a item");
//                            }
//                        }
//                    }
//                }


//            if (ChestNameContainsReflect("Harp", (GuiChest) event.gui)) {
//                chat.queueClientChatMessage("detected harp gui", chat.chatEnum.CHATNOPREFIX);
//                (new Thread(() -> {
//                    try {
//                        for (; ; Thread.sleep(1L)) {
//                            int l2 = SettingsManager.getSettingsManager().getSettingByName("deley", this).getValInt();
//
//                            for (int i3 = 28; i3 < 35; ++i3) {
//                                Slot slot6 = chestInv.inventorySlots.inventorySlots.get(i3);
//
//                                if (!this.isToggled()) {
//                                    break;
//                                }
//
//                                logger.info(slot6.getStack().getItem().getUnlocalizedName());
//                                if (slot6.getStack().getItem() == Item.getItemById(155)) {
//                                    Slot slot7 = chestInv.inventorySlots.inventorySlots.get(i3 + 9);
//
//                                    Thread.sleep(l2 / 2);
//
//                                    genericInvokeMethod(chestInv, "handleMouseClick", slot7, slot7.slotNumber, 0, 1);
//                                    //chestInv.handleMouseClick(slot7, slot7.slotNumber, 0, ClickType.PICKUP_ALL);
//
//
//
//                                }
//
//                                    Thread.sleep(1L);
//
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                })).start();
        }
    }
//
//
//        try {
////                    if (this.ChestNameContains("Harp")) {
//            Field buttonlistFiled = GuiScreen.class.getDeclaredField("buttonList");
//            GuiScreen currentScreen = event.gui;
//
//            buttonlistFiled.setAccessible(true);
//
//
//            List<GuiButton> buttonList = (List<GuiButton>) buttonlistFiled.get(currentScreen);
//
//            buttonList.add(new GuiButton(1000, currentScreen.width / 2, currentScreen.height / 16, mc.fontRendererObj.getStringWidth("Up") + 5, 12, "Up"));
//            buttonList.add(new GuiButton(1001, currentScreen.width / 2, currentScreen.height / 16 + 14, mc.fontRendererObj.getStringWidth("Down") + 5, 12, "Down"));
//            buttonList.add(new GuiButton(1002, currentScreen.width / 2, currentScreen.height / 16 + 28, mc.fontRendererObj.getStringWidth("Reset") + 5, 12, "Reset"));
//            buttonList.add(new GuiButton(1003, currentScreen.width / 2 - currentScreen.width / 12, currentScreen.height / 16 + 14, mc.fontRendererObj.getStringWidth("200ms delay") + 5, 12, "200ms delay"));
//            buttonList.add(new GuiButton(1004, currentScreen.width / 2 - currentScreen.width / 12, currentScreen.height / 16 + 28, mc.fontRendererObj.getStringWidth("312ms delay") + 5, 12, "312ms delay"));
//            buttonList.add(new GuiButton(1005, currentScreen.width / 2 - currentScreen.width / 12, currentScreen.height / 16, mc.fontRendererObj.getStringWidth("100ms delay") + 5, 12, "100ms delay"));
//
//            buttonlistFiled.set(currentScreen, buttonList);

//                        for (; this.ChestNameContains("Harp"); Thread.sleep(1L)) {
//                            int l2 = PublicClient.currentDelay;
//
//                            for (int i3 = 28; i3 < 35; ++i3) {
//                                Slot slot6 = this.inventorySlots.inventorySlots.get(i3);
//
//                                if (!this.isToggled()) {
//                                    break;
//                                }
//
//                                if (slot6.getStack().getItem() == Item.getItemById(35)) {
//                                    Slot slot7 = this.inventorySlots.inventorySlots.get(i3 + 9);
//                                    Thread.sleep(l2 / 2);
//                                    this.handleMouseClick(slot7, slot7.slotNumber, 0, ClickType.PICKUP_ALL);
//                                    Thread.sleep(l2 / 2);
//                                }
//
//                                Thread.sleep(1L);
//                            }
//                        }
//


//
//            //}
//        } catch (Exception exception1) {
//            exception1.printStackTrace();
//        }
//        if (event.gui instanceof GuiChest) {
//            (new Thread(() ->
//            {
//
//            })).start();
//        }


}
