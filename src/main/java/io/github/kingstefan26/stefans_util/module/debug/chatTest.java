package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.decoratorInterface;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.impl.chatService.*;

public class chatTest extends basicModule {
    public chatTest() {
        super("chatTest", "testing the chat service", moduleManager.Category.DEBUG);
    }


    @Override
    public void onEnable() {
        chatService.queueClientChatMessage("menu.kokomod.test", chatService.chatEnum.I18N);
        super.onEnable();
    }
}
