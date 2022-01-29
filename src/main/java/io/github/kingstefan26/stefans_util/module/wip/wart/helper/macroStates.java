/*
 * Copyright (c) 2021-2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.wip.wart.helper;

// state machine who
public enum macroStates {
    // cold start scenario:
    // IDLE -> STARTING -> RECALIBRATING -> MACROING -> STOPPING -> IDLE

    // pauses and unpauses scenario:
    // IDLE -> STARTING -> RECALIBRATING -> MACROING -> STOPPED -> AUTONOMUS_RECALIBRATING -> MACROING -> STOPPING -> IDLE

    IDLE, // event based aka dont stop untill user input
    PAUSED, // event based aka dont stop untill user input
    MACROING, // event based aka dont stop untill user input
    RECALIBRATING, // event based aka dont stop untill user input
    AUTONOMOUS_RECALIBRATING,
    STARTING,
    STOPPING
}
