package io.github.kingstefan26.kokomod.core.module;

import io.github.kingstefan26.kokomod.util.sendChatMessage;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class unloadAllmodulesCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "unloadAllmodules";
    }

    public static String usage(ICommandSender arg0) {
        return new unloadAllmodulesCommand().getCommandUsage(arg0);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        int c = moduleRegistery.getModuleRegistery().loadedModules.size();
        moduleRegistery.getModuleRegistery().unloadAllModules();
        sendChatMessage.sendClientDebugMessage("unloaded " + c +" modules");
    }
}
