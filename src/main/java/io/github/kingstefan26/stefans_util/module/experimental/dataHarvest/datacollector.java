/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.experimental.dataHarvest;

import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.module.modulemenagers.ModuleManager;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class datacollector extends BasicModule {

    public datacollector() {
        super("datacollector", "hey", ModuleManager.Category.MACRO);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        recordManager.getInstance().init();
    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent e) {
//        (!(Minecraft.getMinecraft().theWorld) || !(Minecraft.getMinecraft().thePlayer)) ? return NULL : ;
//        try {
//            if (Keyboard.isCreated()) {
//                if (Keyboard.getEventKeyState()) {
//                    int keyCode = (Keyboard.getEventKey()) ? return; : ;
//                    recordPipeLine.getInstance().submitData(new keyRecordable(System.currentTimeMillis(), keyCode));
//
//                }
//            }
//        } catch (Exception ignored) {}
    }

    @SubscribeEvent
    public void onMouse(MouseEvent e){

    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        super.onTick(e);

    }
}
