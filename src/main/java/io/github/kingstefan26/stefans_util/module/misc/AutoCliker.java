/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.MultichoiseSetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderNoDecimalSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;


public class AutoCliker extends basicModule {

	private long rightlastClick, righthold;

	private double rightspeed, rightholdLength, rightmin, rightmax;

	private long leftlastClick, lefthold;

	private double leftspeed, leftholdLength, leftmin, leftmax;


	static String mode;

	public AutoCliker() {
		super("AutoClicker", "Automatically clicks tha mouse", moduleManager.Category.MISC, new keyBindDecorator("autoclick"));
	}

	@Override
	public void onLoad(){
		new SliderNoDecimalSetting("leftMouseMinCPS", this, 8, 1, 20, (newVal) -> {
			leftmin = (int) newVal;
		});
		new SliderNoDecimalSetting("leftMouseMaxCPS", this, 12, 1, 20, (newVal) -> {
			leftmax = (int) newVal;
		});

		new SliderNoDecimalSetting("rightMouseMinCPS", this, 8, 1, 20, (newVal) -> {
			rightmin = (int) newVal;
		});
		new SliderNoDecimalSetting("rightMouseMaxCPS", this, 12, 1, 20, (newVal) -> {
			rightmax = (int) newVal;
		});

		new MultichoiseSetting("ClickMode", this,"lmb", new ArrayList<>(Arrays.asList("lmb", "rmb", "both")), (newVal) -> {
			mode = (String) newVal;
		});


		super.onLoad();
	}

	@SubscribeEvent
	public void onTick(TickEvent.RenderTickEvent e) {
		if(mc.currentScreen == null) return;
		switch(mode){
			case"lmb":
				lmbAutoclick();
				break;
			case"rmb":
				rmbAutoClick();
				break;
			case"both":
				lmbAutoclick();
				rmbAutoClick();
				break;
		}
	}

	private void lmbAutoclick() {
		if (Mouse.isButtonDown(0)) {
			if (System.currentTimeMillis() - leftlastClick > leftspeed * 1000) {
				leftlastClick = System.currentTimeMillis();
				if (lefthold < leftlastClick) {
					lefthold = leftlastClick;
				}
				int key = mc.gameSettings.keyBindAttack.getKeyCode();
				KeyBinding.setKeyBindState(key, true);
				KeyBinding.onTick(key);
				this.updateVals();
			} else if (System.currentTimeMillis() - lefthold > leftholdLength * 1000) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
				this.updateVals();
			}
		}
	}

	private void rmbAutoClick() {
		if (Mouse.isButtonDown(1)) {
			if (System.currentTimeMillis() - rightlastClick > rightspeed * 1000) {
				rightlastClick = System.currentTimeMillis();
				if (righthold < rightlastClick) {
					righthold = rightlastClick;
				}
				int key = mc.gameSettings.keyBindUseItem.getKeyCode();
				KeyBinding.setKeyBindState(key, true);
				KeyBinding.onTick(key);
				this.updateVals();
			} else if (System.currentTimeMillis() - righthold > rightholdLength * 1000) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
				this.updateVals();
			}
		}
	}

	private void updateVals() {
		if (rightmin >= rightmax) {
			rightmax = rightmin + 1;
		}

		rightspeed = 1.0 / ThreadLocalRandom.current().nextDouble(rightmin - 0.2, rightmax);
		rightholdLength = rightspeed / ThreadLocalRandom.current().nextDouble(rightmin, rightmax);


		if (leftmin >= leftmax) {
			leftmax = leftmin + 1;
		}

		leftspeed = 1.0 / ThreadLocalRandom.current().nextDouble(leftmin - 0.2, leftmax);
		leftholdLength = leftspeed / ThreadLocalRandom.current().nextDouble(leftmin, leftmax);

	}

	@Override
	public void onEnable() {
		super.onEnable();
		updateVals();
	}

}
