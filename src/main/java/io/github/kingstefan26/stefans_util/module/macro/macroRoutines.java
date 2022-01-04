/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.macro;

public interface macroRoutines {

//  on if the module is enabled
    void continuousChecks(); // every 20 ticks
    void continuousRender(Float partialTick); // render ticks

//    PAUSED, // event based aka dont stop untill user input
    void togglePause();
    void macroPauseRoutine(); // single time
    void macroPauseRenderRoutine(); // render tick
    void doContinuousPauseChecks(int key); // input event

//    MACROING, // event based aka dont stop untill user input
    void preMacroingRoutine(); //single time
    void doMacroRenderRoutine(); // render tick
    void doMacroRoutine();// client tick
    void doContinuousMacroChecks(); // render tick

//    RECALIBRATING, // event based aka dont stop untill user input
    void checkMacroConditionsRenderRoutine(float partialTicks); // render tick
    void checkMacroConditionsRoutine(); // client tick
    void recalibratingDisableKeyLisiner(int key);

//    AUTONOMOUS_RECALIBRATING,
    void recalibrateMacroRoutine(); // client tick
    void recalibrateMacroRenderRoutine(); // client tick


//    STARTING,
    void enableMacroRoutine(); // single time
    void startingFromLastLeftOffRoutine();

//    STOPPING
    void disableMacroRoutine();

}
