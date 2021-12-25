package io.github.kingstefan26.stefans_util.module.premium;

import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;

public class webmodule extends basicModule {
    public webmodule() {
        super("webmodule", "yay!", moduleManager.Category.DEBUG);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        webmoduletest.sendExampleChatMessage();
    }
}