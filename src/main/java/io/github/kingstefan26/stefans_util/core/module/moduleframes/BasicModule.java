package io.github.kingstefan26.stefans_util.core.module.moduleframes;

import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.module.interfaces.baseModuleInterface;
import io.github.kingstefan26.stefans_util.core.module.interfaces.moduleMinecraftInterfaceEvents;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.decoratorInterface;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.localDecoratorManager;
import io.github.kingstefan26.stefans_util.core.module.modulemenagers.ModuleManager;
import io.github.kingstefan26.stefans_util.util.StefanutilEvents;
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


public abstract class BasicModule implements baseModuleInterface, moduleMinecraftInterfaceEvents {
    protected static Minecraft mc = Minecraft.getMinecraft();
    protected Logger logger;
    private final ModuleManager.Category category;
    String name;
    String description;
    private boolean closed;
    private boolean toggled;

    private localDecoratorManager localDecoratorManager;

    protected BasicModule(String name,
                          String description,
                          ModuleManager.Category category,
                          decoratorInterface... decorators) {
        setLocalDecoratorManager(new localDecoratorManager(this, decorators));
        this.name = name;
        this.description = description;
        this.category = category;
        this.logger = LogManager.getLogger(name);
        if (name.length() > 15) logger.warn("lenght limit execied {}", name);
        for (decoratorInterface m : getLocalDecoratorManager().decoratorArrayList) {
            m.onInit(this);
        }
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        if (isClosed()) return;
        if (this.toggled && toggled) return;
        if (!this.toggled && !toggled) return;
        this.toggled = toggled;

        if (this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public void toggle() {
        if (isClosed()) return;
        this.toggled = !this.toggled;

        if (this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public ModuleManager.Category getCategory() {
        return this.category;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }



    @Override
    public void onEnable() {
        if (isClosed()) return;
        for (decoratorInterface m : getLocalDecoratorManager().decoratorArrayList) {
            m.onEnable();
        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        for (decoratorInterface m : getLocalDecoratorManager().decoratorArrayList) {
            m.onDisable();
        }
    }

    @Override
    public void onLoad() {

        for (decoratorInterface m : getLocalDecoratorManager().decoratorArrayList) {
            m.onLoad();
        }
        ClickGui.getClickGui().registerComponent(this);
    }

    @Override
    public void onUnload() {
        for (decoratorInterface m : getLocalDecoratorManager().decoratorArrayList) {
            m.onDisable();
        }
        ClickGui.getClickGui().removeComponent(this);
        if(this.toggled) this.onDisable();
        this.setClosed(true);
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
    public void onPlayerTeleportEvent(StefanutilEvents.playerTeleportEvent e) {

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

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public localDecoratorManager getLocalDecoratorManager() {
        return localDecoratorManager;
    }

    public void setLocalDecoratorManager(localDecoratorManager localDecoratorManager) {
        this.localDecoratorManager = localDecoratorManager;
    }
}
