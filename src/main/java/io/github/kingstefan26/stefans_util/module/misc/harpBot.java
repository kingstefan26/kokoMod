package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class harpBot extends Module {
    public harpBot() {
        super("harpbot", "a bot that bots harp", ModuleManager.Category.DEBUG);
    }

    @Override
    public void onLoad() {
        SettingsManager.getSettingsManager().rSetting(new Setting("delay", this, 0, 1000, 100, true));
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

    ScheduledExecutorService scheduledExecutorService;

    Runnable command = (() -> {
        logger.info("Executed!");
        if (mc.currentScreen instanceof GuiChest) {

            if (ChestNameContainsReflect("Harp", (GuiChest) mc.currentScreen)) {
                for (int i = 28; i < 35; ++i) {
                    Slot slot6 = slots.get(i);
                    ItemStack stack = slot6.getStack();
                    if (stack == null) continue;
                    logger.info(stack.getItem().getUnlocalizedName());
                    if (slot6.getStack().getItem() == Item.getItemById(155)) {
                        logger.info("Slot " + i + " is quartz block");
                        chatService.queueClientChatMessage("Slot " + i + " is quartz block", chatService.chatEnum.CHATNOPREFIX);
                        //TODO: click the slot in GuiChest
                    }
                }
            }
        }
    });

    @Override
    public void onEnable() {
        super.onEnable();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(command, 0, 2, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        scheduledExecutorService.shutdown();
    }

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

    public static Object genericInvokeMethod(Object obj, String methodName, Object... params) {
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
        if (!(event.gui instanceof GuiChest)) return;
        if (!ChestNameContainsReflect("Harp", (GuiChest) mc.currentScreen)) return;

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
                    chatService.queueClientChatMessage("added +10 to delay", chatService.chatEnum.CHATPREFIX);
                    break;
                case (1001):
                    chatService.queueClientChatMessage("added -10 to delay", chatService.chatEnum.CHATPREFIX);
                    break;
                case (1002):
                    chatService.queueClientChatMessage("delay was reset", chatService.chatEnum.CHATPREFIX);
                    break;
                case (1003):
                    chatService.queueClientChatMessage("set delay to 200ms", chatService.chatEnum.CHATPREFIX);
                    break;
                case (1004):
                    chatService.queueClientChatMessage("set delay to 312ms", chatService.chatEnum.CHATPREFIX);
                    break;
                case (1005):
                    chatService.queueClientChatMessage("set delay to 100ms", chatService.chatEnum.CHATPREFIX);
                    break;
            }
        }
    }

//    @SubscribeEvent
//    public void GuiOpenEvent(GuiOpenEvent event) {
//        if (event.gui instanceof GuiChest) {
//            (new Thread(() -> {
//                while (ChestNameContainsReflect("Harp", (GuiChest) event.gui)) {
//                    if (mc.currentScreen == null || event.gui == null || !this.isToggled()) break;
//
//                    try {
//                        if (slots.size() == 0) continue;
//                        for (int i3 = 28; i3 < 35; ++i3) {
//                            Slot slot6 = slots.get(i3);
//
//                            ItemStack stack = slot6.getStack();
//                            if (stack == null) continue;
//
//                            logger.info(stack.getItem().getUnlocalizedName());
//
//                            if (slot6.getStack().getItem() == Item.getItemById(155)) {
//                                logger.info("Slot " + i3 + " is quartz block");
//                                genericInvokeMethod(event.gui, "handleMouseClick", slot6, slot6.slotNumber, 0, 1);
//                            }
//
//                        }
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            })).start();
//
//        }
//    }


//            (new Thread(() ->
//            {
//
//            })).start();
}