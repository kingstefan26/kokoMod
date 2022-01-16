/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.module.moduleFrames;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.decoratorInterface;
import io.github.kingstefan26.stefans_util.core.setting.impl.ChoseAKeySetting;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

abstract public class prototypeModule extends basicModule {
    int keyKey;

    public prototypeModule(String name, decoratorInterface... decorators) {
        super(name, "debug class", moduleManager.Category.DEBUG, decorators);
    }

    @Override
    public void onLoad() {
        new ChoseAKeySetting("doActionKey", this, Keyboard.KEY_0, (newvalue) -> keyKey = (int) newvalue);
        super.onLoad();
    }

    @Override
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isCreated()) {
            if (Keyboard.getEventKeyState()) {
                int keyCode = Keyboard.getEventKey();
                if (keyCode <= 0)
                    return;

                if (keyCode == keyKey) {
                    PROTOTYPETEST();
                }
            }
        }
        super.onKeyInput(event);
    }

    protected abstract void PROTOTYPETEST();
}
