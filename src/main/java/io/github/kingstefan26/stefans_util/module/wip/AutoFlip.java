/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.wip;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoFlip extends basicModule {
    int counter;


    public AutoFlip() {
        super("AutoFlip", "its like magic", moduleManager.Category.MACRO);
    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START) return;
        counter++;
        // every 10 seconds
        if (counter % 200 == 0) {
            counter = 0;

            // refresh bin

        }

    }
}
