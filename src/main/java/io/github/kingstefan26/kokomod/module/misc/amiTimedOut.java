package io.github.kingstefan26.kokomod.module.misc;

import io.github.kingstefan26.kokomod.core.module.Category;
import io.github.kingstefan26.kokomod.core.module.blueprints.Module;
import io.github.kingstefan26.kokomod.util.forgeEventClasses.keepAlivePacketEvent;
import io.github.kingstefan26.kokomod.util.sendChatMessage;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.Session;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class amiTimedOut extends Module {
	int ping;
	private long serverPing;
	private long checkTimer;
	String currentIp;


	public amiTimedOut() {
		super("am i Timed out?", "shows if you timed out", Category.MISC, true, " amitimedout enabled", " amitimedout diasabled");
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.ClientTickEvent e) {
		if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;
        serverPing = mc.getCurrentServerData().pingToServer;
		updatePing();



		if (System.currentTimeMillis() - checkTimer > 1000) {
			checkTimer = System.currentTimeMillis();
			sendChatMessage.sendClientMessage("pingToServer: " + serverPing, false);
			sendChatMessage.sendClientMessage(".getResponseTime(): " + ping, false);
		}
	}

	@SubscribeEvent
	public void onKeepAlivePacet(keepAlivePacketEvent e){
		sendChatMessage.sendClientMessage("keep alive packet pog???", false);
	}

//C00PacketKeepAlive
	@Override
	public void onEnable() {
		currentIp =  mc.getCurrentServerData().serverIP;
		super.onEnable();
		checkTimer = System.currentTimeMillis();
	}

	public void updatePing() {
		NetHandlerPlayClient netHandlerPlayClient = mc.getNetHandler();
		if (netHandlerPlayClient == null) return;

		Session session = mc.getSession();
		if (session == null || session.getProfile() == null || session.getProfile().getId() == null) return;

		NetworkPlayerInfo networkPlayerInfo = netHandlerPlayClient.getPlayerInfo(session.getProfile().getId());

		if (networkPlayerInfo != null) {
			ping = networkPlayerInfo.getResponseTime();
		}
		ping = networkPlayerInfo.getResponseTime();
	}

}