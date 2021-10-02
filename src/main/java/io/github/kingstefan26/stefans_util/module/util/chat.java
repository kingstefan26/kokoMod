package io.github.kingstefan26.stefans_util.module.util;

import io.github.kingstefan26.stefans_util.core.module.UtilModule;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;


public class chat extends UtilModule {
    private static final String MESSAGE_PREFIX = "[§bKokomods§f] ";
    private static final String DEBUG_PREFIX = "[§9KOKOMOD-DEBUG§9§r] ";
    public enum chatEnum{
        DEBUG,
        CHAT,
        CHATNOPREFIX,
        CHATCOMPONENT
        }
    static HashMap<chatEnum, Object> chatSendQueue = new HashMap<chatEnum, Object>();

    public chat() {
        super("chat sender/receiver Module");
    }

    public static void queueClientChatMessage(Object message, chatEnum type){
        chatSendQueue.put(type, message);
    }

    @Override
    public void onTick(TickEvent.Type type, Side side, TickEvent.Phase phase){
        if(Minecraft.getMinecraft().theWorld != null || Minecraft.getMinecraft().thePlayer != null){
            Iterator<Map.Entry<chatEnum, Object>> it = chatSendQueue.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = it.next();
                sendClientMessage(pair.getValue(), (chatEnum) pair.getKey());
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
    }

    private static synchronized void sendClientMessage(Object content, chatEnum e) {
        ClientChatReceivedEvent event;
        switch(e){
            case DEBUG:
                event = new ClientChatReceivedEvent((byte) 1, new ChatComponentText(DEBUG_PREFIX + content));
                break;
            case CHAT:
                event = new ClientChatReceivedEvent((byte) 1, new ChatComponentText(MESSAGE_PREFIX + content));
                break;
            case CHATNOPREFIX:
                event = new ClientChatReceivedEvent((byte) 1, new ChatComponentText((String) content));
                break;
            case CHATCOMPONENT:
                event = new ClientChatReceivedEvent((byte) 1, (ChatComponentText)content);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + e);
        }

        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(event.message);
        }
    }
}
