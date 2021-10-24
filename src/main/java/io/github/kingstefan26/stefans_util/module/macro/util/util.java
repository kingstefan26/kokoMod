package io.github.kingstefan26.stefans_util.module.macro.util;

import io.github.kingstefan26.stefans_util.module.macro.wart.UniversalWartMacro;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class util {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static Vec3 getPlayerFeetVec(){
        return new Vec3((int)mc.thePlayer.posX,(int)mc.thePlayer.posY,(int)mc.thePlayer.posZ);
    }
    public static Vec3 unRelitaviseCords(Vec3 relativeCords, Vec3 playerPos){
        return new Vec3(
                playerPos.xCoord + relativeCords.xCoord,
                playerPos.yCoord + relativeCords.yCoord,
                playerPos.zCoord + relativeCords.zCoord);
    }
    public static class checkBlock {
        public int x, y, z;
        public String name;
        public checkBlock(int x,int y,int z, String blockName){
            this.name = blockName;
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public Vec3 getCords(){
            return new Vec3(x,y,z);
        }
    }


    public class checkBlockRegisrty{
        List<checkBlock> mainArray = new ArrayList<>();
        public void putBlock(checkBlock c){
            mainArray.add(c);
        }
        public checkBlock getblockinCords(Vec3 cords){
            final checkBlock[] match = {null};
            mainArray.forEach(block -> {
                if(block.x == cords.xCoord && block.y == cords.yCoord && block.z == cords.zCoord){
                    match[0] = block;
                }
            });
            return match[0];
        }
        public void clear(){
            mainArray.clear();
        }
        public void recheckblockinCords(Vec3 cords){
            ListIterator<checkBlock> iter = mainArray.listIterator();
            while(iter.hasNext()){
                checkBlock c = iter.next();
                if(c.getCords().equals(cords)){
                    mainArray.add(retriveCheckBlockInCords(c.getCords()));
                    iter.remove();
                }
            }
        }
    }
    public static checkBlock retriveCheckBlockInCords(Vec3 cords){
        WorldClient world = Minecraft.getMinecraft().theWorld;
        Block block = world.getBlockState(new BlockPos(cords.xCoord, cords.yCoord, cords.zCoord)).getBlock();
        if(block == null) return null;
        return new checkBlock((int)cords.xCoord,(int)cords.yCoord,(int)cords.zCoord, block.getUnlocalizedName());
    }

    public static class macroMenu extends GuiScreen {
        private final UniversalWartMacro parent;

        public macroMenu(UniversalWartMacro parent){
            this.parent = parent;
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.drawDefaultBackground();
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        public void initGui() {
            this.allowUserInput = true;
        }

        @Override
        public void onGuiClosed() {
            super.onGuiClosed();
            parent.disableFromGui();
        }

        @Override
        public boolean doesGuiPauseGame() {
            return false;
        }

    }

    public enum walkStates {
        LEFT, RIGHT, TOP, BOTTOM, HOLD, DEFAULT, LEFTTOP, RIGHTTOP, RIGHTBOTTOM, LEFTBOTTOM
    }
}