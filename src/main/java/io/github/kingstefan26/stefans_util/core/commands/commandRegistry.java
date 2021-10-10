package io.github.kingstefan26.stefans_util.core.commands;

import io.github.kingstefan26.stefans_util.core.clickgui.newGui.newClickGui;
import io.github.kingstefan26.stefans_util.module.util.chat;
import io.github.kingstefan26.stefans_util.util.CalendarUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.ArrayList;
import java.util.List;

import static io.github.kingstefan26.stefans_util.module.macro.macroUtil.lastLeftOff.lastLeftOff.getLastLeftOff;


public class commandRegistry {
    public static ArrayList<CommandBase> simpleCommands;
    public commandRegistry(){
        simpleCommands = new ArrayList<>();
        simpleCommands.add(new SimpleCommand("kokomod", new SimpleCommand.ProcessCommandRunnable() {
            public void processCommand(ICommandSender sender, String[] args) {
                chat.queueClientChatMessage("funny that you ask", chat.chatEnum.CHATPREFIX);
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
        simpleCommands.add(new SimpleCommand("listmods", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                Loader.instance().getActiveModList().forEach(a -> {
                    chat.queueClientChatMessage("Mod id: " + a.getModId(), chat.chatEnum.DEBUG);
                });
                List<ModContainer> c = Loader.instance().getModList();
//                c.forEach(a -> chat.queueClientChatMessage("Mod id: " + a.getModId(), chat.chatEnum.DEBUG));
                for(ModContainer abc : c){
                    chat.queueClientChatMessage("Mod id: " + abc.getModId(), chat.chatEnum.DEBUG);
                    System.out.println("Mod id: " + abc.getModId());
                }
            }
        }));
    }
}