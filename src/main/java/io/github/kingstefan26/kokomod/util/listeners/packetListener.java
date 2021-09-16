package io.github.kingstefan26.kokomod.util.listeners;


import io.github.kingstefan26.kokomod.util.forgeEventClasses.receivedKeepAlivePacketEvent;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraftforge.common.MinecraftForge;

public class packetListener extends ChannelDuplexHandler {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Packet packet = (Packet) msg;
		System.out.println(packet);

		if (packet instanceof C00PacketKeepAlive) {
			MinecraftForge.EVENT_BUS.post(new receivedKeepAlivePacketEvent());
		}
		super.channelRead(ctx, packet);
	}
}
