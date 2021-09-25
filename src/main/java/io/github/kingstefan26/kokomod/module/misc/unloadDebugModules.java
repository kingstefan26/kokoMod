package io.github.kingstefan26.kokomod.module.misc;

import io.github.kingstefan26.kokomod.module.moduleIndex;
import io.github.kingstefan26.kokomod.util.sendChatMessage;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class unloadDebugModules extends CommandBase {

    public static String usage(ICommandSender arg0) {
        return new unloadDebugModules().getCommandUsage(arg0);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "unloadDebugModules";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        int c = moduleIndex.getmoduleIndex().getDebugModules().size();
        moduleIndex.getmoduleIndex().unloadDebugModules();
        sendChatMessage.sendClientDebugMessage("unloaded " + c +" modules");
    }
}
