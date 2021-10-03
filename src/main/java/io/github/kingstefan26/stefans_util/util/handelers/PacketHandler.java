package io.github.kingstefan26.stefans_util.util.handelers;

import io.github.kingstefan26.stefans_util.module.util.SBinfo;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;

public class PacketHandler extends ChannelDuplexHandler{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            if (SBinfo.isOnHypixel() && msg instanceof Packet && msg.getClass().getName().endsWith("S00PacketKeepAlive")) {
//            S01PacketPong packet = (S01PacketPong) msg;
                    MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.receivedKeepAlivePacketEvent());
            }

        super.channelRead(ctx, msg);
    }
}
