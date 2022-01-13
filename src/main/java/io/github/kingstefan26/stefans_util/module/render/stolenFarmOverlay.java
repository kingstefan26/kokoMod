/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import static io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager.Category.RENDER;

public class stolenFarmOverlay extends basicModule {

    public stolenFarmOverlay() {
        super("stolenoverlay!", "shows tha crops when you have a hoe", RENDER, new presistanceDecorator());
    }


    final String[] GuiScreenText = new String[]{"null", "null", "null"};


    long timer;

    ArrayList<Float> avrageValueStorecps = new ArrayList<>();
    float avragecps;
    long lastUpdate = -1;
    int counterLast = -1;
    int counter = -1;
    float cropsPerSecondLast = 0;
    float cropsPerSecond = 0;
    final LinkedList<Integer> counterQueue = new LinkedList<>();

    private float interp(float now, float last, long lastUpdate) {
        float interp = now;
        if (last >= 0 && last != now) {
            float factor = (System.currentTimeMillis() - lastUpdate) / 1000f;
            factor = Math.max(0, Math.min(1, factor));
            interp = last + (now - last) * factor;
        }
        return interp;
    }

    private synchronized void updateThaInfo() {

        //reset and save the counter
        counterLast = counter;
        counter = -1;

        //if the module is somehow on while player is null stop
        if (Minecraft.getMinecraft().thePlayer == null) return;

        //get item in players hand
        ItemStack stack = Minecraft.getMinecraft().thePlayer.getHeldItem();

        //if the item is not null and it has extra nbt
        if (stack != null && stack.hasTagCompound()) {
            //get the extra nbt
            NBTTagCompound tag = stack.getTagCompound();

            //if there are ExtraAttributes in nbt tags
            if (tag.hasKey("ExtraAttributes", 10)) {
                //get the ExtraAttributes
                NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");

                //if ExtraAttributes has mined crops
                if (ea.hasKey("mined_crops", 99)) {
                    //get the value of mined_crops from ExtraAttributes
                    //and add it into counterqueue
                    counter = ea.getInteger("mined_crops");
                    counterQueue.add(0, counter);
                    //do the same for farmed_cultivating
                } else if (ea.hasKey("farmed_cultivating", 99)) {
                    counter = ea.getInteger("farmed_cultivating");
                    counterQueue.add(0, counter);
                }
            }
        }
        //if the counterQueue is bigger then 3 remove the excess
        while (counterQueue.size() >= 4) {
            counterQueue.removeLast();
        }
        //reset the crops per second if queue is empty
        if (counterQueue.isEmpty()) {
            cropsPerSecond = -1;
            cropsPerSecondLast = 0;
        } else {
            //if queue is not empty

            //save current crops per second
            cropsPerSecondLast = cropsPerSecond;

            //get the last and first elements of the queue
            int last = counterQueue.getLast();
            int first = counterQueue.getFirst();

            //crops per second is equal to the diffrence bettwen the last element and the
            //first divided by 3 (idk why the 3 part maybe its better then just subtraction)
            cropsPerSecond = (first - last) / 3f;
        }


        if (avrageValueStorecps.size() > 20)
            avrageValueStorecps.remove(avrageValueStorecps.size() - 1);


        avrageValueStorecps.add(0, cropsPerSecond);


        float temp = (float) 0;
        for (Float avrageValueStorecp : avrageValueStorecps) {
            temp += avrageValueStorecp;
        }
        avragecps = temp / avrageValueStorecps.size();
    }


    @Override
    public void onEnable() {
        super.onEnable();
        timer = System.currentTimeMillis();
    }

    @Override
    public void onDisable() {
        avrageValueStorecps.clear();
        cropsPerSecond = -1;
    }

    boolean shouldRender;

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

    int tickCouter;

