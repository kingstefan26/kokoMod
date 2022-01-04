package io.github.kingstefan26.stefans_util.util.stolenBs.freeCam;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class freeCamModuleMinecraft {
    public static int timeout = 0;


    public class FakeClientPlayer extends EntityLivingBase {

        public FakeClientPlayer(World world) {
            super(world);//, "fakeClientPlayer");
        }

        @Override
        public ItemStack getHeldItem() {
            return null;
        }

        @Override
        public ItemStack getEquipmentInSlot(int slotIn) {
            return null;
        }

        @Override
        public ItemStack getCurrentArmor(int slotIn) {
            return null;
        }

        @Override
        public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {

        }

        @Override
        public ItemStack[] getInventory() {
            return new ItemStack[0];
        }

    }


    FakeClientPlayer fakePlayer = null;
    static int x, y, z;
    static int prevx, prevy, prevz;
    float prevyaw, prevpitch;

    private final KeyBinding reset = new KeyBinding("Reset", Keyboard.KEY_O, "FreeCam");
    private final KeyBinding yt = new KeyBinding("Up", Keyboard.KEY_SPACE, "FreeCam");
    private final KeyBinding yt2 = new KeyBinding("Down", Keyboard.KEY_RSHIFT, "FreeCam");
    private final KeyBinding zt = new KeyBinding("Right", Keyboard.KEY_RIGHT, "FreeCam");
    private final KeyBinding zt2 = new KeyBinding("Left", Keyboard.KEY_LEFT, "FreeCam");
    private final KeyBinding xt = new KeyBinding("Forward", Keyboard.KEY_UP, "FreeCam");
    private final KeyBinding xt2 = new KeyBinding("Backward", Keyboard.KEY_DOWN, "FreeCam");

    public freeCamModuleMinecraft() {
//        super("freecam", "frees ur cam!", ModuleManager.Category.RENDER, true);
        ClientRegistry.registerKeyBinding(yt);
        ClientRegistry.registerKeyBinding(yt2);
        ClientRegistry.registerKeyBinding(zt);
        ClientRegistry.registerKeyBinding(zt2);
        ClientRegistry.registerKeyBinding(xt);
        ClientRegistry.registerKeyBinding(xt2);
        ClientRegistry.registerKeyBinding(reset);
    }

    @SubscribeEvent
    public void ClientTick(TickEvent.ClientTickEvent e) {
        if (timeout > 0) {
            timeout--;
        }
    }

    @SubscribeEvent
    public void KeyInputEvent(InputEvent.KeyInputEvent event) {
        if (timeout == 0 && GameSettings.isKeyDown(reset)) {
            timeout = 10;
            x = (int) Minecraft.getMinecraft().thePlayer.posX;
            y = (int) Minecraft.getMinecraft().thePlayer.posY;
            z = (int) Minecraft.getMinecraft().thePlayer.posZ;
        }
        if (timeout == 0 && GameSettings.isKeyDown(yt)) {
            timeout = 2;
            y++;
        }
        if (timeout == 0 && GameSettings.isKeyDown(yt2)) {
            timeout = 2;
            y--;
        }
        if (timeout == 0 && GameSettings.isKeyDown(zt)) {
            timeout = 2;
            z++;
        }
        if (timeout == 0 && GameSettings.isKeyDown(zt2)) {
            timeout = 2;
            z--;
        }
        if (timeout == 0 && GameSettings.isKeyDown(xt)) {
            timeout = 2;
            x++;
        }
        if (timeout == 0 && GameSettings.isKeyDown(xt2)) {
            timeout = 2;
            x--;
        }
    }

//    @SubscribeEvent
//    public void onPreRenderGame(RenderGameOverlayEvent.Pre event) {
//
//        if (fakePlayer == null) {
//            fakePlayer = new FakeClientPlayer(Minecraft.getMinecraft().thePlayer.getEntityWorld());
//        }
//
//        // set position and angles; note that posY is not altered but camera still correct:
//        fakePlayer.setLocationAndAngles(x, y, z, Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.rotationPitch);
//
//        // set previous values to prevent camera from freaking out:
//        fakePlayer.prevRotationPitch = prevpitch;
//        fakePlayer.prevRotationYaw = prevyaw;
//        fakePlayer.rotationYawHead = Minecraft.getMinecraft().thePlayer.rotationYawHead;
//        fakePlayer.prevPosX = prevx;
//        fakePlayer.prevPosY = prevy;
//        fakePlayer.prevPosZ = prevz;
//        mc.setRenderViewEntity(fakePlayer);
//
//        prevx = x;
//        prevy = y;
//        prevz = z;
//        prevpitch = mc.thePlayer.rotationPitch;
//        prevyaw = mc.thePlayer.rotationYaw;
//
//    }
//
//    @Override
//    public void onEnable() {
//        super.onEnable();
//
//
//        x = (int) Minecraft.getMinecraft().thePlayer.posX;
//        y = (int) Minecraft.getMinecraft().thePlayer.posY;
//        z = (int) Minecraft.getMinecraft().thePlayer.posZ;
//
//
//    }
//
//    @Override
//    public void onDisable() {
//        super.onDisable();
//        mc.setRenderViewEntity(mc.thePlayer);
//    }


}
