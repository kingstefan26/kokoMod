package io.github.kingstefan26.stefans_util.mixins;

import net.minecraft.client.ClientBrandRetriever;
import net.minecraftforge.fml.common.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ClientBrandRetriever.class)
public class MixinClientBrandRetriver {
    @Inject(method = "getClientModName()Ljava/lang/String;", at = @At("RETURN"))
    private static void getClientModName(CallbackInfoReturnable<String> cir) {
        System.out.println("HELLLO IS THIS WORKING!!!!!!!!!");
        cir.setReturnValue("vanilla");
    }
}
