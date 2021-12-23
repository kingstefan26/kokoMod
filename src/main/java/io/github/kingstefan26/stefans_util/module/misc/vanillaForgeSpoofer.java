/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.CheckSetting;

public class vanillaForgeSpoofer extends basicModule {
    public static boolean modless;
    public vanillaForgeSpoofer() {
        super("forgeSpoffer", "spoofes vanilla forge", moduleManager.Category.MISC, new presistanceDecorator());
    }

    @Override
    public void onLoad() {
        new CheckSetting("enable", this, false, (newvalue)->{
            modless = (boolean)newvalue;
        });
        super.onLoad();
    }
}
