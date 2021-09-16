package io.github.kingstefan26.kokomod.module.macro.macroUtil.lastLeftOff;

import io.github.kingstefan26.kokomod.util.sendChatMessage;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import static io.github.kingstefan26.kokomod.module.macro.macroUtil.lastLeftOff.lastLeftOff.LastLeftOff;

public class lastLeftOffStatusCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "lastleftoffdebug";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    public static String usage(ICommandSender arg0) {
        return new lastLeftOffStatusCommand().getCommandUsage(arg0);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        sendChatMessage.sendClientDebugMessage("hello");
    }
}
