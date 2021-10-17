package io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl;

import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.Idecorator;
import io.github.kingstefan26.stefans_util.module.util.chat;

public class onoffMessageDecorator extends Idecorator {
    public onoffMessageDecorator() {
        super();
    }
    @Override
    public void onEnable(){
        chat.queueClientChatMessage("Enabled " + this.parentModule.name, chat.chatEnum.CHATPREFIX);
    }
    @Override
    public void onDisable(){
        chat.queueClientChatMessage("Disabled " + this.parentModule.name, chat.chatEnum.CHATPREFIX);
    }
}
