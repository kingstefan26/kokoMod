/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import com.google.common.base.Throwables;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.prototypeModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;

import java.io.IOException;

public class ApiDebug extends prototypeModule {
    public ApiDebug() {
        super("ApiDebug");
    }

    @Override
    protected void PROTOTYPETEST() {
        try {
            chatService.queueCleanChatMessage("Success: " + APIHandler.tokentouuid(mc.getSession().getToken()));
        } catch (IOException e) {
            chatService.queueCleanChatMessage("Fail: " + Throwables.getRootCause(e).getMessage());
            e.printStackTrace();
        }
    }
}
