/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sprint extends basicModule {

	public Sprint() {
		super("Sprint", "Always holds down the sprint key", moduleManager.Category.MISC, new presistanceDecorator());
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent e){
		if(mc.thePlayer != null || mc.theWorld != null || !mc.gameSettings.keyBindSprint.isKeyDown())
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
	}

//	@Override
//	public void onTick(TickEvent.ClientTickEvent e){
//		if(mc.thePlayer != null || mc.theWorld != null || !mc.gameSettings.keyBindSprint.isKeyDown()) KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
//	}

	@Override
	public void onDisable() {
		if(mc.thePlayer != null || mc.theWorld != null){
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
		}
	}
}