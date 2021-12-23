/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.mixins;

import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin({ModList.class})
public class ModlessMixin {
    @Shadow
    private Map<String, String> modTags;

//    @Inject(method = {"<init>(Ljava/util/List;)V"}, at = {@At("RETURN")})
//    public void test(List<ModContainer> modList, CallbackInfo ci) {
//        if (vanillaForgeSpoofer.modless) {
////            try {
////                if (Minecraft.func_71410_x().func_71356_B()) {
////                    return;
////                }
////            } catch (Exception var5) {
////                return;
////            }
//
//            this.modTags.entrySet().removeIf((modx) ->
//                    !modx.getKey().equalsIgnoreCase("fml")
//                    && !modx.getKey().equalsIgnoreCase("forge")
//                    && !modx.getKey().equalsIgnoreCase("mcp")
//            );
//
//
//
//
//
//
////            if (vanillaForgeSpoofer.fakemods > 0) {
////                Iterator var3 = OringoClient.configFile.FakeMods.iterator();
////
////                while(var3.hasNext()) {
////                    String mod = (String)var3.next();
////                    this.modTags.put(mod, mod);
////                }
////            }
//
//        }
//    }



    @Inject(method = "<init>(Ljava/util/List;)V",at = @At("RETURN"))
    public void constructorHead(
            // you will need to put any parameters the constructor accepts here, they will be the actual passed values
            // it also needs to accept a special argument that mixin passes to this injection method
            CallbackInfo ci,
            List<ModContainer> modList
    ) {
        this.modTags.entrySet().removeIf((modx) -> {
            return !((String)modx.getKey()).equalsIgnoreCase("fml")
                    && !((String)modx.getKey()).equalsIgnoreCase("forge") 
                    && !((String)modx.getKey()).equalsIgnoreCase("mcp");
        });
        System.out.println("I AM FUKING RAGIN");
    }


}
