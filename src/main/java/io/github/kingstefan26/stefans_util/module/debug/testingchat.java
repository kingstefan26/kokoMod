package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.service.impl.chatService;

public class testingchat extends Module {
    public testingchat() {
        super("test", "test", ModuleManager.Category.DEBUG);
    }
    @Override
    public void onEnable(){
        chatService.queueClientChatMessage("hello luvs", chatService.chatEnum.CHATPREFIX);
        this.toggle();
    }
}
