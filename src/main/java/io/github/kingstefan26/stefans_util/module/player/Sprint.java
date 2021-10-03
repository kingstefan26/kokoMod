package io.github.kingstefan26.stefans_util.module.player;

import io.github.kingstefan26.stefans_util.core.config.confgValueType;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", "Always holds down the sprint key", ModuleManager.Category.MOVEMENT, true);
		this.enableMessage = "Enabled sprint";
		this.disableMessage = "Disabled sprint";
		this.presistanceEnabled = true;
	}

	@Override
	public void onTick(TickEvent.ClientTickEvent e){
		if(mc.thePlayer != null || mc.theWorld != null) KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
	}



	@Override
	public void onDisable() {
		if(mc.thePlayer != null || mc.theWorld != null){
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
		}
	}
}
