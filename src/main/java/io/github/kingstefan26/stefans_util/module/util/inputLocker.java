package io.github.kingstefan26.stefans_util.module.util;
import io.github.kingstefan26.stefans_util.core.module.UtilModule;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import io.github.kingstefan26.stefans_util.util.stolenBs.MouseLocker;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class inputLocker extends UtilModule {
    public inputLocker(){
        super("inputLocker");
    }
    public static boolean locked;
    public static int unlockkey;

    @SubscribeEvent
    public void onClientTick(TickEvent.RenderTickEvent event) {
        if(locked){
            Mouse.getDX();
            Mouse.getDY();
            mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(locked){
            if(event.phase == TickEvent.Phase.START) {
                while (Keyboard.next()) {
                    if (Keyboard.isKeyDown(unlockkey)) {
                        locked = false;
                        MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.clickedUnlockKeyEvent());
                    }
                }
                while(Mouse.next()) {}
            }
        }
    }
}