    String uuid;

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START) return;
        tickCouter++;
        if (tickCouter % 20 != 0) return;
        tickCouter = 0;

        if(!WorldInfoService.isInSkyblock() && mc.thePlayer == null) return;


        String temp = getCurrentItemUuid();
        if (temp != null) {
            if (uuid == null) uuid = temp;
            if (!uuid.equals(temp)) {
                avragecps = 0;
                cropsPerSecondLast = 0;
                avrageValueStorecps.clear();
                counterQueue.clear();
                counterLast = -1;
                counter = -1;
                cropsPerSecondLast = 0;
                cropsPerSecond = 0;
                uuid = temp;
            }
        } else {
            uuid = null;
            shouldRender = false;
        }


        if (mc.currentScreen != null) {
            avragecps = 0;
            shouldRender = false;
            return;
        }
        if (mc.thePlayer.getHeldItem() != null) {

            ItemStack hand = mc.thePlayer.getHeldItem();
            if (hand.getItem().getRegistryName().toLowerCase().contains("hoe")) {
                lastUpdate = System.currentTimeMillis();
                shouldRender = true;
                updateThaInfo();
                GuiScreenText[0] = "counter " + coolFormat(counter);
                GuiScreenText[1] = "Ø crops/h " + coolFormat(avragecps * 60 * 60);
                GuiScreenText[2] = "Ø money/h " + coolFormat(avragecps * 60 * 60 * 4);
            } else {
                shouldRender = false;
                avragecps = 0;
                cropsPerSecondLast = 0;
                avrageValueStorecps.clear();
                counterQueue.clear();
                counterLast = -1;
                counter = -1;
                cropsPerSecondLast = 0;
                cropsPerSecond = 0;
            }
        }

    }


    private static final String[] suffix = new String[]{"","k", "m", "b", "t"};
    private static final int MAX_LENGTH = 4;

    private static String coolFormat(double number) {
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while(r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")){
            r = r.substring(0, r.length()-2) + r.substring(r.length() - 1);
        }
        return r;
    }


    @Override
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        if (counter == -1 || mc.currentScreen != null) return;
        if (shouldRender) {

            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            int y = 2;
            for (String s : GuiScreenText) {
                fr.drawString(s, 1, y, 0xffff00, true);

                y += fr.FONT_HEIGHT;
            }
        }
    }


