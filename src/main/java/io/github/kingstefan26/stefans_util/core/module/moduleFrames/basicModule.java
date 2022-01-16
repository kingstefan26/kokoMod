package io.github.kingstefan26.stefans_util.core.module.moduleFrames;

import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.interfaces.baseModuleInterface;
import io.github.kingstefan26.stefans_util.core.module.interfaces.moduleMinecraftInterfaceEvents;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.decoratorInterface;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.localDecoratorManager;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


abstract public class basicModule implements baseModuleInterface, moduleMinecraftInterfaceEvents {
    protected static Minecraft mc = Minecraft.getMinecraft();
    protected Logger logger;
    public String name;
    public String description;
    private final moduleManager.Category category;
    public boolean closed, loaded;
    private boolean toggled;

    public localDecoratorManager localDecoratorManager;

    public basicModule(String name, String description, moduleManager.Category category, decoratorInterface... decorators) {
        localDecoratorManager = new localDecoratorManager(this, decorators);
        this.name = name;
        this.description = description;
        this.category = category;
        this.logger = LogManager.getLogger(name);
        for(decoratorInterface m : localDecoratorManager.decoratorArrayList){
            m.onInit(this);
        }
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        if(closed) return;
        if(this.toggled && toggled) return;
        if(!this.toggled && !toggled) return;
        this.toggled = toggled;

        if (this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public void toggle() {
        if(closed) return;
        this.toggled = !this.toggled;

        if (this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public moduleManager.Category getCategory() {
        return this.category;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }


//    final String uuid = UUID.randomUUID().toString().replace("-", "");


    @Override
    public void onEnable() {
        if(closed) return;
        for(decoratorInterface m : localDecoratorManager.decoratorArrayList){
            m.onEnable();
        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        for(decoratorInterface m : localDecoratorManager.decoratorArrayList){
            m.onDisable();
        }
    }

    @Override
    public void onLoad() {
        for(decoratorInterface m : localDecoratorManager.decoratorArrayList){
            m.onLoad();
        }
        ClickGui.getClickGui().registerComponent(this);
        loaded = true;
    }

    @Override
    public void onUnload() {
        for(decoratorInterface m : localDecoratorManager.decoratorArrayList){
            m.onDisable();
        }
        ClickGui.getClickGui().removeComponent(this);
        if(this.toggled) this.onDisable();
        this.closed = true;
        loaded = false;
    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {

    }

    @Override
    public void onRenderTick(TickEvent.RenderTickEvent e) {

    }

    @Override
    public void onWorldRender(RenderWorldLastEvent e) {

    }

    @Override
    public void onPlayerFall() {

    }

    @Override
    public void onPlayerTeleport() {

    }

    @Override
    public void onGuiRender(RenderGameOverlayEvent e) {

    }

    @Override
    public void onChat(ClientChatReceivedEvent e) {

    }

    @Override
    public void onGuiOpen(GuiOpenEvent e) {

    }

    @Override
    public void onPlayerTeleportEvent(stefan_utilEvents.playerTeleportEvent e) {

    }

    @Override
    public void onUnloadWorld(WorldEvent.Unload event) {

    }

    @Override
    public void onHighestClientTick(TickEvent.ClientTickEvent event) {

    }

    @Override
    public void onKeyInput(InputEvent.KeyInputEvent event) {

    }
}
