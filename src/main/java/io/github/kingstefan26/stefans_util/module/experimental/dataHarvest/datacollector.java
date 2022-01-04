/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.experimental.dataHarvest;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.module.experimental.dataHarvest.recordables.impl.keyRecordable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class datacollector extends basicModule {

    public datacollector() {
        super("datacollector", "hey", moduleManager.Category.MACRO);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        recordManager.getInstance().init();
    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent e) {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null)
            return;
        try {
            if (Keyboard.isCreated()) {
                if (Keyboard.getEventKeyState()) {
                    int keyCode = Keyboard.getEventKey();
                    if (keyCode <= 0)
                        return;

                    recordPipeLine.getInstance().submitData(new keyRecordable(System.currentTimeMillis(), keyCode));

                }
            }
        } catch (Exception ignored) {}
    }

    @SubscribeEvent
    public void onMouse(MouseEvent e){

    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        super.onTick(e);

    }
}
