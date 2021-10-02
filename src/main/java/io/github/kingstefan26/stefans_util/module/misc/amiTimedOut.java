package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;
import io.github.kingstefan26.stefans_util.util.forgeEventClasses.receivedKeepAlivePacketEvent;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import io.github.kingstefan26.stefans_util.util.sendChatMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;

public class amiTimedOut extends Module {
	private long checkTimer;
	private boolean wasEnabled;


	public amiTimedOut() {
		super("am i Timed out?", "shows warnings based on your network status", ModuleManager.Category.MISC, true);
		this.enableMessage = "amitimedout enabled";
		this.disableMessage = "amitimedout diasabled";
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.ClientTickEvent e) {
		if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;
		if(System.currentTimeMillis() - checkTimer > 2000){
			mc.displayGuiScreen(new GuiScreen() {
				private final drawCenterString drawCenterStringOBJ = drawCenterString.getdrawCenterString();
				@Override
				public void initGui() {
        		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - (this.height / 4) + 10, "couldn't care less"));
					super.initGui();
				}

				@Override
				public void drawScreen(int mouseX, int mouseY, float partialTicks) {
					this.drawDefaultBackground();
					drawCenterStringOBJ.drawCenterStringOnScreen(mc, "server stopped responding", "ff002f");
					super.drawScreen(mouseX, mouseY, partialTicks);
				}
    			@Override
				protected void actionPerformed(GuiButton button) throws IOException {
					if (button.id == 0) {
						mc.thePlayer.closeScreen();
					}
				}
				@Override
				public boolean doesGuiPauseGame() {
					return false;
				}
			});
			wasEnabled = true;
		}else{
			if(wasEnabled){
				mc.setIngameFocus();
				wasEnabled = false;
			}
		}
	}

	@SubscribeEvent
	public void onKeepAlivePacet(receivedKeepAlivePacketEvent e){
//		sendChatMessage.sendClientMessage("keep alive packet pog???", false);
		checkTimer = System.currentTimeMillis();

	}



	@Override
	public void onEnable() {
		if(mc.isSingleplayer()){
			sendChatMessage.sendClientMessage("you are in singleplayer nono", true);
			this.setToggled(false);
			return;
		}
		checkTimer = System.currentTimeMillis();
		super.onEnable();
	}
}