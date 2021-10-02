package io.github.kingstefan26.stefans_util.module.player;

import io.github.kingstefan26.stefans_util.core.config.confgValueType;
import io.github.kingstefan26.stefans_util.core.module.Category;
import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import io.github.kingstefan26.stefans_util.util.sendChatMessage;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", "Always holds down the sprint key", Category.MOVEMENT, true);
		SettingsManager.getSettingsManager().rSetting(new Setting("Permanently enable sprint", this, confgValueType.PERSISTENT));
		if(SettingsManager.getSettingsManager().getSettingByName("Permanently enable sprint", this).getValBoolean()){
			this.toggle();
		}
	}

	@Override
	public void onTick(TickEvent.Type type, Side side, TickEvent.Phase phase){
		if(mc.thePlayer != null || mc.theWorld != null) KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
	}


	@Override
	public void onEnable(){
		if(mc.thePlayer != null || mc.theWorld != null) sendChatMessage.sendClientMessage(" Enabled sprint" , true);
	}

	@Override
	public void onDisable() {
		if(mc.thePlayer != null || mc.theWorld != null){
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
			sendChatMessage.sendClientMessage(" Disabled sprint" , true);
		}
	}
}