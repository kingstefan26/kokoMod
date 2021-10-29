package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.onoffMessageDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.impl.keyControlService;

import java.util.Timer;
import java.util.TimerTask;

public class keyControlServiceTest extends basicModule {
    public keyControlServiceTest() {
        super("keyControlServiceTest", "testing the new service", moduleManager.Category.DEBUG,
                new keyBindDecorator("keyControlServiceTest"), new onoffMessageDecorator());
    }

    @Override
    public void onEnable() {
        keyControlService.submitCommandASYNC(new keyControlService.command(2000, false, keyControlService.action.walk.walkright));
//        keyControlService.submitCommandASYNC(new keyControlService.command(2000, false, keyControlService.action.walk.walkback));
//        keyControlService.submitCommandASYNC(new keyControlService.command(2000, false, keyControlService.action.walk.walkleft));
//        keyControlService.submitCommandASYNC(new keyControlService.command(2000, false, keyControlService.action.walk.walkforward));

        super.onEnable();
        chatService.queueCleanChatMessage("am i disapir");
        chatService.removeLastChatMessage();
    }
}
