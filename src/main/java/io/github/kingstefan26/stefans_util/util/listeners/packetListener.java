package io.github.kingstefan26.stefans_util.util.listeners;


import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraftforge.common.MinecraftForge;

public class packetListener extends ChannelDuplexHandler {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Packet packet = (Packet) msg;
		System.out.println(packet);

		if (packet instanceof C00PacketKeepAlive) {
			MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.receivedKeepAlivePacketEvent());
		}
		super.channelRead(ctx, packet);
	}
}
