package io.github.kingstefan26.kokomod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;

import static net.minecraft.event.ClickEvent.Action.OPEN_URL;

public class sendChatMessage {
    private static final String MESSAGE_PREFIX = "[§bKokomods§f] ";

    public static synchronized void sendClientMessage(String text, boolean prefix) {
        ClientChatReceivedEvent event = new ClientChatReceivedEvent((byte) 1, new ChatComponentText((prefix ? MESSAGE_PREFIX : "") + text));

        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(event.message);
        }
    }

    public static synchronized void sendClientMessageWithRickRoll(String text, boolean prefix) {
        ChatComponentText a = new ChatComponentText((prefix ? MESSAGE_PREFIX : "") + text);
        a.getChatStyle().setChatClickEvent(new ClickEvent(OPEN_URL, "https://youtu.be/dQw4w9WgXcQ"));
        ClientChatReceivedEvent event = new ClientChatReceivedEvent((byte) 1, a);

        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(event.message);
        }
    }

    public static synchronized void sendClientMessage(ChatComponentText message) {
        ClientChatReceivedEvent event = new ClientChatReceivedEvent((byte) 1, message);

        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(event.message);
        }
    }
}
