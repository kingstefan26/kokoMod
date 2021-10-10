package io.github.kingstefan26.stefans_util.mixins;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Loader.class)
public class Mixinloader {
    //Lnet/minecraftforge/fml/common/Loader;getActiveModList()Ljava/util/List;
    //Lnet/minecraftforge/fml/common/Loader;getActiveModList()Ljava/util/List;
    //loadMods()V
    //getActiveModList()Ljava/util/List<net.minecraftforge.fml.common.ModContainer>

    @Inject(method = "getActiveModList()Ljava/util/List;", at = @At("HEAD"), remap=false)
    private void getActiveModList(CallbackInfoReturnable<List<ModContainer>> cir) {
        System.out.println("HELLLO IS THIS WORKING!!!!!!!!!");
        cir.getReturnValue().forEach(mod -> {
            System.out.println("GET THIS COCK SUCKED: " + mod.getModId());
        });

    }
//
//    //Lnet/minecraftforge/fml/common/Loader;loadMods()V
//    @Inject(method = "loadMods()V", at = @At("HEAD"))
//    private void MixThatShitIn(CallbackInfo ci){
//
//    }
}
