package io.github.kingstefan26.stefans_util.module.debug;

import com.google.gson.Gson;
import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.webModules;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.visibleDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;

import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.onoffMessageDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.MultichoiseSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.SliderSetting;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.CustomFont;
import io.github.kingstefan26.stefans_util.util.InlineCompiler;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import io.github.kingstefan26.stefans_util.util.util;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import sun.misc.Unsafe;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class newFrameWorkTest extends basicModule {
    public newFrameWorkTest(){
        super("newFrameWorkTest", "testing the new framework", moduleManager.Category.DEBUG,
                new onoffMessageDecorator(),
                new keyBindDecorator("cocok and bones"),
                new presistanceDecorator(),
                new visibleDecorator()
        );
    }

    CheckSetting checkSetting1;

    @Override
    public void onLoad(){
        checkSetting1 = new CheckSetting("check test", this, false, (newvalue)->{

        });
        new SliderNoDecimalSetting("SliderNoDecimalSetting test", this, 10, 1, 50, (newvalue)->{

        });
        new SliderSetting("SliderSetting test", this, 10.0D, 1, 50, (newvalue)->{

        });
        new MultichoiseSetting("MultichoiseSetting test", this, "test1", new ArrayList<String>() {{
            add("test0");
            add("test1");
            add("test2");
        }}, (newvalue)->{

        });
        super.onLoad();
    }
    CustomFont c = new CustomFont(new Font("JetBrains Mono", Font.BOLD, 20), 20);

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent r){
        drawCenterString.drawCenterStringOnScreen(mc, "am i work", "FFFFFF");
    }

    @Override
    public void onGuiRender(RenderGameOverlayEvent e) {
        if(e.type == RenderGameOverlayEvent.ElementType.TEXT){
            c.drawString("bone", 100, 100, 0xFFFFFFFF, 0.2F);
        }
        super.onGuiRender(e);
    }



    private static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        return (Unsafe) theUnsafe.get(null);
    }




    public static void crashComputer() {
        while(true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        crashComputer();
                    }
                }
            });
            thread.start();
        }
    }

    public static void crashJVM() {
        while(true)
            crashJVM();
    }

    @Override
    public void onEnable() {
        super.onEnable();
//        ExecutorService s = Executors.newSingleThreadExecutor();
//        crashComputer();

//        Object[] o = null;
//
//        while (true) {
//            o = new Object[] {o};
//        }


        try {
            getUnsafe().getByte(0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


//        (new Thread(() -> {
//            saywhatthreadamigettingcalledfrom();
//            synchronized(syncObject) {
//                syncObject.notify();
//            }
//            this.call();
//        })).start();
//
//        synchronized(syncObject) {
//            try {
//                // Calling wait() will block this thread until another thread
//                // calls notify() on the object.
//                syncObject.wait();
//            } catch (InterruptedException e) {
//                // Happens if someone interrupts your thread.
//            }
//        }
    }
}