//    private static final int PADDING_X = 5;
//    private static final int PADDING_Y = 5;
//    public int overlayWidth = -1;
//    public int overlayHeight = -1;
//
//    public int getAbsX(ScaledResolution scaledResolution, int objWidth) {
//        int width = scaledResolution.getScaledWidth();
//
//        if(centerX) {
//            return width/2 + x;
//        }
//
//        int ret = x;
//        if(x < 0) {
//            ret = width + x - objWidth;
//        }
//
//        if(ret < 0) ret = 0;
//        if(ret > width - objWidth) ret = width - objWidth;
//
//        return ret;
//    }
//
//    public int getAbsY(ScaledResolution scaledResolution, int objHeight) {
//        int height = scaledResolution.getScaledHeight();
//
//        if(centerY) {
//            return height/2 + y;
//        }
//
//        int ret = y;
//        if(y < 0) {
//            ret = height + y - objHeight;
//        }
//
//        if(ret < 0) ret = 0;
//        if(ret > height - objHeight) ret = height - objHeight;
//
//        return ret;
//    }
//
//    protected Vector2f getPosition(int overlayWidth, int overlayHeight) {
//        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
//
//        int x = getAbsX(scaledResolution, overlayWidth);
//        int y = getAbsY(scaledResolution, overlayHeight);
//
//        return new Vector2f(x, y);
//    }
//
//
//    protected Vector2f getSize(List<String> strings) {
//        int overlayHeight = 0;
//        int overlayWidth = 0;
//        for(String s : strings) {
//            if(s == null) {
//                overlayHeight += 3;
//                continue;
//            }
//            for(String s2 : s.split("\n")) {
//                int sWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2);
//                if(sWidth > overlayWidth) {
//                    overlayWidth = sWidth;
//                }
//                overlayHeight += 10;
//            }
//        }
//        overlayHeight -= 2;
//
//        int paddingX = 0;
//        int paddingY = 0;
//
//            paddingX = PADDING_X;
//            paddingY = PADDING_Y;
//
//        return new Vector2f(overlayWidth+paddingX*2, overlayHeight+paddingY*2);
//    }
//
//    protected void renderLine(String line, Vector2f position, boolean dummy) {}
//
//    protected Vector2f getTextOffset() {
//        return new Vector2f();
//    }
//
//    private void render(List<String> strings, boolean dummy) {
//        if(strings == null) return;
//
//        Vector2f size = getSize(strings);
//        overlayHeight = (int)size.y;
//        overlayWidth = (int)size.x;
//
//        Vector2f position = getPosition(overlayWidth, overlayHeight);
//        int x = (int)position.x;
//        int y = (int)position.y;
//
//
//
//        Gui.drawRect(x, y, x+overlayWidth, y+overlayHeight, 0x80000000);
//
//        GlStateManager.enableBlend();
//        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
//
//        int paddingX = 0;
//        int paddingY = 0;
//
//            paddingX = PADDING_X;
//            paddingY = PADDING_Y;
//
//
//        Vector2f textOffset = getTextOffset();
//        paddingX += (int) textOffset.x;
//        paddingY += (int) textOffset.y;
//
//        int yOff = 0;
//        for(String s : strings) {
//            if(s == null) {
//                yOff += 3;
//            } else {
//                for(String s2 : s.split("\n")) {
//                    Vector2f pos = new Vector2f(x+paddingX, y+paddingY+yOff);
//                    renderLine(s2, pos, dummy);
//
//                    int xPad = (int)pos.x;
//                    int yPad = (int)pos.y;
//
//                        for(int xO=-2; xO<=2; xO++) {
//                            for(int yO=-2; yO<=2; yO++) {
//                                if(Math.abs(xO) != Math.abs(yO)) {
//                                    Minecraft.getMinecraft().fontRendererObj.drawString(s2,
//                                            xPad+xO/2f, yPad+yO/2f,
//                                            new Color(0, 0, 0, 200/Math.max(Math.abs(xO), Math.abs(yO))).getRGB(), false);
//                                }
//                            }
//                        }
//
//                    Minecraft.getMinecraft().fontRendererObj.drawString(s2,
//                            xPad, yPad, 0xffffff, true);
//
//                    yOff += 10;
//                }
//            }
//        }
//    }


}

