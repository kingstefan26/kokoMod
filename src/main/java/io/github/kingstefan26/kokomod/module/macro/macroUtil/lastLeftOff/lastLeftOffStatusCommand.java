package io.github.kingstefan26.kokomod.module.macro.macroUtil.lastLeftOff;

import io.github.kingstefan26.kokomod.util.CalendarUtils;
import io.github.kingstefan26.kokomod.util.sendChatMessage;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;

import static io.github.kingstefan26.kokomod.module.macro.macroUtil.lastLeftOff.lastLeftOff.LastLeftOff;
import static io.github.kingstefan26.kokomod.module.macro.macroUtil.lastLeftOff.lastLeftOff.getLastLeftOff;

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
        ArrayList<String> messeges = new ArrayList<>();
        if(getLastLeftOff().getLastleftoffObject() == null) {
            messeges.add("error getting last left off object");
            return;
        }else{
            messeges.add("x :" + getLastLeftOff().getLastleftoffObject().x);
            messeges.add("y :" + getLastLeftOff().getLastleftoffObject().y);
            messeges.add("z :" + getLastLeftOff().getLastleftoffObject().z);
            messeges.add("Crop type :" + getLastLeftOff().getLastleftoffObject().getCropType().toString());
            messeges.add("time :" + CalendarUtils.ConvertMilliSecondsToFormattedDate(getLastLeftOff().getLastleftoffObject().getTime()));
            messeges.add("macro stage :" + getLastLeftOff().getLastleftoffObject().getMacroStage().toString());
        }


        messeges.forEach(sendChatMessage::sendClientDebugMessage);
    }
}
