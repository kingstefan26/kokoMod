package io.github.kingstefan26.stefans_util.util.stolenBs;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * {@code KeyboardLocker} is responsible for locking and unlocking the keyboard input to Minecraft.
 * <p>
 * {@code KeyboardLocker} complies with the Singleton design pattern.
 * An instance of {@code KeyboardLocker} can be retrieved with the following code:
 * <pre>{@code
 * KeyboardLocker keyboardLocker = KeyboardLocker.getKeyboardLocker();
 * }</pre>
 */
@SideOnly(Side.CLIENT)
public class KeyboardLocker {
    protected static Minecraft mc = Minecraft.getMinecraft();
    private static KeyboardLocker keyboardLocker;
    private boolean isKeyboardLocked;

    private KeyboardLocker() {
    }

    public static KeyboardLocker getKeyboardLocker() {
        if(keyboardLocker == null)
            keyboardLocker = new KeyboardLocker();
        return keyboardLocker;
    }

    public boolean isKeyboardLocked() {
        return isKeyboardLocked;
    }

    public void lockKeyboard() {
        isKeyboardLocked = true;
    }

    public void unlockKeyboard() {
        isKeyboardLocked = false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(isKeyboardLocked() && event.phase == TickEvent.Phase.START) {
            while (Keyboard.next()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                    if (mc.isGamePaused())
                        mc.displayGuiScreen(null);
                    else {
                        unlockKeyboard();
                        MouseLocker.getMouseLocker().unlockMousePos();
                        MouseLocker.getMouseLocker().unlockMouseButtons();
                        mc.displayInGameMenu();
                    }
                }
            }
        }
    }
}
