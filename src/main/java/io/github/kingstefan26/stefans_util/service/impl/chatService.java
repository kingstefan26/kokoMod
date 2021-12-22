package io.github.kingstefan26.stefans_util.service.impl;

import io.github.kingstefan26.stefans_util.service.Service;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;
import java.util.Queue;


public class chatService extends Service {
    public chatService() {
        super("chat sender/receiver Module");
    }

    private static final String MESSAGE_PREFIX = "[§bKokomod§f] ";
    private static final String DEBUG_PREFIX = "[§9KOKOMOD-DEBUG§9§r] ";

    @Override
    public void start() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void stop() {
    }
    public enum chatEnum {
        DEBUG,
        CHATPREFIX,
        CHATNOPREFIX,
        CHATCOMPONENT

    }
    static class message {
        public Object text = null;

        public chatEnum type = null;
        public message(Object text, chatEnum type) {
            this.text = text;
            this.type = type;
        }

    }
    static Queue<message> messageQueue = new LinkedList<>();

    public static boolean lockEnableMessages = true;

    public static void queueClientChatMessage(Object message, chatEnum type) {
        messageQueue.add(new message(message, type));
    }

    public static void queueClientChatMessage(String message) {
        messageQueue.add(new message(message, chatEnum.CHATPREFIX));
    }

    public static void queueCleanChatMessage(String message) {
        if (message == null) return;
        messageQueue.add(new message(message, chatEnum.CHATNOPREFIX));
    }

    public static void queueClientChatMessage(ChatComponentText message) {
        messageQueue.add(new message(message, chatEnum.CHATNOPREFIX));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (Minecraft.getMinecraft().theWorld != null || Minecraft.getMinecraft().thePlayer != null) {
            if (messageQueue.peek() != null) {
                message temp = messageQueue.remove();
                sendClientMessage(temp);
            }
        }
    }


    private static synchronized void sendClientMessage(message message) {
        ClientChatReceivedEvent event;
        switch (message.type) {
            case DEBUG:
                event = new ClientChatReceivedEvent((byte) 1, new ChatComponentText(DEBUG_PREFIX + message.text));
                break;
            case CHATPREFIX:
                event = new ClientChatReceivedEvent((byte) 1, new ChatComponentText(MESSAGE_PREFIX + message.text));
                break;
            case CHATNOPREFIX:
                event = new ClientChatReceivedEvent((byte) 1, new ChatComponentText((String) message.text));
                break;
            case CHATCOMPONENT:
                event = new ClientChatReceivedEvent((byte) 1, (ChatComponentText) message.text);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + message.type);
        }

        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(event.message);
//            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(event.message);
        }
    }

    /**
     * DOES NOT WORK
     */
    public static synchronized void removeLastChatMessage(){
//        List<ChatLine> chatLines = ReflectionHelper.getPrivateValue(GuiNewChat.class, Minecraft.getMinecraft().ingameGUI.getChatGUI(), "chatLines");
        try{
            Minecraft.getMinecraft().ingameGUI.getChatGUI().deleteChatLine(0);
        }catch (Exception ignored){

        }
    }
}
