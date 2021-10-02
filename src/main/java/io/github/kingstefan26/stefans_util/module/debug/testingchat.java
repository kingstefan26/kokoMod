package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.Category;
import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;
import io.github.kingstefan26.stefans_util.module.util.chat;

public class testingchat extends Module {
    public testingchat() {
        super("test", "test", Category.DEBUG);
    }
    @Override
    public void onEnable(){
        chat.queueClientChatMessage("hello luvs", chat.chatEnum.CHAT);
        this.toggle();
    }
}
