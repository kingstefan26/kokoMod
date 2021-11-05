package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.preRewrite.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.preRewrite.module.Module;
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
