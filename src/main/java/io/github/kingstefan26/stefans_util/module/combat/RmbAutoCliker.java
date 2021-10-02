package io.github.kingstefan26.stefans_util.module.combat;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.concurrent.ThreadLocalRandom;

public class RmbAutoCliker extends Module {

    private long lastClick;
    private long hold;

    private double speed;
    private double holdLength;
    private double min;
    private double max;

    public RmbAutoCliker() {
        super("RmbAutoClicker", "Automatically clicks when you hold down right click", ModuleManager.Category.COMBAT, true);
        this.enableMessage = "rmb autoclicker enabled";
        this.disableMessage = "rmb autoclicker disabled";

        SettingsManager.getSettingsManager().rSetting(new Setting("RbmMinCPS", this, 8, 1, 20, false));
        SettingsManager.getSettingsManager().rSetting(new Setting("RbmMaxCPS", this, 12, 1, 20, false));
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
            if (Mouse.isButtonDown(1)) {
                if (System.currentTimeMillis() - lastClick > speed * 1000) {
                    lastClick = System.currentTimeMillis();
                    if (hold < lastClick) {
                        hold = lastClick;
                    }
                    int key = mc.gameSettings.keyBindUseItem.getKeyCode();
                    KeyBinding.setKeyBindState(key, true);
                    KeyBinding.onTick(key);
                    this.updateVals();
                } else if (System.currentTimeMillis() - hold > holdLength * 1000) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    this.updateVals();
                }
            }
    }

    private void updateVals() {
        min = SettingsManager.getSettingsManager().getSettingByName("RbmMinCPS").getValDouble();
        max = SettingsManager.getSettingsManager().getSettingByName("RbmMaxCPS").getValDouble();

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
