package io.github.kingstefan26.stefans_util.util.handelers;

import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacketHandler extends ChannelDuplexHandler {
    Logger packetLogger = LogManager.getLogger("packetLogger");
    public long lastping;
    public long lastpong;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (WorldInfoService.isOnHypixel() && msg instanceof Packet && msg.getClass().getName().endsWith("S00PacketKeepAlive")) {
            MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.receivedKeepAlivePacketEvent());
            lastpong = System.nanoTime();
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (WorldInfoService.isOnHypixel() && msg instanceof Packet && msg.getClass().getName().endsWith("C00PacketKeepAlive")) {
            lastping = System.nanoTime();
        }
        super.write(ctx, msg, promise);
    }
}
