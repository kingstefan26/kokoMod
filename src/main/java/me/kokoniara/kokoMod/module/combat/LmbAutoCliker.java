package me.kokoniara.kokoMod.module.combat;

import me.kokoniara.kokoMod.kokoMod;
import me.kokoniara.kokoMod.module.Category;
import me.kokoniara.kokoMod.module.Module;
import me.kokoniara.kokoMod.settings.Setting;
import me.kokoniara.kokoMod.util.sendChatMessage;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.concurrent.ThreadLocalRandom;

public class LmbAutoCliker extends Module {

	private long lastClick;
	private long hold;

	private double speed;
	private double holdLength;
	private double min;
	private double max;

	public LmbAutoCliker() {
		super("LmbAutoClicker", "Automatically clicks when you hold down left click", Category.COMBAT, true, "lmb autoclicker enabled", "lmb autoclicker disabled");

		kokoMod.instance.settingsManager.rSetting(new Setting("LbmMinCPS", this, 8, 1, 20, false));
		kokoMod.instance.settingsManager.rSetting(new Setting("LbmMaxCPS", this, 12, 1, 20, false));
	}

	@SubscribeEvent
	public void onTick(TickEvent.RenderTickEvent e) {
		if (Mouse.isButtonDown(0)) {
			if (System.currentTimeMillis() - lastClick > speed * 1000) {
				lastClick = System.currentTimeMillis();
				if (hold < lastClick) {
					hold = lastClick;
				}
				int key = mc.gameSettings.keyBindAttack.getKeyCode();
				KeyBinding.setKeyBindState(key, true);
				KeyBinding.onTick(key);
				this.updateVals();
			} else if (System.currentTimeMillis() - hold > holdLength * 1000) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
				this.updateVals();
			}
		}
	}

	private void updateVals() {
		min = kokoMod.instance.settingsManager.getSettingByName("LbmMinCPS").getValDouble();
		max = kokoMod.instance.settingsManager.getSettingByName("LbmMaxCPS").getValDouble();

		if (min >= max) {
			max = min + 1;
		}

		speed = 1.0 / ThreadLocalRandom.current().nextDouble(min - 0.2, max);
		holdLength = speed / ThreadLocalRandom.current().nextDouble(min, max);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		updateVals();
	}

}
