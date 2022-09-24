package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.onoffMessageDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.impl.inputLockerService;
import io.github.kingstefan26.stefans_util.service.impl.keyControlService;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.event.KeyEvent;

public class keyControlServiceTest extends BasicModule {
    public keyControlServiceTest() {
        super("keyControlServiceTest", "testing the new service", ModuleManager.Category.DEBUG,
                new keyBindDecorator("keyControlServiceTest"), new onoffMessageDecorator(), new presistanceDecorator());
    }

    boolean shouldLoop = true;


    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event) {
        if (shouldLoop) {
            shouldLoop = false;
            keyControlService.submitCommandASYNC(new keyControlService.simpleCommand(200, keyControlService.action.walk.left, () -> {
                keyControlService.submitCommandASYNC(new keyControlService.simpleCommand(200, keyControlService.action.walk.back, () -> {
                    keyControlService.submitCommandASYNC(new keyControlService.simpleCommand(200, keyControlService.action.walk.right, () -> {
                        keyControlService.submitCommandASYNC(new keyControlService.simpleCommand(200, keyControlService.action.walk.forward, () -> {
                            shouldLoop = true;
                        }));
                    }));
                }));
            }));
        }
    }

    @Override
    public void onEnable() {

        inputLockerService.lock(Keyboard.KEY_0, () -> {
            chatService.queueCleanChatMessage("yay unlocked ig");
            shouldLoop = false;
        });


        shouldLoop = true;


        (new Thread(() -> {
            try {
                for (int i = 0; i < 6; i++) {
                    Thread.sleep(500);
                    logger.info("THIS HAPPENED");

                    Robot robot = new Robot();


                    robot.keyPress(KeyEvent.VK_CAPS_LOCK);
                    robot.keyRelease(KeyEvent.VK_CAPS_LOCK);

                }
            } catch (Exception e) {
                logger.error(e);
            }
        })).start();


        this.toggle();
    }
}
