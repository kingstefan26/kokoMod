/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.module.macro.wart.helper.wartMacroHelpers;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Tuple;



public class blockThings extends basicModule {
    public blockThings() {
        super("test of block thigns", "test", moduleManager.Category.DEBUG);
    }

    @Override
    public void onEnable() {
        Tuple<BlockPos, String>[][][] blocks = wartMacroHelpers.checkBlocksAroundPlayer();
        for(Tuple<BlockPos, String>[][] arr1 : blocks){
            for(Tuple<BlockPos, String>[] arr : arr1){
                for(Tuple<BlockPos, String> block: arr){
                    if(block.getSecond() != null){
                        chatService.queueClientChatMessage(block.getSecond());
                    }
                }
            }
        }

        super.onEnable();
    }
}
