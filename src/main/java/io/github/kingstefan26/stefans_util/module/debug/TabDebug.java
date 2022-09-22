/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleframes.prototypeModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.TabListUtil;
import net.minecraft.client.network.NetworkPlayerInfo;

public class TabDebug extends prototypeModule {
    public TabDebug() {
        super("tabDebug");
    }

    @Override
    protected void PROTOTYPETEST() {
        StringBuilder a = new StringBuilder("null");
        for (NetworkPlayerInfo fetchTabEntire : TabListUtil.fetchTabEntires()) {
            if (!fetchTabEntire.getDisplayName().getUnformattedText().equals("")) {
                a.append(fetchTabEntire.getDisplayName().getUnformattedText());
            }
        }
        String c = a.toString().replaceAll("[^\\x00-\\x7F]", "").replace("\n", "").replace("\r", "").replace(" ", "");
        chatService.queueClientChatMessage(c);
        logger.info(c);
    }
}
