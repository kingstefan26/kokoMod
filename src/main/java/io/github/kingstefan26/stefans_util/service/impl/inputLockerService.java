package io.github.kingstefan26.stefans_util.service.impl;

import io.github.kingstefan26.stefans_util.service.Service;
import io.github.kingstefan26.stefans_util.util.StefanutilEvents;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class inputLockerService extends Service {
    public inputLockerService() {
        super("inputLocker");
    }

    private static boolean locked;
    public static boolean getLockStatus(){
        return locked;
    }
    private static int unlockkey;
    private static Runnable callback;
    static exeptionKeybind[] exeptionKeybinds;


    public static void unlock() {
        locked = false;
    }

    public static void lock() {
        KeyBinding.unPressAllKeys();
        inputLockerService.callback = null;
        locked = true;
    }

    public static void lock(int unLockKey, Runnable callback, exeptionKeybind... exeptions) {
        KeyBinding.unPressAllKeys();
        inputLockerService.callback = callback;
        exeptionKeybinds = exeptions;
        unlockkey = unLockKey;
        locked = true;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (locked) {
            if (event.phase == TickEvent.Phase.START) {
                while (Keyboard.next()) {
                    if (Keyboard.isKeyDown(unlockkey)) {
                        locked = false;
                        if (callback == null) {
                            MinecraftForge.EVENT_BUS.post(new StefanutilEvents.clickedUnlockKeyEvent());
                        } else {
                            callback.run();
                        }
                    }
                    if (exeptionKeybinds != null) {
                        for (exeptionKeybind exeption : exeptionKeybinds) {
                            if (exeption != null) {
                                if (Keyboard.isKeyDown(exeption.keycode)) {
                                    exeption.action.run();
                                }
                            }
                        }
                    }
                }
                while (Mouse.next()) {
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.RenderTickEvent event) {
        if (locked) {
            Mouse.getDX();
            Mouse.getDY();
            mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;
        }
    }

    public static class exeptionKeybind {
        private int keycode;
        private Runnable action;

        public exeptionKeybind(int keycode, Runnable action) {
            this.keycode = keycode;
            this.action = action;
        }
    }

    @Override
    public void start() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void stop() {
    }
}
