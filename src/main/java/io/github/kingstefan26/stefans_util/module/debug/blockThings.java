/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.module.macro.wart.helper.wartMacroHelpers;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;


public class blockThings extends basicModule {
    public blockThings() {
        super("test of block thigns", "test", moduleManager.Category.DEBUG);
    }

    @Override
    public void onEnable() {



//                 relative cords BlockPos{x=-1, y=-1, z=-1} block: minecraft:air
//                 relative cords BlockPos{x=-1, y=-1, z=0} block: minecraft:air
//                 relative cords BlockPos{x=-1, y=-1, z=1} block: minecraft:air
//                 relative cords BlockPos{x=-1, y=0, z=-1} block: minecraft:air
//                 relative cords BlockPos{x=-1, y=0, z=0} block: minecraft:air
//                 relative cords BlockPos{x=-1, y=0, z=1} block: minecraft:air
//                 relative cords BlockPos{x=-1, y=1, z=-1} block: minecraft:air
//                 relative cords BlockPos{x=-1, y=1, z=0} block: minecraft:air
//                 relative cords BlockPos{x=-1, y=1, z=1} block: minecraft:air
//                 relative cords BlockPos{x=-1, y=2, z=-1} block: minecraft:air
//                 relative cords BlockPos{x=-1, y=2, z=0} block: minecraft:coal_ore
//                 relative cords BlockPos{x=-1, y=2, z=1} block: minecraft:air
//                 relative cords BlockPos{x=0, y=-1, z=-1} block: minecraft:air
//                 relative cords BlockPos{x=0, y=-1, z=0} block: minecraft:air
//                 relative cords BlockPos{x=0, y=-1, z=1} block: minecraft:air
//                 relative cords BlockPos{x=0, y=0, z=-1} block: minecraft:air
//                 relative cords BlockPos{x=0, y=0, z=0} block: minecraft:air
//                 relative cords BlockPos{x=0, y=0, z=1} block: minecraft:air
//                 relative cords BlockPos{x=0, y=1, z=-1} block: minecraft:air
//                 relative cords BlockPos{x=0, y=1, z=0} block: minecraft:air
//                 relative cords BlockPos{x=0, y=1, z=1} block: minecraft:air
//                 relative cords BlockPos{x=0, y=2, z=-1} block: minecraft:dirt
//                 relative cords BlockPos{x=0, y=2, z=0} block: minecraft:air
//                 relative cords BlockPos{x=0, y=2, z=1} block: minecraft:wool
//                 relative cords BlockPos{x=1, y=-1, z=-1} block: minecraft:air
//                 relative cords BlockPos{x=1, y=-1, z=0} block: minecraft:air
//                 relative cords BlockPos{x=1, y=-1, z=1} block: minecraft:air
//                 relative cords BlockPos{x=1, y=0, z=-1} block: minecraft:air
//                 relative cords BlockPos{x=1, y=0, z=0} block: minecraft:air
//                 relative cords BlockPos{x=1, y=0, z=1} block: minecraft:air
//                 relative cords BlockPos{x=1, y=1, z=-1} block: minecraft:air
//                 relative cords BlockPos{x=1, y=1, z=0} block: minecraft:air
//                 relative cords BlockPos{x=1, y=1, z=1} block: minecraft:air
//                 relative cords BlockPos{x=1, y=2, z=-1} block: minecraft:air
//                 relative cords BlockPos{x=1, y=2, z=0} block: minecraft:bedrock
//                 relative cords BlockPos{x=1, y=2, z=1} block: minecraft:air


//        north = BlockPos{x=0, y=2, z=-1} block: minecraft:dirt
//        east = BlockPos{x=1, y=2, z=0} block: minecraft:bedrock
//        south = BlockPos{x=0, y=2, z=1} block: minecraft:wool
//        west = BlockPos{x=-1, y=2, z=0} block: minecraft:coal_ore

        Tuple<BlockPos, String>[][][] blocks = wartMacroHelpers.checkBlocksAroundPlayer();

        int xShift = -1, yShift = -1, zShift = -1;
        // legend to this magic piece of math, says what direction we are facing
        // 0 = south
        // 1 = west
        // 2 = north
        // 3 = east
        int direction = MathHelper.floor_double((double)(mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 3; k++) {

                    int relativex = i + xShift, relativey = j +yShift, relativez = k +zShift;

                    if(blocks[i][j][k].getSecond().equals("minecraft:nether_wart")){
                        if(relativey == 1){
                            if(relativex == 0 && relativez == -1){
                                if(direction == 2){
                                    chatService.queueCleanChatMessage("horray we are looking at wart");
                                }
                            }
                            if(relativex == 1 && relativez == 0){
                                if(direction == 3){
                                    chatService.queueCleanChatMessage("horray we are looking at wart");
                                }
                            }
                            if(relativex == 0 && relativez == 1){
                                if(direction == 0){
                                    chatService.queueCleanChatMessage("horray we are looking at wart");

                                }
                            }
                            if(relativex == -1 && relativez == 0){
                                if(direction == 1){
                                    chatService.queueCleanChatMessage("horray we are looking at wart");
                                }
                            }
                        }
                    }
                }
            }
        }

        super.onEnable();
    }
}
