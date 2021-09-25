package io.github.kingstefan26.kokomod.core.commands;

import io.github.kingstefan26.kokomod.core.module.loadAllmodulesCommand;
import io.github.kingstefan26.kokomod.core.module.unloadAllmodulesCommand;
import io.github.kingstefan26.kokomod.module.macro.macroUtil.lastLeftOff.lastLeftOffStatusCommand;
import io.github.kingstefan26.kokomod.module.misc.unloadDebugModules;
import net.minecraft.command.CommandBase;

import java.util.ArrayList;


public class commandIndex {
    public static commandIndex commandIndex;
    public static commandIndex getCommandIndex(){
        if(commandIndex == null) commandIndex = new commandIndex();
        return commandIndex;
    }

    ArrayList<CommandBase> commands;
    public commandIndex() {
        commands = new ArrayList<>();

        commands.add(new lastLeftOffStatusCommand());
        commands.add(new unloadDebugModules());
        commands.add(new unloadAllmodulesCommand());
        commands.add(new loadAllmodulesCommand());
    }

    public ArrayList<CommandBase>getCommands() {
        return this.commands;
    }

}
