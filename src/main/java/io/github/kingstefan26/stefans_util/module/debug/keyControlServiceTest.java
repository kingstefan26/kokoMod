package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.onoffMessageDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;

import java.awt.*;
import java.awt.event.KeyEvent;

public class keyControlServiceTest extends basicModule {
    public keyControlServiceTest() {
        super("keyControlServiceTest", "testing the new service", moduleManager.Category.DEBUG,
                new keyBindDecorator("keyControlServiceTest"), new onoffMessageDecorator(), new presistanceDecorator());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        //keyControlService.submitCommandASYNC(new keyControlService.command(2000, false, keyControlService.action.walk.right));
//        keyControlService.submitCommandASYNC(new keyControlService.command(2000, false, keyControlService.action.walk.walkback));
//        keyControlService.submitCommandASYNC(new keyControlService.command(2000, false, keyControlService.action.walk.walkleft));
//        keyControlService.submitCommandASYNC(new keyControlService.command(2000, false, keyControlService.action.walk.walkforward));


        (new Thread(() -> {
            try{
                for (int i = 0; i < 6; i++) {
                    Thread.sleep(500);
                    logger.info("THIS HAPPENED");

                    Robot robot = new Robot();


                    robot.keyPress(KeyEvent.VK_CAPS_LOCK);
                    robot.keyRelease(KeyEvent.VK_CAPS_LOCK);

                }
            }catch (Exception e){
                logger.error(e);
            }
        })).start();



        //chatService.queueCleanChatMessage("am i disapir");
        chatService.removeLastChatMessage();
        this.toggle();
    }
}
