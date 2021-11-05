package io.github.kingstefan26.stefans_util.util.stolenBs.freeCamv2;

import io.github.kingstefan26.stefans_util.core.preRewrite.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.preRewrite.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class freeCamModulev2 extends Module {

    public class FreecamEntity extends EntityOtherPlayerMP {
        public FreecamEntity() {
            super(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getGameProfile());
            copyLocationAndAnglesFrom(Minecraft.getMinecraft().thePlayer);

            clonePlayer(Minecraft.getMinecraft().thePlayer, true);
            rotationYawHead = Minecraft.getMinecraft().thePlayer.rotationYawHead;
            renderYawOffset = Minecraft.getMinecraft().thePlayer.renderYawOffset;

            chasingPosX = posX;
            chasingPosY = posY;
            chasingPosZ = posZ;

            Minecraft.getMinecraft().theWorld.addEntityToWorld(getEntityId(), this);
        }

        public void resetPlayerPosition() {
            Minecraft.getMinecraft().thePlayer.setPositionAndRotation(posX, posY, posZ,
                    rotationYaw, rotationPitch);
        }

        public void despawn() {
            Minecraft.getMinecraft().theWorld.removeEntityFromWorld(getEntityId());
        }
    }

    public freeCamModulev2() {
        super("freecamv2", "frees ur cam!", ModuleManager.Category.RENDER, true);
    }

    private FreecamEntity freecamEnt;

    @Override
    public void onEnable() {
        freecamEnt = new FreecamEntity();
    }

    @Override
    public void onDisable() {
        freecamEnt.resetPlayerPosition();
        freecamEnt.despawn();
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }

}
