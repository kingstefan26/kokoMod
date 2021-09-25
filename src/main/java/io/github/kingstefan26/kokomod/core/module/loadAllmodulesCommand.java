package io.github.kingstefan26.kokomod.core.module;

import io.github.kingstefan26.kokomod.util.sendChatMessage;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class loadAllmodulesCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "loadAllmodules";
    }

    public static String usage(ICommandSender arg0) {
        return new loadAllmodulesCommand().getCommandUsage(arg0);
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
        moduleRegistery.getModuleRegistery().loadModules();
        int c = moduleRegistery.getModuleRegistery().loadedModules.size();
        sendChatMessage.sendClientDebugMessage("unloaded " + c +" modules");
    }
}
