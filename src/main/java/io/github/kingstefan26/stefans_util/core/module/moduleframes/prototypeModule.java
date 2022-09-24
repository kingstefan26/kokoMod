/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.module.moduleframes;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.decoratorInterface;
import io.github.kingstefan26.stefans_util.core.setting.impl.ChoseAKeySetting;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public abstract class prototypeModule extends BasicModule {
    int keyKey;

    protected prototypeModule(String name, decoratorInterface... decorators) {
        super(name, "debug class", ModuleManager.Category.DEBUG, decorators);
    }

    protected prototypeModule(String name) {
        super(name, "debug class", ModuleManager.Category.DEBUG);
    }

    @Override
    public void onLoad() {
        new ChoseAKeySetting("doActionKey", this, Keyboard.KEY_0, (newvalue) -> keyKey = (int) newvalue);
        super.onLoad();
    }

    @Override
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {

            int keyCode = Keyboard.getEventKey();
            if (keyCode <= 0)
                return;

            if (keyCode == keyKey) {
                PROTOTYPETEST();
            }

        }
        super.onKeyInput(event);
    }

    protected abstract void PROTOTYPETEST();
}
