package io.github.kingstefan26.stefans_util.module.misc.amiTimedOut;

import io.github.kingstefan26.stefans_util.core.module.Category;
import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;
import io.github.kingstefan26.stefans_util.util.forgeEventClasses.receivedKeepAlivePacketEvent;
import io.github.kingstefan26.stefans_util.util.sendChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class amiTimedOut extends Module {
	private long checkTimer;
	private boolean wasEnabled;


	public amiTimedOut() {
		super("am i Timed out?", "shows warnings based on your network status", Category.MISC, true);
		this.enableMessage = "amitimedout enabled";
		this.disableMessage = "amitimedout diasabled";
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.ClientTickEvent e) {
		if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;
		if(System.currentTimeMillis() - checkTimer > 2000){
			mc.displayGuiScreen(new amiTimedOutGui());
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