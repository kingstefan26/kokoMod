package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;

public class chatTest extends BasicModule {
    public chatTest() {
        super("chatTest", "testing the chat service", ModuleManager.Category.DEBUG);
    }


    @Override
    public void onEnable() {
        chatService.queueClientChatMessage("menu.kokomod.test", chatService.chatEnum.I18N);
        super.onEnable();
    }
}
