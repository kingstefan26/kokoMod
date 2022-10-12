/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.wip;

import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class dontOpenGui extends BasicModule {
    public dontOpenGui() {
        super("Dont Open Gui", "ad", ModuleManager.Category.WIP);
    }


    @Override
    public void onEnable() {
        mc.displayGuiScreen(null);
        super.onEnable();
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (isToggled()) {
            if (event.gui instanceof ClickGui) return;
            if (event.gui == null) return;
            event.setCanceled(true);
        }
    }
}
