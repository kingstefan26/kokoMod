package io.github.kingstefan26.stefans_util.util;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author TheObliterator
 *         <p/>
 *         A class to create and draw true type
 *         fonts onto the Minecraft game engine.
 */
public class CustomFont {

    private int texID;
    private int[] xPos;
    private int[] yPos;
    private int startChar;
    private int endChar;
    private FontMetrics metrics;

    /**
     * Instantiates the font, filling in default start
     * and end character parameters.
     * <p/>
     * 'new CustomFont(ModLoader.getMinecraftInstance(),
     * "Arial", 12);
     *
     * @param mc   The Minecraft instance for the font to be bound to.
     * @param font The name of the font to be drawn.
     * @param size The size of the font to be drawn.
     */
    public CustomFont(Minecraft mc, Object font, int size) {
        this(mc, font, size, 32, 126);
    }

    /**
     * Instantiates the font, pre-rendering a sprite
     * font image by using a true type font on a
     * bitmap. Then allocating that bitmap to the
     * Minecraft rendering engine for later use.
     * <p/>
     * 'new CustomFont(ModLoader.getMinecraftInstance(),
     * "Arial", 12, 32, 126);'
     *
     * @param mc        The Minecraft instance for the font to be bound to.
     * @param font      The name of the font to be drawn.
     * @param size      The size of the font to be drawn.
     * @param startChar The starting ASCII character id to be drawable. (Default 32)
     * @param endChar   The ending ASCII character id to be drawable. (Default 126)
     */
    public CustomFont(Minecraft mc, Object font, int size, int startChar, int endChar) {
        this.startChar = startChar;
        this.endChar = endChar;
        xPos = new int[endChar - startChar];
        yPos = new int[endChar - startChar];

        // Create a bitmap and fill it with a transparent color as well
        // as obtain a Graphics instance which can be drawn on.
        // NOTE: It is CRUICIAL that the size of the image is 256x256, if
        // it is not the Minecraft engine will not draw it properly.
        BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        Graphics g1 = img.getGraphics();
        Graphics2D g = (Graphics2D) g1;
        try {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            if (font instanceof String) {
                String fontName = (String) font;
                if (fontName.contains("/"))
                    g.setFont(Font.createFont(Font.TRUETYPE_FONT, new File(fontName)).deriveFont((float) size));
                else
                    g.setFont(new Font(fontName, Font.PLAIN, size));
            } else if (font instanceof InputStream) {
                g.setFont(Font.createFont(Font.TRUETYPE_FONT, (InputStream) font).deriveFont((float) size));
            } else if (font instanceof File) {
                g.setFont(Font.createFont(Font.TRUETYPE_FONT, (File) font).deriveFont((float) size));
            } else if(font instanceof Font){
                g.setFont((Font)font);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, 256, 256);
        g.setColor(Color.white);
        metrics = g.getFontMetrics();

        // Draw the specified range of characters onto
        // the new bitmap, spacing according to the font
        // widths. Also allocating positions of characters
        // on the bitmap to two arrays which will be used
        // later when drawing.
        int x = 2;
        int y = 2;
        for (int i = startChar; i < endChar; i++) {
            g.drawString("" + ((char) i), x, y + g.getFontMetrics().getAscent());
            xPos[i - startChar] = x;
            yPos[i - startChar] = y - metrics.getMaxDescent();
            x += metrics.stringWidth("" + (char) i) + 2;
            if (x >= 250 - metrics.getMaxAdvance()) {
                x = 2;
                y += metrics.getMaxAscent() + metrics.getMaxDescent() + size / 2;
            }
        }

        // Render the finished bitmap into the Minecraft
        // graphics engine.
        texID = new DynamicTexture(img).getGlTextureId();
    }

    /**
     * Draws a given string with an automatically
     * calculated shadow below it.
     *
     * @param text  The string to be drawn
     * @param x     The x position to start drawing
     * @param y     The y position to start drawing
     * @param color The color of the non-shadowed text (Hex)
     */
    public void drawStringS(String text, int x, int y, int color) {
        int l = color & 0xff000000;
        int shade = (0x000000) >> 2;
        shade += l;
        drawString(text, x + 1, y + 1, shade);
        drawString(text, x, y, color);
    }

    /**
     * Draws a given string onto a gui/subclass.
     *
     * @param text  The string to be drawn
     * @param x     The x position to start drawing
     * @param y     The y position to start drawing
     * @param color The color of the non-shadowed text (Hex)
     */
    public void drawString(String text, int x, int y, int color) {
        GlStateManager.enableBlend();
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5,0.5,0.5);
        GlStateManager.bindTexture(texID);
        float red = (float) (color >> 16 & 0xff) / 255F;
        float green = (float) (color >> 8 & 0xff) / 255F;
        float blue = (float) (color & 0xff) / 255F;
        float alpha = (float) (color >> 24 & 0xff) / 255F;
        GlStateManager.color(red, green, blue, alpha);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            try {
                drawChar(c, x, y);
            } catch (Exception ignored) {}
            x += metrics.getStringBounds("" + c, null).getWidth();
        }
        GlStateManager.scale(2,2,2);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
    }

    /**
     * Returns the created FontMetrics
     * which is used to retrive various
     * information about the True Type Font
     *
     * @return FontMetrics of the created font.
     */
    public FontMetrics getMetrics() {
        return metrics;
    }

    /**
     * Gets the drawing width of a given
     * string of string.
     *
     * @param text The string to be measured
     * @return The width of the given string.
     */
    public int getStringWidth(String text) {
        return (int) getBounds(text).getWidth();
    }

    /**
     * Gets the drawing height of a given
     * string of string.
     *
     * @param text The string to be measured
     * @return The height of the given string.
     */
    public int getStringHeight(String text) {
        return (int) getBounds(text).getHeight();
    }

    /**
     * A method that returns a Rectangle that
     * contains the width and height demensions
     * of the given string.
     *
     * @param text The string to be measured
     * @return Rectangle containing width and height that
     *         the text will consume when drawn.
     */
    private Rectangle getBounds(String text) {
        int w = 0;
        int h = 0;
        int tw = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\\') {
                char type = text.charAt(i + 1);

                if (type == 'n') {
                    h += metrics.getAscent() + 2;
                    if (tw > w)
                        w = tw;
                    tw = 0;
                }
                i++;
                continue;
            }
            tw += metrics.stringWidth("" + c);
        }
        if (tw > w)
            w = tw;
        h += metrics.getAscent();
        return new Rectangle(0, 0, w, h);
    }

    /**
     * Private drawing method used within other
     * drawing methods.
     */
    private void drawChar(char c, int x, int y) {
        Rectangle2D bounds = metrics.getStringBounds("" + c, null);
        drawTexturedModalRect(x, y, xPos[(byte) c - startChar], yPos[(byte) c - startChar], (int) bounds.getWidth(), (int) bounds.getHeight() + metrics.getMaxDescent() + 2);
    }

    private void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(x + 0), (double)(y + height), (double)0).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), (double)0).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + 0), (double)0).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        worldrenderer.pos((double)(x + 0), (double)(y + 0), (double)0).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }

    public int getCharWidth(char var1) {
        return (int) this.getBounds(Character.toString(var1)).getWidth() / 2;
    }

    public java.util.List<String> listFormattedStringToWidth(String var1, int var2) {
        return Arrays.asList(this.wrapFormattedStringToWidth(var1, var2).split("\n"));
    }

    String wrapFormattedStringToWidth(String var1, int var2) {
        int var3 = this.sizeStringToWidth(var1, var2);

        if (var1.length() <= var3) {
            return var1;
        } else {
            String var4 = var1.substring(0, var3);
            String var5 = getFormatFromString(var4)
                    + var1.substring(var3 + (var1.charAt(var3) == 32 ? 1 : 0));
            return var4 + "\n" + this.wrapFormattedStringToWidth(var5, var2);
        }
    }

    private int sizeStringToWidth(String var1, int var2) {
        int var3 = var1.length();
        int var4 = 0;
        int var5 = 0;
        int var6 = -1;

        for (boolean var7 = false; var5 < var3; ++var5) {
            char var8 = var1.charAt(var5);

            switch (var8) {
                case 32:
                    var6 = var5;

                case 167:
                    if (var5 < var3 - 1) {
                        ++var5;
                        char var9 = var1.charAt(var5);

                        if (var9 != 108 && var9 != 76) {
                            if (var9 == 114 || var9 == 82) {
                                var7 = false;
                            }
                        } else {
                            var7 = true;
                        }
                    }

                    break;

                default:
                    var4 += this.getCharWidth(var8);

                    if (var7) {
                        ++var4;
                    }
            }

            if (var8 == 10) {
                ++var5;
                var6 = var5;
                break;
            }

            if (var4 > var2) {
                break;
            }
        }

        return var5 != var3 && var6 != -1 && var6 < var5 ? var6 : var5;
    }

    private static String getFormatFromString(String var0) {
        String var1 = "";
        int var2 = -1;
        int var3 = var0.length();

        while ((var2 = var0.indexOf(167, var2 + 1)) != -1) {
            if (var2 < var3 - 1) {
                char var4 = var0.charAt(var2 + 1);

                if (isFormatColor(var4)) {
                    var1 = "\u00a7" + var4;
                } else if (isFormatSpecial(var4)) {
                    var1 = var1 + "\u00a7" + var4;
                }
            }
        }

        return var1;
    }

    private static boolean isFormatColor(char var0) {
        return var0 >= 48 && var0 <= 57 || var0 >= 97 && var0 <= 102
                || var0 >= 65 && var0 <= 70;
    }

    private static boolean isFormatSpecial(char var0) {
        return var0 >= 107 && var0 <= 111 || var0 >= 75 && var0 <= 79
                || var0 == 114 || var0 == 82;
    }
}

