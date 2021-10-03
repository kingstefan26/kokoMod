package io.github.kingstefan26.stefans_util.core.commands;

import com.google.common.collect.Lists;
import io.github.kingstefan26.stefans_util.module.util.chat;
import io.github.kingstefan26.stefans_util.util.CalendarUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;

import static io.github.kingstefan26.stefans_util.module.macro.macroUtil.lastLeftOff.lastLeftOff.getLastLeftOff;


public class commandRegistry {
    public static ArrayList<CommandBase> simpleCommands;
    public commandRegistry(){
        simpleCommands = new ArrayList<>();
        simpleCommands.add(new SimpleCommand("kokomod", new SimpleCommand.ProcessCommandRunnable() {
            public void processCommand(ICommandSender sender, String[] args) {
                chat.queueClientChatMessage("funny that you ask", chat.chatEnum.CHAT);
            }
        }));
        simpleCommands.add(new SimpleCommand("lastleftoffdebug", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                ArrayList<String> messeges = new ArrayList<>();
                if(getLastLeftOff().getLastleftoffObject() == null) {
                    messeges.add("error getting last left off object");
                    return;
                }else{
                    messeges.add("x :" + getLastLeftOff().getLastleftoffObject().getX());
                    messeges.add("y :" + getLastLeftOff().getLastleftoffObject().getY());
                    messeges.add("z :" + getLastLeftOff().getLastleftoffObject().getZ());
                    messeges.add("Crop type :" + getLastLeftOff().getLastleftoffObject().getCropType().toString());
                    messeges.add("time :" + CalendarUtils.ConvertMilliSecondsToFormattedDate(getLastLeftOff().getLastleftoffObject().getTime()));
                    messeges.add("macro stage :" + getLastLeftOff().getLastleftoffObject().getMacroStage().toString());
                }


                messeges.forEach(a -> chat.queueClientChatMessage(a, chat.chatEnum.DEBUG));
            }
        }));
    }
}