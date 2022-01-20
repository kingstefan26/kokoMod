/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.macro.wart.helper;

import io.github.kingstefan26.stefans_util.module.macro.wart.UniversalWartMacro;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.impl.keyControlService;
import lombok.Getter;
import lombok.Setter;

import static io.github.kingstefan26.stefans_util.service.impl.keyControlService.action.walk.right;

public class wartState {
    UniversalWartMacro parent;
    @Getter
    @Setter
    boolean sprintStatus, lastLeftOffStaus, stolenFarmOverlayStatus, autorecconectStatus, macroSessionTrackerStatus;
    @Getter
    @Setter
    boolean dontSpamFlag = true;
    @Getter
    @Setter
    long ExpireTimeSpamp = 0;
    @Getter
    @Setter
    boolean autonomous_recalibration_moveheadtowartflag;
    @Getter
    @Setter
    keyControlService.action.walk currentWalkAction = right;
    @Getter
    private macroStates state;

    long throddle = 0;

    public void setState(macroStates state) {
        if (this.state != state && System.currentTimeMillis() >= throddle) {
            throddle = System.currentTimeMillis() + 250;
            this.state = state;
            chatService.queueCleanChatMessage("changed state to " + state);
        }
    }

    public wartState(UniversalWartMacro parent) {
        this.parent = parent;
    }

    public boolean checkState(macroStates input) {
        return state == input;
    }
}
