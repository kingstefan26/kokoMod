/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.macro;

import lombok.Getter;
import lombok.Setter;

public class stateMenager {
    @Getter @Setter
    private macroStates state;

    public boolean checkState(macroStates input){
        return state == input;
    }
}
