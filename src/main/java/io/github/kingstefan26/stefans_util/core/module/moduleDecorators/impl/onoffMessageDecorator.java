package io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl;

import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.Idecorator;
import io.github.kingstefan26.stefans_util.service.impl.chatService;

public class onoffMessageDecorator extends Idecorator {
    public onoffMessageDecorator() {
        super();
    }
    @Override
    public void onEnable(){
        if(!chatService.lockEnableMessages) chatService.queueClientChatMessage("Enabled " + this.parentModule.getName(), chatService.chatEnum.PREFIX);
    }
    @Override
    public void onDisable(){
        chatService.queueClientChatMessage("Disabled " + this.parentModule.getName(), chatService.chatEnum.PREFIX);
    }
}
