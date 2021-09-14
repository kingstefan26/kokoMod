package io.github.kingstefan26.kokomod.util.stolenBs.freeCam;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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