package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.inputLockerService;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager.Category.DEBUG;

public class keyMouselocktest extends basicModule {
    public keyMouselocktest(){
        super("keyMouselocktest", "ad", DEBUG);
    }

    @Override
    public void onEnable(){
        inputLockerService.lock(Keyboard.KEY_L, () -> {});
        super.onEnable();
    }
    @SubscribeEvent
    public void onunlockkey(stefan_utilEvents.clickedUnlockKeyEvent e){
        this.toggle();
    }
}
