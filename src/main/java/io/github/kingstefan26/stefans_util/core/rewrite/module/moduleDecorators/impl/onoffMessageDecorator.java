package io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl;

import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.Idecorator;
import io.github.kingstefan26.stefans_util.service.impl.chatService;

public class onoffMessageDecorator extends Idecorator {
    public onoffMessageDecorator() {
        super();
    }
    @Override
    public void onEnable(){
        if(!chatService.lockEnableMessages) chatService.queueClientChatMessage("Enabled " + this.parentModule.name, chatService.chatEnum.CHATPREFIX);
    }
    @Override
    public void onDisable(){
        chatService.queueClientChatMessage("Disabled " + this.parentModule.name, chatService.chatEnum.CHATPREFIX);
    }
}
