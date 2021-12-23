package io.github.kingstefan26.stefans_util.core.rewrite.clickGui.components;

import io.github.kingstefan26.stefans_util.util.CustomFont;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.io.InputStream;

public class newComponent {

    private InputStream getFileFromResourceAsStream(String fileName) {
// get a file from the resources folder
// works everywhere, IDEA, unit test and JAR file.

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public boolean closed;

    //    public CustomFont c = new CustomFont(new Font("JetBrains Mono", Font.BOLD, 20), 20);
    public CustomFont c = new CustomFont(getFileFromResourceAsStream("assets/stefan_util/textures/font/Teko-Light.ttf"), 20);
//    public CustomFont p1 = new CustomFont(new Font("JetBrains Mono", Font.PLAIN, 17), 17);
    public CustomFont p1 = new CustomFont(getFileFromResourceAsStream("assets/stefan_util/textures/font/Teko-Light.ttf"), 17);


    public static void drawRect(int left, int top, int right, int bottom, int color) {
        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();

        if (left < right) {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
    }

    public void renderComponent() {

    }

    public void updateComponent(int mouseX, int mouseY) {

    }

    public void mouseClicked(int mouseX, int mouseY, int button) {

    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public int getParentHeight() {
        return 0;
    }

    public void keyTyped(char typedChar, int key) {

    }

    public void setOff(int newOff) {

    }

    public int getHeight() {
        return 0;
    }
}
