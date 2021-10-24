package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.service.impl.inputLockerService;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class keyMouselocktest extends Module {
    public keyMouselocktest(){
        super("keyMouselocktest", "ad", ModuleManager.Category.DEBUG);
    }
    @Override
    public void onLoad(){
        super.onLoad();
    }
    @Override
    public void onEnable(){
        inputLockerService.unlockkey = Keyboard.KEY_L;
        inputLockerService.locked = true;
        super.onEnable();
    }
    @SubscribeEvent
    public void onunlockkey(stefan_utilEvents.clickedUnlockKeyEvent e){
        this.toggle();
    }
}
