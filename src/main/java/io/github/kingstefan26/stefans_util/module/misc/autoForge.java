package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;


public class autoForge extends basicModule {
    public WorldClient currentWorld = null;
    public Chunk currentChunk = null;
    boolean oldAutoJumpValue;
    public static int Radius = 10;
    int BlockToChoose = 0;
    public static final double RAD_TO_DEG = (180D / Math.PI);
    ArrayList<BlockPos> cutBlocks = new ArrayList<BlockPos>();

    public autoForge() {
        super("AutoForager", "Auto Forager Bot", moduleManager.Category.MISC);
    }

    @Override
    public void onEnable()
    {
        chatService.queueClientChatMessage("enabled AutoForager lmao", chatService.chatEnum.CHATNOPREFIX);
        this.CheckBlocks(Radius);
//        this.oldAutoJumpValue = this.mc.gameSettings.autoJump;
//        this.mc.gameSettings.autoJump = true;
        super.onEnable();
    }

    public float[] getPlayerRotations()
    {
        return new float[] {mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
    }

    public void setPlayerRotations(float yaw, float pitch)
    {
        mc.thePlayer.rotationYaw = lerpAngle(mc.thePlayer.rotationYaw, yaw, 0.01F);
        mc.thePlayer.rotationPitch = lerpAngle(mc.thePlayer.rotationPitch, pitch, 0.01F);
    }

    public static float lerpAngle(float fromRadians, float toRadians, float progress)
    {
        float f = ((toRadians - fromRadians) % 360.0F + 540.0F) % 360.0F - 180.0F;
        return fromRadians + f * progress % 360.0F;
    }





    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        try
        {
            if (this.cutBlocks.isEmpty())
            {
                return;
            }

            if (this.BlockToChoose > this.cutBlocks.size() - 1)
            {
                this.BlockToChoose = 0;
            }

            float[] afloat = this.getRotation(this.getPlayerPos(), this.cutBlocks.get(this.BlockToChoose));

            if (mc.theWorld.getBlockState(((BlockPos)this.cutBlocks.get(this.BlockToChoose)).up()).getBlock().getMaterial() == Material.air)
            {
                ++this.BlockToChoose;
            }

            this.setPlayerRotations(afloat[0], afloat[1]);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
//        this.mc.gameSettings.autoJump = this.oldAutoJumpValue;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
    }

    private float[] getRotations(Vec3 orig, Vec3 dest) {
        double[] adouble = new double[] {orig.xCoord - dest.xCoord, orig.yCoord - dest.yCoord, orig.zCoord - dest.zCoord};
        double d0 = MathHelper.atan2(adouble[0], -adouble[2]);
        double d1 = Math.sqrt(adouble[0] * adouble[0] + adouble[2] * adouble[2]);
        double d2 = MathHelper.atan2(adouble[1], d1);
        return new float[] {(float)(d0 * (180D / Math.PI)), (float)(d2 * (180D / Math.PI))};
    }

    public float[] getRotation(BlockPos orig, BlockPos dest) {
        return this.getRotations(new Vec3(orig), new Vec3(dest));
    }

    public void CheckBlocks(int radius)
    {
        this.cutBlocks = new ArrayList<BlockPos>();
        int i = (int)Math.round(mc.thePlayer.posX) + radius;
        int j = (int)Math.round(mc.thePlayer.posX) - radius;
        int k = (int)Math.round(mc.thePlayer.posZ) + radius;
        int l = (int)Math.round(mc.thePlayer.posZ) - radius;
        int i1 = 0;

        for (int j1 = i; j1 > j; --j1)
        {
            System.out.println("Doing X coordinate " + j1);

            for (int k1 = k; k1 > l; --k1)
            {
                Block block = mc.theWorld.getBlockState(this.getSearchPos(j1, k1)).getBlock();

                if (block instanceof BlockLog)
                {
                    this.cutBlocks.add(this.getSearchPos(j1, k1));
                    chatService.queueClientChatMessage(String.format("Block in %s , %s is a wood block", j1, k1), chatService.chatEnum.CHATNOPREFIX);
                }
                else
                {
                    System.out.printf("Block in %s , %s is not a wood block%n", j1, k1);
                }

                ++i1;
            }
        }

        for (BlockPos blockpos : this.cutBlocks)
        {
            chatService.queueClientChatMessage(blockpos.getX() + " , " + blockpos.getZ(), chatService.chatEnum.CHATNOPREFIX);
        }
    }

    public BlockPos getSearchPos(int x, int z)
    {
        return new BlockPos((double)x, mc.thePlayer.posY, (double)z);
    }

    public BlockPos getPlayerPos()
    {
        return new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
    }
}
