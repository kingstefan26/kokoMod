/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
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

public class oldAutoClicker extends basicModule {
    String mode = "both";
    long guiTimeOut;
    private long lastClick;
    private long hold;

    private double speed;
    private double holdLength;
    private double min = 10;
    private double max = 12;
    private double rmin = 10;
    private double rmax = 12;
    private long rlastClick;
    private long rhold;
    private double rspeed;
    private double rholdLength;

    public oldAutoClicker() {
        super("AutoClicker", "auto clicker lmao", moduleManager.Category.MISC);
    }

    @Override
    public void onLoad() {
        new SliderNoDecimalSetting("leftMouseMinCPS", this, 8, 1, 20, (newVal) -> {
            min = (int) newVal;
        });
        new SliderNoDecimalSetting("leftMouseMaxCPS", this, 12, 1, 20, (newVal) -> {
            max = (int) newVal;
        });

        new SliderNoDecimalSetting("rightMouseMinCPS", this, 8, 1, 20, (newVal) -> {
            rmin = (int) newVal;
        });
        new SliderNoDecimalSetting("rightMouseMaxCPS", this, 12, 1, 20, (newVal) -> {
            rmax = (int) newVal;
        });

        new MultichoiseSetting("ClickMode", this, "lmb", new ArrayList<>(Arrays.asList("lmb", "rmb", "both")), (newVal) -> {
            mode = (String) newVal;
        });

        super.onLoad();
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
        if (mc.currentScreen != null) {
            guiTimeOut = System.currentTimeMillis() + 100;
            return;
        }

        if (!(System.currentTimeMillis() >= guiTimeOut)) {
            return;
        }
        if (Mouse.isButtonDown(0) && mode.equals("lmb")) {
            lmb();
        }
        if (Mouse.isButtonDown(1) && mode.equals("rmb")) {
            rmb();
        }

        if (mode.equals("both")) {
            if (Mouse.isButtonDown(0)) {
                lmb();
            }
            if (Mouse.isButtonDown(1)) {
                rmb();
            }
        }

    }

    private void rmb() {
        if (System.currentTimeMillis() - rlastClick > rspeed * 1000) {
            rlastClick = System.currentTimeMillis();
            if (rhold < rlastClick) {
                rhold = rlastClick;
            }
            int key = mc.gameSettings.keyBindUseItem.getKeyCode();
            KeyBinding.setKeyBindState(key, true);
            KeyBinding.onTick(key);
            this.updateVals();
        } else if (System.currentTimeMillis() - rhold > rholdLength * 1000) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            this.updateVals();
        }
    }

    private void lmb() {
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

    private void updateVals() {

        if (min >= max) {
            max = min + 1;
        }

        speed = 1.0 / ThreadLocalRandom.current().nextDouble(min - 0.2, max);
        holdLength = speed / ThreadLocalRandom.current().nextDouble(min, max);


        if (rmin >= rmax) {
            rmax = rmin + 1;
        }

        rspeed = 1.0 / ThreadLocalRandom.current().nextDouble(rmin - 0.2, rmax);
        rholdLength = rspeed / ThreadLocalRandom.current().nextDouble(rmin, rmax);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        updateVals();
    }
}
