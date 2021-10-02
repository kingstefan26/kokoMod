package io.github.kingstefan26.stefans_util.util;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;

import static net.minecraft.event.ClickEvent.Action.OPEN_URL;

@Deprecated
public class sendChatMessage {
    private static final String MESSAGE_PREFIX = "[§bKokomods§f] ";
    private static final String DEBUG_PREFIX = "[§9KOKOMOD-DEBUG§9§r] ";

    public static synchronized void sendClientMessage(String text, boolean prefix) {
        ClientChatReceivedEvent event = new ClientChatReceivedEvent((byte) 1, new ChatComponentText((prefix ? MESSAGE_PREFIX : "") + text));

        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(event.message);
        }
    }

    public static synchronized void sendClientDebugMessage(String text) {
        ClientChatReceivedEvent event = new ClientChatReceivedEvent((byte) 1, new ChatComponentText(DEBUG_PREFIX + text));

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
