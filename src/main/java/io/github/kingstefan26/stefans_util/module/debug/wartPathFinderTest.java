/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.prototypeModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;

import static io.github.kingstefan26.stefans_util.module.macro.wart.helper.wartMacroUtil.whichWayToGoMockUp;

public class wartPathFinderTest extends prototypeModule {

    public wartPathFinderTest() {
        super("wartPathFinderTest");
    }

    @Override
    protected void PROTOTYPETEST() {


        chatService.queueCleanChatMessage(String.valueOf(whichWayToGoMockUp(null)));
//        for(keyControlService.action.walk val : keyControlService.action.walk.values()){
//            chatService.queueCleanChatMessage("testing " + val);
//            chatService.queueCleanChatMessage(String.valueOf(whichWayToGoMockUp(val)));
//        }
    }
}
