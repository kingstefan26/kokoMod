/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.config.attotations.impl.BooleanConfigValue;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class jsonconfigtest extends BasicModule {
    @BooleanConfigValue(name = "testvar", defaultValue = false)
    boolean test = false;

    public jsonconfigtest() {
        super("jsonconfigtest", "AAA", ModuleManager.Category.DEBUG);

    }

    @Override
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {

            int keyCode = Keyboard.getEventKey();
            if (keyCode <= 0)
                return;

            if (keyCode == Keyboard.KEY_0) {
                PROTOTYPETEST();
            }

        }
        super.onKeyInput(event);
    }

    protected void PROTOTYPETEST() {
        chatService.queueClientChatMessage(String.valueOf(test));
        test = !test;
    }
}
