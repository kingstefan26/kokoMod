package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.service.impl.inputLockerService;
import io.github.kingstefan26.stefans_util.util.StefanutilEvents;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static io.github.kingstefan26.stefans_util.core.module.modulemenagers.ModuleManager.Category.DEBUG;

public class keyMouselocktest extends BasicModule {
    public keyMouselocktest() {
        super("keyMouselocktest", "ad", DEBUG);
    }

    @Override
    public void onEnable() {
        inputLockerService.lock(Keyboard.KEY_L, () -> {
        });
        super.onEnable();
    }

    @SubscribeEvent
    public void onunlockkey(StefanutilEvents.clickedUnlockKeyEvent e) {
        this.toggle();
    }
}
