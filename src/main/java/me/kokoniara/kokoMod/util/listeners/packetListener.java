package me.kokoniara.kokoMod.util.listeners;


import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import me.kokoniara.kokoMod.util.forgeEventClasses.keepAlivePacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.server.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class packetListener extends ChannelDuplexHandler {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Packet packet = (Packet) msg;
		System.out.println(packet);

		if (packet instanceof C00PacketKeepAlive) {
			MinecraftForge.EVENT_BUS.post(new keepAlivePacketEvent());
		}
		super.channelRead(ctx, packet);
	}
}
