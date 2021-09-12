package me.kokoniara.kokoMod.module.modules.player;

import me.kokoniara.kokoMod.core.kokoMod;
import me.kokoniara.kokoMod.module.moduleUtil.config.confgValueType;
import me.kokoniara.kokoMod.module.moduleUtil.module.Module;
import me.kokoniara.kokoMod.module.moduleUtil.module.Category;
import me.kokoniara.kokoMod.module.moduleUtil.settings.Setting;
import me.kokoniara.kokoMod.module.moduleUtil.settings.SettingsManager;
import me.kokoniara.kokoMod.util.sendChatMessage;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", "Always holds down the sprint key", Category.MOVEMENT, true);
		SettingsManager.getSettingsManager().rSetting(new Setting("Permanently enable sprint", this, confgValueType.PERSISTENT));
		if(SettingsManager.getSettingsManager().getSettingByName("Permanently enable sprint", this).getValBoolean()){
			this.toggle();
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent e) {
		if(mc.thePlayer != null || mc.theWorld != null) KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
	}

	@Override
	public void onEnable(){
		super.onEnable();
		if(mc.thePlayer != null || mc.theWorld != null) sendChatMessage.sendClientMessage(" Enabled sprint" , true);
	}

	@Override
	public void onDisable() {
		super.onDisable();
		if(mc.thePlayer != null || mc.theWorld != null){
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
			sendChatMessage.sendClientMessage(" Disabled sprint" , true);
		}
	}
}