/**
 * package io.github.moulberry.notenoughupdates.overlays;
 * <p>
 * import io.github.moulberry.notenoughupdates.NotEnoughUpdates;
 * import io.github.moulberry.notenoughupdates.core.config.Position;
 * import io.github.moulberry.notenoughupdates.core.util.lerp.LerpUtils;
 * import io.github.moulberry.notenoughupdates.util.Utils;
 * import io.github.moulberry.notenoughupdates.util.XPInformation;
 * import net.minecraft.client.Minecraft;
 * import net.minecraft.item.ItemStack;
 * import net.minecraft.nbt.NBTTagCompound;
 * import net.minecraft.util.EnumChatFormatting;
 * import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 * <p>
 * import java.text.NumberFormat;
 * import java.util.ArrayList;
 * import java.util.HashMap;
 * import java.util.LinkedList;
 * import java.util.List;
 * import java.util.function.Supplier;
 * <p>
 * public class FarmingOverlay extends TextOverlay {
 * <p>
 * private long lastUpdate = -1;
 * private int counterLast = -1;
 * private int counter = -1;
 * private float cropsPerSecondLast = 0;
 * private float cropsPerSecond = 0;
 * private LinkedList<Integer> counterQueue = new LinkedList<>();
 * <p>
 * private XPInformation.SkillInfo skillInfo = null;
 * private XPInformation.SkillInfo skillInfoLast = null;
 * <p>
 * private float lastTotalXp = -1;
 * private boolean isFarming = false;
 * private LinkedList<Float> xpGainQueue = new LinkedList<>();
 * private float xpGainHourLast = -1;
 * private float xpGainHour = -1;
 * <p>
 * private int xpGainTimer = 0;
 * <p>
 * private String skillType = "Farming";
 * <p>
 * public FarmingOverlay(Position position, Supplier<List<String>> dummyStrings, Supplier<TextOverlayStyle> styleSupplier) {
 * super(position, dummyStrings, styleSupplier);
 * }
 * <p>
 * private float interp(float now, float last) {
 * float interp = now;
 * if(last >= 0 && last != now) {
 * float factor = (System.currentTimeMillis()-lastUpdate)/1000f;
 * factor = LerpUtils.clampZeroOne(factor);
 * interp = last + (now - last) * factor;
 * }
 * return interp;
 * }
 *
 * @Override public void update() {
 * <p>
 * lastUpdate = System.currentTimeMillis();
 * counterLast = counter;
 * xpGainHourLast = xpGainHour;
 * counter = -1;
 * <p>
 * if(Minecraft.getMinecraft().thePlayer == null) return;
 * <p>
 * ItemStack stack = Minecraft.getMinecraft().thePlayer.getHeldItem();
 * if(stack != null && stack.hasTagCompound()) {
 * NBTTagCompound tag = stack.getTagCompound();
 * <p>
 * if(tag.hasKey("ExtraAttributes", 10)) {
 * NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");
 * <p>
 * if(ea.hasKey("mined_crops", 99)) {
 * counter = ea.getInteger("mined_crops");
 * counterQueue.add(0, counter);
 * } else if(ea.hasKey("farmed_cultivating", 99)) {
 * counter = ea.getInteger("farmed_cultivating");
 * counterQueue.add(0, counter);
 * }
 * }
 * }
 * <p>
 * <p>
 * while(counterQueue.size() >= 4) {
 * counterQueue.removeLast();
 * }
 * <p>
 * if(counterQueue.isEmpty()) {
 * cropsPerSecond = -1;
 * cropsPerSecondLast = 0;
 * } else {
 * cropsPerSecondLast = cropsPerSecond;
 * int last = counterQueue.getLast();
 * int first = counterQueue.getFirst();
 * <p>
 * cropsPerSecond = (first - last)/3f;
 * }
 * <p>
 * if(counter != -1) {
 * overlayStrings = new ArrayList<>();
 * } else {
 * overlayStrings = null;
 * }
 * <p>
 * }
 * @Override public void updateFrequent() {
 * super.updateFrequent();
 * <p>
 * if(counter < 0) {
 * overlayStrings = null;
 * } else {
 * HashMap<Integer, String> lineMap = new HashMap<>();
 * <p>
 * overlayStrings = new ArrayList<>();
 * <p>
 * NumberFormat format = NumberFormat.getIntegerInstance();
 * <p>
 * if(counter >= 0) {
 * int counterInterp = (int)interp(counter, counterLast);
 * <p>
 * lineMap.put(0, EnumChatFormatting.AQUA+"Counter: "+EnumChatFormatting.YELLOW+format.format(counterInterp));
 * }
 * <p>
 * if(counter >= 0) {
 * if(cropsPerSecondLast == cropsPerSecond && cropsPerSecond <= 0) {
 * lineMap.put(1, EnumChatFormatting.AQUA+"Crops/m: "+EnumChatFormatting.YELLOW+"N/A");
 * } else {
 * float cpsInterp = interp(cropsPerSecond, cropsPerSecondLast);
 * <p>
 * lineMap.put(1, EnumChatFormatting.AQUA+"Crops/m: "+EnumChatFormatting.YELLOW+
 * String.format("%.2f", cpsInterp*60));
 * }
 * }
 * <p>
 * float xpInterp = xpGainHour;
 * if(xpGainHourLast == xpGainHour && xpGainHour <= 0) {
 * lineMap.put(5, EnumChatFormatting.AQUA+"XP/h: "+EnumChatFormatting.YELLOW+"N/A");
 * } else {
 * xpInterp = interp(xpGainHour, xpGainHourLast);
 * <p>
 * lineMap.put(5, EnumChatFormatting.AQUA+"XP/h: "+EnumChatFormatting.YELLOW+
 * format.format(xpInterp)+(isFarming ? "" : EnumChatFormatting.RED + " (PAUSED)"));
 * }
 * <p>
 * if(skillInfo != null) {
 * StringBuilder levelStr = new StringBuilder(EnumChatFormatting.AQUA + skillType.substring(0, 4) + ": ");
 * <p>
 * levelStr.append(EnumChatFormatting.YELLOW)
 * .append(skillInfo.level)
 * .append(EnumChatFormatting.GRAY)
 * .append(" [");
 * <p>
 * float progress = skillInfo.currentXp / skillInfo.currentXpMax;
 * if(skillInfoLast != null && skillInfo.currentXpMax == skillInfoLast.currentXpMax) {
 * progress = interp(progress, skillInfoLast.currentXp / skillInfoLast.currentXpMax);
 * }
 * <p>
 * float lines = 25;
 * for(int i=0; i<lines; i++) {
 * if(i/lines < progress) {
 * levelStr.append(EnumChatFormatting.YELLOW);
 * } else {
 * levelStr.append(EnumChatFormatting.DARK_GRAY);
 * }
 * levelStr.append('|');
 * }
 * <p>
 * levelStr.append(EnumChatFormatting.GRAY)
 * .append("] ")
 * .append(EnumChatFormatting.YELLOW)
 * .append((int)(progress*100))
 * .append("%");
 * <p>
 * int current = (int)skillInfo.currentXp;
 * if(skillInfoLast != null && skillInfo.currentXpMax == skillInfoLast.currentXpMax) {
 * current = (int)interp(current, skillInfoLast.currentXp);
 * }
 * <p>
 * int remaining = (int)(skillInfo.currentXpMax - skillInfo.currentXp);
 * if(skillInfoLast != null && skillInfo.currentXpMax == skillInfoLast.currentXpMax) {
 * remaining = (int)interp(remaining, (int)(skillInfoLast.currentXpMax - skillInfoLast.currentXp));
 * }
 * <p>
 * lineMap.put(2, levelStr.toString());
 * lineMap.put(3, EnumChatFormatting.AQUA+"Current XP: " + EnumChatFormatting.YELLOW+ format.format(current));
 * if(remaining < 0) {
 * lineMap.put(4, EnumChatFormatting.AQUA+"Remaining XP: " + EnumChatFormatting.YELLOW+ "MAXED!");
 * lineMap.put(7, EnumChatFormatting.AQUA+"ETA: "+EnumChatFormatting.YELLOW+ "MAXED!");
 * } else {
 * lineMap.put(4, EnumChatFormatting.AQUA+"Remaining XP: " + EnumChatFormatting.YELLOW+ format.format(remaining));
 * if(xpGainHour < 1000) {
 * lineMap.put(7, EnumChatFormatting.AQUA+"ETA: "+EnumChatFormatting.YELLOW+ "N/A");
 * } else {
 * lineMap.put(7, EnumChatFormatting.AQUA+"ETA: "+EnumChatFormatting.YELLOW+ Utils.prettyTime((long)(remaining)*1000*60*60/(long)xpInterp));
 * }
 * }
 * <p>
 * }
 * <p>
 * float yaw = Minecraft.getMinecraft().thePlayer.rotationYawHead;
 * yaw %= 360;
 * if(yaw < 0) yaw += 360;
 * if(yaw > 180) yaw -= 360;
 * <p>
 * lineMap.put(6, EnumChatFormatting.AQUA+"Yaw: "+EnumChatFormatting.YELLOW+
 * String.format("%.2f", yaw)+EnumChatFormatting.BOLD+"\u1D52");
 * <p>
 * for(int strIndex : NotEnoughUpdates.INSTANCE.config.skillOverlays.farmingText) {
 * if(lineMap.get(strIndex) != null) {
 * overlayStrings.add(lineMap.get(strIndex));
 * }
 * }
 * if(overlayStrings != null && overlayStrings.isEmpty()) overlayStrings = null;
 * }
 * }
 * <p>
 * <p>
 * }
 */
