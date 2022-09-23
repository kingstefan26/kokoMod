/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.core.config.attotations.impl.DoubleConfigValue;
import io.github.kingstefan26.stefans_util.core.config.attotations.impl.IntegerConfigValue;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.module.modulemenagers.ModuleManager;
import io.github.kingstefan26.stefans_util.core.setting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import javax.vecmath.Vector2f;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class macroSessionTracker extends BasicModule {
    @DoubleConfigValue(name = "totalminedcrops", defaultValue = 0.0)
    double totalminedcrops;

    static long timeZeroPoint = 0;
    private static final String[] suffix = new String[]{"", "k", "m", "b", "t"};

    private static final int PADDING_X = 5;

    Tuple<Integer, String> originalCropValue;

    int lastUpdateCounter;
    private static final int PADDING_Y = 5;
    private static final int MAX_LENGTH = 4;
    long lastUpdate;
    long thisUpdate;

    public macroSessionTracker() {
        super("macroSessionTracker", "traks ur macro session, aka crops money etc", ModuleManager.Category.RENDER);
    }

    @IntegerConfigValue(name = "macroSessionTracker.totalcrops", defaultValue = 0)
    int totalcrops;
    public static int cropsMinedThisSession;
    public static int totalMinedCropsWithKokomod;
    public static long totalFarmingTime;


    boolean shouldRender = true;

    String getCurrentItemUuid() {
        if (Minecraft.getMinecraft().thePlayer == null) return null;

        ItemStack stack = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();

            //if there are ExtraAttributes in nbt tags
            if (tag.hasKey("ExtraAttributes", 10)) {
                //get the ExtraAttributes
                NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");

                if (ea.hasKey("uuid")) {
                    return ea.getString("uuid");
                }
            }
        }
        return null;
    }

    public static long thisSessionFarmingTime;
    public int overlayWidth = -1;


    int getCurrentItemCrops() {
        if (Minecraft.getMinecraft().thePlayer == null) return 0;

        ItemStack stack = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();

            //if there are ExtraAttributes in nbt tags
            if (tag.hasKey("ExtraAttributes", 10)) {
                //get the ExtraAttributes
                NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");

                if (ea.hasKey("mined_crops", 99)) {
                    //get the value of mined_crops from ExtraAttributes
                    //and add it into counterqueue
                    return ea.getInteger("mined_crops");

                    //do the same for farmed_cultivating
                }
            }
        }
        return 0;
    }


    int tickCouter;
    public int overlayHeight = -1;
    Tuple[] GuiScreenText;

    @DoubleConfigValue(name = "macroSessionTracker.totaltime", defaultValue = 0)
    double totaltime;
    boolean showGlobalStats;

    public static String timeToDuration(final long timeInMs) {
        final long dy = TimeUnit.MILLISECONDS.toDays(timeInMs);
        final long hr = TimeUnit.MILLISECONDS.toHours(timeInMs)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(timeInMs));
        final long min = TimeUnit.MILLISECONDS.toMinutes(timeInMs)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMs));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(timeInMs)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMs));

        return (dy != 0 ? dy + "D " : "") + (hr != 0 ? hr + "H " : "") + (min != 0 ? min + "M " : "") + sec + "S";

    }

    public static String coolFormat(double number) {
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }
        return r;
    }

    @Override
    public void onLoad() {
        totalFarmingTime = (long) totaltime;
        totalMinedCropsWithKokomod = totalcrops;


        new CheckSetting("showGlobalStats", this, true, (newvalue) -> showGlobalStats = (boolean) newvalue);

        super.onLoad();
    }

    String getCurrentItemSkyblockId() {
        if (Minecraft.getMinecraft().thePlayer == null) return null;

        ItemStack stack = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();

            //if there are ExtraAttributes in nbt tags
            if (tag.hasKey("ExtraAttributes", 10)) {
                //get the ExtraAttributes
                NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");

                if (ea.hasKey("id")) {
                    return ea.getString("id");
                }
            }
        }
        return null;
    }

    boolean isFirstUpdate;

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START) return;
        tickCouter++;
        if (tickCouter % 20 != 0) return;
        tickCouter = 0;

        if (!WorldInfoService.isInSkyblock() && mc.thePlayer == null) return;


        String temp = getCurrentItemUuid();
        String id = getCurrentItemSkyblockId();
        if (originalCropValue == null && temp != null) {
            if (id.contains("WARTS")) {
                originalCropValue = new Tuple<>(getCurrentItemCrops(), temp);
            }
        }

        if (originalCropValue != null) {
            if (!originalCropValue.getSecond().equals(temp)) {
                shouldRender = false;
                return;
            } else {
                shouldRender = true;
            }
        }

        if (mc.thePlayer.getHeldItem() != null && originalCropValue != null) {

            ItemStack hand = mc.thePlayer.getHeldItem();
            if (hand.getItem().getRegistryName().toLowerCase().contains("hoe")) {
                // update this here

                int currentTotal = getCurrentItemCrops() - originalCropValue.getFirst();

                if (currentTotal != 0) {

                    int change = currentTotal - lastUpdateCounter;
                    cropsMinedThisSession += change;
//                    logger.info("mined since last update " + change);
                    lastUpdateCounter = currentTotal;


                    thisUpdate = System.currentTimeMillis() - timeZeroPoint;
                    long timedistance = thisUpdate - lastUpdate;

//                    System.out.println(timedistance);


                    if (timedistance > 900 && change > 10) {
                        if (isFirstUpdate) {
                            isFirstUpdate = false;
                            timeZeroPoint = System.currentTimeMillis();
                        }
                        thisSessionFarmingTime += timedistance;
                    } else {
//                        MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.stoppedCollectingWart());
                    }

                    lastUpdate = System.currentTimeMillis() - timeZeroPoint;

                    totalcrops = totalMinedCropsWithKokomod + cropsMinedThisSession;
                    totaltime = totalFarmingTime + thisSessionFarmingTime;

                    shouldRender = true;

                    if (showGlobalStats) {
                        GuiScreenText = new Tuple[]{
                                new Tuple<>("crops/this session", coolFormat(cropsMinedThisSession)),
                                new Tuple<>("money/this session", coolFormat(cropsMinedThisSession * 3)),
                                new Tuple<>("time/this session", timeToDuration(thisSessionFarmingTime)),
                                new Tuple<>("crops/total", coolFormat(totalMinedCropsWithKokomod + cropsMinedThisSession)),
                                new Tuple<>("money/total", coolFormat(totalMinedCropsWithKokomod + cropsMinedThisSession * 3)),
                                new Tuple<>("time/total", timeToDuration(totalFarmingTime + thisSessionFarmingTime))
                        };
                    } else {
                        GuiScreenText = new Tuple[]{
                                new Tuple<>("crops/this session", coolFormat(cropsMinedThisSession)),
                                new Tuple<>("money/this session", coolFormat(cropsMinedThisSession * 3)),
                                new Tuple<>("time/this session", timeToDuration(thisSessionFarmingTime))
                        };
                    }


                }
            } else {
                shouldRender = false;
            }
        }

    }

    @Override
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        if (mc.currentScreen != null) return;
        if (shouldRender) {

//            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
//            int y = 2 + (fr.FONT_HEIGHT * 3);
//            if (GuiScreenText == null) return;
//            for (String s : GuiScreenText) {
//                fr.drawString(s, 1, y, 0xffff00, true);
//
//                y += fr.FONT_HEIGHT;
//            }


//            var test = new Tuple[]{
//                    new Tuple<>("Ø crops/this session", coolFormat(cropsMinedThisSession)),
//                    new Tuple<>("Ø money/this session", coolFormat(cropsMinedThisSession * 3)),
//                    new Tuple<>("time/this session", timeToDuration(thisSessionFarmingTime))
//            };

            if (GuiScreenText != null) {
                renderKeyValues(Arrays.asList(GuiScreenText));
            }


        }
    }

    private void render(List<String> strings) {
        if (strings == null) return;

        Vector2f size = getSize(strings);
        overlayHeight = (int) size.y;
        overlayWidth = (int) size.x;

//        Vector2f position = getPosition(overlayWidth, overlayHeight);
        int x = 20;
        int y = 10;

//        TextOverlayStyle style = styleSupplier.get();
//
//        if(style == TextOverlayStyle.BACKGROUND) {
//        }
        Gui.drawRect(x, y, x + overlayWidth, y + overlayHeight, 0x80000000);

        GlStateManager.enableBlend();
        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

        //        Vector2f textOffset = getTextOffset();
//        paddingX += (int) textOffset.x;
//        paddingY += (int) textOffset.y;

        int yOff = 0;
        for (String s : strings) {
            if (s == null) {
                yOff += 3;
            } else {
                for (String s2 : s.split("\n")) {
                    Vector2f pos = new Vector2f(x + PADDING_X, y + PADDING_Y + yOff);
//                    renderLine(s2, pos, dummy);

                    int xPad = (int) pos.x;
                    int yPad = (int) pos.y;

//                    if(style == TextOverlayStyle.FULL_SHADOW) {
//                        String clean = Utils.cleanColourNotModifiers(s2);
//                        for(int xO=-2; xO<=2; xO++) {
//                            for(int yO=-2; yO<=2; yO++) {
//                                if(Math.abs(xO) != Math.abs(yO)) {
//                                    Minecraft.getMinecraft().fontRendererObj.drawString(clean,
//                                            xPad+xO/2f, yPad+yO/2f,
//                                            new Color(0, 0, 0, 200/Math.max(Math.abs(xO), Math.abs(yO))).getRGB(), false);
//                                }
//                            }
//                        }
//                    }
                    Minecraft.getMinecraft().fontRendererObj.drawString(s2,
                            xPad, yPad, 0xffffff, true);

                    yOff += 10;
                }
            }
        }
    }

    private void renderKeyValues(List<Tuple<String, String>> strings) {
        if (strings == null) return;

        Vector2f size = getSizeALT(strings);
        overlayHeight = (int) size.y;
        overlayWidth = (int) size.x;

//        Vector2f position = getPosition(overlayWidth, overlayHeight);
        int x = 20;
        int y = 10;

//        TextOverlayStyle style = styleSupplier.get();
//
//        if(style == TextOverlayStyle.BACKGROUND) {
//        }
        Gui.drawRect(x, y, x + overlayWidth, y + overlayHeight, 0x80000000);

        GlStateManager.enableBlend();
        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

        //        Vector2f textOffset = getTextOffset();
//        paddingX += (int) textOffset.x;
//        paddingY += (int) textOffset.y;

        int yOff = 0;
        for (Tuple<String, String> s : strings) {
            if (s == null) {
                yOff += 3;
            } else {
                Vector2f pos = new Vector2f(x + PADDING_X, y + PADDING_Y + yOff);
//                    renderLine(s2, pos, dummy);

                int xPad = (int) pos.x;
                int yPad = (int) pos.y;


                Minecraft.getMinecraft().fontRendererObj.drawString(s.getFirst(),
                        xPad, yPad, 0xffffff, true);

                // x+overlayWidth = right edge
                // mc.fontRendererObj.getStringWidth(s.getSecond()) = string width
                // yPad = the correct y value

                Minecraft.getMinecraft().fontRendererObj.drawString(s.getSecond(),
                        ((x + overlayWidth) - mc.fontRendererObj.getStringWidth(s.getSecond())) - PADDING_X, yPad, 0xffffff, true);

                yOff += 10;
            }
        }
    }


    protected Vector2f getSize(List<String> strings) {
        int overlayHeight = 0;
        int overlayWidth = 0;
        for (String s : strings) {
            if (s == null) {
                overlayHeight += 3;
                continue;
            }
            for (String s2 : s.split("\n")) {
                int sWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2);
                if (sWidth > overlayWidth) {
                    overlayWidth = sWidth;
                }
                overlayHeight += 10;
            }
        }
        overlayHeight -= 2;

        return new Vector2f(overlayWidth + PADDING_X * 2, overlayHeight + PADDING_Y * 2);
    }

    protected Vector2f getSizeALT(List<Tuple<String, String>> strings) {
        int overlayHeight = 0;
        int overlayWidth = 0;
        for (Tuple<String, String> s : strings) {
            if (s == null) {
                overlayHeight += 3;
                continue;
            }
            String s2 = s.getFirst() + " " + s.getSecond();
            int sWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2);
            if (sWidth > overlayWidth) {
                overlayWidth = sWidth;
            }
            overlayHeight += 10;
        }
        overlayHeight -= 2;

        return new Vector2f(overlayWidth + PADDING_X * 2, overlayHeight + PADDING_Y * 2);
    }


}
