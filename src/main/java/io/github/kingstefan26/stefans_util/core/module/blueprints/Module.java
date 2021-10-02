package io.github.kingstefan26.stefans_util.core.module.blueprints;

import io.github.kingstefan26.stefans_util.core.clickgui.ClickGui;
import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.Category;
import io.github.kingstefan26.stefans_util.util.sendChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.UUID;


public class Module implements moduleInterface {
	final String uuid = UUID.randomUUID().toString().replace("-", "");

	protected static Minecraft mc = Minecraft.getMinecraft();

	private final String name;
	private final Category category;

	public String description, enableMessage, disableMessage;
	private int key;

	public boolean closed;
	private boolean toggled;
	private boolean visible;
	configObject visibleConfigObject;
	private boolean keybindEnabled = false;
	private KeyBinding fmlkeybindObject;
	protected Logger logger;


	public Module(String name, String description, Category category,boolean keybindEnabled) {
		super();
		this.name = name;
		this.description = description;
		this.keybindEnabled = keybindEnabled;
		if(keybindEnabled){
			this.fmlkeybindObject = new KeyBinding(this.description, key, "kokoMod");
			ClientRegistry.registerKeyBinding(this.fmlkeybindObject);
		}
		this.category = category;
		this.toggled = false;
		visibleConfigObject = new configObject("visibility", this.name, true);
		visible = visibleConfigObject.getBooleanValue();
		logger = LogManager.getLogger(name);
	}

	public Module(String name, String description, Category category) {
		super();
		this.name = name;
		this.description = description;
		this.category = category;
		this.toggled = false;
		visibleConfigObject = new configObject("visibility", this.name, true);
		visible = visibleConfigObject.getBooleanValue();
		logger = LogManager.getLogger(name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getKey() {
		if(closed) return 0;
		if(keybindEnabled){
			this.key = this.fmlkeybindObject.getKeyCode();
		}
		return this.key;
	}
	public KeyBinding getKeyBindingObj() {
		return this.fmlkeybindObject;
	}

	public void setKey(int key) {
		this.key = key;
		if(keybindEnabled){
			this.fmlkeybindObject.setKeyCode(key);
		}
	}

	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggled) {
		if(closed) return;
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

	@Override
	public void onEnable() {
		if(closed) return;
		MinecraftForge.EVENT_BUS.register(this);
		if(this.enableMessage != null) sendChatMessage.sendClientMessage(this.enableMessage, true);
	}

	@Override
	public void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
		if(this.disableMessage != null) sendChatMessage.sendClientMessage(this.disableMessage, true);
	}

	public void setInvisible(){
		this.visible = false;
		visibleConfigObject.setBooleanValue(false);
	}
	public void setVisible(){
		this.visible = true;
		visibleConfigObject.setBooleanValue(true);
	}
	public boolean getVisibility(){
		return this.visible;
	}
	public void toggleVisibility(){
		if(this.visible){
			this.visible = false;
			visibleConfigObject.setBooleanValue(false);
		}else {
			this.visible = true;
			visibleConfigObject.setBooleanValue(true);
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public Category getCategory() {
		return this.category;
	}

	@Override
	public void onLoad() {
		ClickGui.getClickGui().registerComponent(this);
	}

	@Override
	public void onTick(TickEvent.Type type, Side side, TickEvent.Phase phase) {

	}

	@Override
	public void onWorldRender(RenderGlobal context, float partialTick) {

	}

	@Override
	public void onPlayerFall() {

	}

	@Override
	public void onPlayerTeleport() {

	}

	@Override
	public void onGuiRender(float partialTicks, ScaledResolution resolution, RenderGameOverlayEvent.ElementType type) {

	}

	@Override
	public void onUnload() {
		this.onDisable();
		this.closed = true;
	}
}
