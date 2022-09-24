/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.module;


import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.config.attotations.AnnotationProcessor;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.util.StefanutilEvents;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ModuleManager {
    Logger logger = LogManager.getLogger("ModuleManager");

    private ModuleManager() {

    }

    private static ModuleManager instance;

    public static ModuleManager getInstance() {
        if(instance == null) instance = new ModuleManager();
        return instance;
    }

    private List<String> productionModuleIndex;

    public List<BasicModule> getLoadedModules() {
        return loadedModules;
    }

    private List<BasicModule> loadedModules = new ArrayList<>();


    public void initModuleManager() {
        findAndLoadModuleRegistry();

        AnnotationProcessor configvalueprocessor = new AnnotationProcessor();
        io.github.kingstefan26.stefans_util.core.setting.attnotationSettings.AnnotationProcessor attotantionsettingprocessor = new io.github.kingstefan26.stefans_util.core.setting.attnotationSettings.AnnotationProcessor();

        for (BasicModule loadedModule : loadedModules) {
            configvalueprocessor.init(loadedModule);
            attotantionsettingprocessor.init(loadedModule);
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void unloadAllModules() {
        Iterator<BasicModule> iter = loadedModules.iterator();

        while (iter.hasNext()) {
            BasicModule m = iter.next();
            m.onUnload();
            iter.remove();
        }

    }

    public void findAndLoadModuleRegistry() {
        try{
            productionModuleIndex = ModuleUtil.findModuleNames();
        }catch (Exception e){
            logger.error(e);
        }
        try {
            loadedModules = ModuleUtil.loadModules(productionModuleIndex);
        }catch (Exception e){
            logger.error(e);
        }
    }

    public BasicModule getModuleByClassName(String name) {
            for (BasicModule module : loadedModules) {
                if (module.getClass().getName().equals(name)) return module;
            }
        return null;
    }

    public BasicModule getModuleByName(String name) {
            for (BasicModule module : loadedModules) {
                if (module.getName().equals(name)) return module;
            }
        return null;
    }

    public List<BasicModule> getModulesInCategory(ModuleManager.Category c) {
        ArrayList<BasicModule> a = new ArrayList<>();
        for (BasicModule m : loadedModules) {
            if (m.getCategory() == c) {
                a.add(m);
            }
        }
        return a;
    }

    public enum Category {
        MOVEMENT, RENDER, MISC, MACRO, UTIL_MODULE, DEBUG, WIP
    }

    @SubscribeEvent
    public void onPlayerTeleportEvent(StefanutilEvents.playerTeleportEvent event) {
        for (BasicModule m : loadedModules) {
            if (m.isToggled()) {
                m.onPlayerTeleportEvent(event);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHighestClientTick(TickEvent.ClientTickEvent event) {
        for (BasicModule m : loadedModules) {
            if (m.isToggled()) {
                m.onHighestClientTick(event);
            }
        }
    }

    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        for (BasicModule m : loadedModules) {
            if (m.isToggled()) {
                m.onUnloadWorld(event);
            }
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent e) {
        for (BasicModule m : loadedModules) {
            if (m.isToggled()) {
                m.onGuiOpen(e);
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        for (BasicModule m : loadedModules) {
            if (m.isToggled()) {
                m.onTick(e);
            }
        }
    }

    @SubscribeEvent
    public void onTickRenderTick(TickEvent.RenderTickEvent event) {
        for (BasicModule m : loadedModules) {
            if (m.isToggled()) {
                m.onRenderTick(event);
            }
        }

    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent e) {
        for (BasicModule m : loadedModules) {
            if (m.isToggled()) {
                m.onWorldRender(e);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerFall(StefanutilEvents.playerFallEvent e) {
        for (BasicModule m : loadedModules) {
            if (m.isToggled()) {
                m.onPlayerFall();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTeleport(StefanutilEvents.playerTeleportEvent e) {
        for (BasicModule m : loadedModules) {
            if (m.isToggled()) {
                m.onPlayerTeleport();
            }
        }
    }

    @SubscribeEvent
    public void onGuiRender(RenderGameOverlayEvent e) {
        for (BasicModule m : loadedModules) {
            if (m.isToggled()) {
                m.onGuiRender(e);
            }
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        for (BasicModule m : loadedModules) {
            if (m.isToggled()) {
                m.onChat(e);
            }
        }
    }

    @SubscribeEvent
    public void keyevent(InputEvent.KeyInputEvent e) {
        if (Minecraft.getMinecraft().theWorld == null) return;
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (!Keyboard.isCreated()) return;
        if (!Keyboard.getEventKeyState()) return;

        int keyCode = Keyboard.getEventKey();
        if (keyCode <= 0)
            return;

        for (BasicModule m : loadedModules) {
            if (m.isToggled()) {
                m.onKeyInput(e);
            }
            m.getLocalDecoratorManager().decoratorArrayList.forEach(decorator -> {
                if (decorator.getClass().getName().equals(keyBindDecorator.class.getName())) {
                    keyBindDecorator a = (keyBindDecorator) decorator;
                    if (a.keybind.getKeyCode() == keyCode) {
                        a.fireKeyBind();
                    }
                }
            });
        }
        if (keyCode == Keyboard.KEY_APOSTROPHE) Minecraft.getMinecraft().displayGuiScreen(ClickGui.getClickGui());

    }
}
