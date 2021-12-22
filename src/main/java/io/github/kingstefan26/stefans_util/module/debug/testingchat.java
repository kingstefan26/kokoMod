package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;

public class testingchat extends basicModule {
    public testingchat() {
        super("test", "test", moduleManager.Category.DEBUG);
    }

    @Override
    public void onEnable() {


        this.toggle();
    }
}
