package io.github.kingstefan26.stefans_util.core.commands;

import io.github.kingstefan26.stefans_util.module.macro.wart.UniversalWartMacro;
import io.github.kingstefan26.stefans_util.module.macro.util.util;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.impl.notificationService;
import io.github.kingstefan26.stefans_util.service.Service;
import io.github.kingstefan26.stefans_util.service.serviceMenager;
import io.github.kingstefan26.stefans_util.util.CalendarUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.ArrayList;
import java.util.List;

import static io.github.kingstefan26.stefans_util.module.render.lastLeftOff.getLastLeftOff;


public class commandRegistry {
    public static ArrayList<CommandBase> simpleCommands;

    public commandRegistry() {
        simpleCommands = new ArrayList<>();
        simpleCommands.add(new SimpleCommand("kokomod", new SimpleCommand.ProcessCommandRunnable() {
            public void processCommand(ICommandSender sender, String[] args) {
                chatService.queueClientChatMessage("funny that you ask", chatService.chatEnum.CHATPREFIX);
            }
        }));
        simpleCommands.add(new SimpleCommand("lastleftoffdebug", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                ArrayList<String> messeges = new ArrayList<>();
                if (getLastLeftOff().getLastleftoffObject() == null) {
                    messeges.add("error getting last left off object");
                    return;
                } else {
                    messeges.add("x :" + getLastLeftOff().getLastleftoffObject().getX());
                    messeges.add("y :" + getLastLeftOff().getLastleftoffObject().getY());
                    messeges.add("z :" + getLastLeftOff().getLastleftoffObject().getZ());
                    messeges.add("Crop type :" + getLastLeftOff().getLastleftoffObject().getCropType().toString());
                    messeges.add("time :" + CalendarUtils.ConvertMilliSecondsToFormattedDate(getLastLeftOff().getLastleftoffObject().getTime()));
                    messeges.add("macro stage :" + getLastLeftOff().getLastleftoffObject().getMacroStage().toString());
                }
                messeges.forEach(a -> chatService.queueClientChatMessage(a, chatService.chatEnum.DEBUG));
            }
        }));
        simpleCommands.add(new SimpleCommand("listmods", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                Loader.instance().getActiveModList().forEach(a -> {
                    chatService.queueClientChatMessage("Mod id: " + a.getModId(), chatService.chatEnum.DEBUG);
                });
                List<ModContainer> c = Loader.instance().getModList();
//                c.forEach(a -> chat.queueClientChatMessage("Mod id: " + a.getModId(), chat.chatEnum.DEBUG));
                for (ModContainer abc : c) {
                    chatService.queueClientChatMessage("Mod id: " + abc.getModId(), chatService.chatEnum.DEBUG);
                    System.out.println("Mod id: " + abc.getModId());
                }
            }
        }));

        simpleCommands.add(new SimpleCommand("push", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {

                if (args.length > 2) {
                    notificationService.push(new notificationService.Notification(args[0], args[1], 10));
                } else {
                    notificationService.push(new notificationService.Notification("cock", "and bones", 2));
                }
            }
        }));
        simpleCommands.add(new SimpleCommand("KokoModPrivacyPolicy", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiScreen() {

                    final String[] b = new String[]{
                            "    Kokoniara LLC Privacy Policy",
                            "This privacy policy (\"policy\") will help you understand how Kokoniara LLC (\"us\", \"we\", \"our\") uses and doesn't protects the data",
                            "We reserve the right to change this policy at any given time, of which you will not be promptly updated on.",
                            "What User Data We Collect",
                            "When you visit the website, we may collect the following data:",
                            "        • Your IP address.",
                            "        • You Mine-craft Token and client id",
                            "        • Your address zip code",
                            "        • You social security number.",
                            "Why We Collect Your Data",
                            "We are collecting your data for several reasons:",
                            "        • To Log into Your account",
                            "        • steal ur SkyBlock coins"
                    };

                    @Override
                    public void drawScreen(int par1, int par2, float par3) {
                        super.drawScreen(par1, par2, par3);
                        super.drawDefaultBackground();
                        for (int i = 0; i < b.length; ++i) {
                            drawCenteredString(fontRendererObj, b[i], width / 2, height / 3 + 12 * i, 0xFFFFFFFF);
                        }
                    }
                });
            }
        }));

        simpleCommands.add(new SimpleCommand("iAMfacing", new SimpleCommand.ProcessCommandRunnable() {
            boolean between(float variable, float minValueInclusive, float maxValueInclusive) {
                return variable >= minValueInclusive && variable <= maxValueInclusive;
            }
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                //get block in front of player face and feet
                util.checkBlock inFrontofFace = null;
                float yaw = UniversalWartMacro.getYaw();

                if (between(yaw, -45, 45)) {
                    chatService.queueClientChatMessage("facing pozitive Z");
                    inFrontofFace = util.retriveCheckBlockInCords(util.unRelitaviseCords(new Vec3(0,1,1) ,util.getPlayerFeetVec()));
                }
                else if (between(yaw, -135, -45)) {
                    chatService.queueClientChatMessage("facing positive X");
                    inFrontofFace = util.retriveCheckBlockInCords(util.unRelitaviseCords(new Vec3(1,1,0) ,util.getPlayerFeetVec()));
                }
                else if (between(yaw, 135, 180) && between(yaw, -135, -180)) {
                    chatService.queueClientChatMessage("facing negative Z");
                    inFrontofFace = util.retriveCheckBlockInCords(util.unRelitaviseCords(new Vec3(0,1,-1) ,util.getPlayerFeetVec()));
                }
                else if (between(yaw, 45, 135)) {
                    chatService.queueClientChatMessage("facing negative X");
                    inFrontofFace = util.retriveCheckBlockInCords(util.unRelitaviseCords(new Vec3(-1,1,0) ,util.getPlayerFeetVec()));
                }

                if(inFrontofFace != null){
                    chatService.queueClientChatMessage("in front of face" + inFrontofFace.name);
                }
            }
        }));

        simpleCommands.add(new SimpleCommand("ServicesStatus", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                chatService.queueCleanChatMessage("============= KOKOMOD SERVICES =============");
                for(Service s : serviceMenager.services){
                    switch (s.serviceStatus){
                        case NULL:
                        case ERRORED:
                            chatService.queueCleanChatMessage("§4●§r " + s.name + " §4" + s.serviceStatus.name());
                            break;
                        case STOPPING:
                        case INITIALISING:
                            chatService.queueCleanChatMessage("§6●§r " + s.name + " §6" + s.serviceStatus.name());
                            break;
                        case STOPPED:
                            chatService.queueCleanChatMessage("§c●§r " + s.name + " §c" + s.serviceStatus.name());
                            break;
                        case RUNNING:
                            chatService.queueCleanChatMessage("§2●§r " + s.name + " §2" + s.serviceStatus.name());
                            break;
                    }

                }
                chatService.queueCleanChatMessage("============================================");
            }
        }));
    }
}