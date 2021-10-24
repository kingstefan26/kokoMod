package io.github.kingstefan26.stefans_util.service.impl;

import io.github.kingstefan26.stefans_util.service.Service;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class notificationService extends Service {

    private static final List<Notification> notifications = new CopyOnWriteArrayList<>();

    public static void push(Notification n) {
        notifications.add(n);
    }

    @Override
    public void start() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void stop() {

    }

    public static class Notification {
        public Notification(String description, String title, int duration) {
            this.description = description;
            this.title = title;
            this.duration = duration;
        }

        public int x, y;
        boolean closing;
        float time;
        int colour = 0x5555FF;

        public int duration;
        public String text, title;
        String description;
    }

    public float lerp(float start, float end, float interpolation) {
        return start + (end - start) * clamp01(interpolation);
    }

    public float clamp01(float value) {
        if ((double) value < 0.0)
            return 0.0f;
        return (double) value > 1.0 ? 1f : value;
    }


    public void startScissorBox(int x, int y, int width, int height) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        totalScissor(x, y, width, height);
    }

    public void startScissorBox(float x, float y, float width, float height) {
        startScissorBox((int) x, (int) y, (int) width, (int) height);
    }

    public void endScissorBox() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }

    public void totalScissor(double xPosition, double yPosition, double width, double height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaledWidth = scaledResolution.getScaledWidth();
        int windowWidth = mc.displayWidth;
        int scaledHeight = scaledResolution.getScaledHeight();
        int windowHeight = mc.displayHeight;
        GL11.glScissor(
                (int) ((xPosition * windowWidth) / scaledWidth),
                (int) (((scaledHeight - (yPosition + height)) * windowHeight) / scaledHeight),
                (int) (width * windowWidth / scaledWidth),
                (int) (height * windowHeight / scaledHeight)
        );
    }

    private List<String> wrapTextLines(String text, int width, String split) {
        String wrapped = wrapText(text, width, split);
        if (wrapped.isEmpty())
            return new ArrayList<>();
        return Arrays.asList(wrapped.split("\n"));
    }

    private String wrapText(String text, int width, String split) {
        String[] words = text.split("(" + split + "|\n)");
        int lineLength = 0;
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i != words.length - 1)
                word += split;
            int wordLength = mc.fontRendererObj.getStringWidth(word);
            if (lineLength + wordLength <= width) {
                output.append(word);
                lineLength += wordLength;
            } else if (wordLength <= width) {
                output.append("\n").append(word);
                lineLength = wordLength;
            } else
                output.append(wrapText(word, width, "")).append(split);
        }
        return output.toString();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent r) {
        render(r.renderTickTime);
    }


    public notificationService() {
        super("notificationEngine");
    }

    public void render(float ticks) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaledWidth = scaledResolution.getScaledWidth();
        int scaledHeight = scaledResolution.getScaledHeight();

        float y = 5;
        for (Notification notification : notifications) {
            if (notifications.indexOf(notification) > 2) {
                continue;
            }

            if (notification.x < 1) {
                notification.x = scaledWidth;
            }

            int duration = (notification.duration == -1 ? 4 : notification.duration);

            /* Text. */
            String title = notification.title;
            float width = 225;
            List<String> wrappedTitle = wrapTextLines(title, (int) (width - 10), " ");
            List<String> wrappedDescription = wrapTextLines(notification.description, (int) (width - 10), " ");
            int textLines = wrappedTitle.size() + wrappedDescription.size();

            /* Size and positon. */
            float height = 18 + (textLines * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT);
            float x = notification.x = (int) lerp(notification.x, scaledWidth - width - 5, ticks / 4);
            if (notification.closing && notification.time < 0.75f) {
                x = notification.x = (int) lerp(notification.x, scaledWidth + width, ticks / 4);
            }

            /* Rendering. */
            GlStateManager.pushMatrix();


            Gui.drawRect((int) x, (int) y, (int) width, (int) height, notification.colour);
            //drawHollowRect((int) x + 4, (int) y + 4, (int) width - 8, (int) height - 8, 1, 0xFFFFFFFF);
            Gui.drawRect((int) x + 4, (int) y + 4, (int) width - 8, (int) height - 8, 0xFFFFFFFF);

            /* Text. */
            if (notification.time > 0.1f) {
                startScissorBox(x, y, width, height);
                int i = 0;
                for (String line : wrappedTitle) {
                    mc.fontRendererObj.drawString(line, x + 8, y + 8 + (i * 2) + (i * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT), 0x222222, true);
                    i++;
                }
                for (String line : wrappedDescription) {
                    mc.fontRendererObj.drawString(line, x + 8, y + 8 + (i * 2) + (i * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT), 0x222222, true);
                    i++;
                }
                endScissorBox();
            }
            GlStateManager.popMatrix();

            /* Positioning. */
            y += height + 5;

            /* Other handling things. */
            if (notification.time >= duration) {
                notification.closing = true;
            }


            notification.time += (notification.closing ? -0.02 : 0.02) * (ticks * 3);


            if (notification.closing && notification.time <= 0) {
                notifications.remove(notification);
            }
        }
    }
}
