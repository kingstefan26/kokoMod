package io.github.kingstefan26.stefans_util.core.commands;

import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleRegistery;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.newConfig.fileCacheing.cacheManager;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.auth.authmenager;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.dynamicModules.jarLoader;
import io.github.kingstefan26.stefans_util.module.macro.util.util;
import io.github.kingstefan26.stefans_util.module.render.lastLeftOff;
import io.github.kingstefan26.stefans_util.service.Service;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.impl.notificationService;
import io.github.kingstefan26.stefans_util.service.serviceMenager;
import io.github.kingstefan26.stefans_util.util.CalendarUtils;
import io.github.kingstefan26.stefans_util.util.file;
import io.github.kingstefan26.stefans_util.util.handelers.ScoreboardHandler;
import io.github.kingstefan26.stefans_util.util.renderUtil.updateWidowTitle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.github.kingstefan26.stefans_util.module.macro.wart.helper.wartMacroHelpers.getYaw;


public class
commandRegistry {



    public static ArrayList<CommandBase> simpleCommands = new ArrayList<CommandBase>() {{
        add(new SimpleCommand("itwasjustaprank", new SimpleCommand.ProcessCommandRunnable() {

//            public List<String> getSidebarLines() {
//                List<String> lines = new ArrayList<>();
//                Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
//                if (scoreboard == null) {
//                    return lines;
//                }
//
//                ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
//
//                if (objective == null) {
//                    return lines;
//                }
//
//                Collection<Score> scores = scoreboard.getSortedScores(objective);
//                List<Score> list = Lists.newArrayList(scores.stream()
//                        .filter(input -> input != null && input.getPlayerName() != null && !input.getPlayerName().startsWith("#"))
//                        .collect(Collectors.toList()));
//
//                if (list.size() > 15) {
//                    scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
//                } else {
//                    scores = list;
//                }
//
//                for (Score score : scores) {
//                    ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
//                    lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
//                }
//
//                return lines;
//            }

            public void processCommand(ICommandSender sender, String[] args) {
//                for(String s : getSidebarLines()){
//                    System.out.println(s);
//
//                    String subjectString = StringUtils.stripControlCodes(s.toLowerCase());
//                    subjectString = Normalizer.normalize(subjectString, Normalizer.Form.NFD);
//                    String resultString = subjectString.replaceAll("[^\\x00-\\x7F]", "");
//                    if(resultString.isEmpty()) continue;
//
//                    System.out.println(resultString);
//
//                    chatService.queueClientChatMessage("presonal island " + resultString.contains("your island"));
//
//                }
                chatService.queueClientChatMessage("presonal island " + ScoreboardHandler.getScoreboardAsCleanString().contains("Your Island"));
            }
        }));

        add(new SimpleCommand("resetDisplayTitle", new SimpleCommand.ProcessCommandRunnable() {
            public void processCommand(ICommandSender sender, String[] args) {
                updateWidowTitle.updateTitle("Kokoclient V69.420 | " + authmenager.getInstance().getCashedAuthObject().status);
            }
        }));



        add(new SimpleCommand("kokomod", new SimpleCommand.ProcessCommandRunnable() {
            public void processCommand(ICommandSender sender, String[] args) {
                chatService.queueClientChatMessage("funny that you ask", chatService.chatEnum.CHATPREFIX);
            }
        }));
        add(new SimpleCommand("lastleftoffdebug", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                ArrayList<String> messeges = new ArrayList<>();
                if (lastLeftOff.getLastLeftOff().getLastleftoffObject() == null) {
                    messeges.add("error getting last left off object");
                    return;

                } else {
                    messeges.add("x :" + lastLeftOff.getLastLeftOff().getLastleftoffObject().getX());
                    messeges.add("y :" + lastLeftOff.getLastLeftOff().getLastleftoffObject().getY());
                    messeges.add("z :" + lastLeftOff.getLastLeftOff().getLastleftoffObject().getZ());
                    messeges.add("Crop type :" + lastLeftOff.getLastLeftOff().getLastleftoffObject().getCropType().toString());
                    messeges.add("time :" + CalendarUtils.ConvertMilliSecondsToFormattedDate(lastLeftOff.getLastLeftOff().getLastleftoffObject().getTime()));
                    messeges.add("macro stage :" + lastLeftOff.getLastLeftOff().getLastleftoffObject().getMacroStage().toString());
                }
                messeges.forEach(a -> chatService.queueClientChatMessage(a, chatService.chatEnum.DEBUG));
            }
        }));
        add(new SimpleCommand("listmods", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                Loader.instance().getActiveModList().forEach(a -> chatService.queueClientChatMessage("Mod id: " + a.getModId(), chatService.chatEnum.DEBUG));
                List<ModContainer> c = Loader.instance().getModList();
//                c.forEach(a -> chat.queueClientChatMessage("Mod id: " + a.getModId(), chat.chatEnum.DEBUG));
                for (ModContainer abc : c) {
                    chatService.queueClientChatMessage("Mod id: " + abc.getModId(), chatService.chatEnum.DEBUG);
                    System.out.println("Mod id: " + abc.getModId());
                }
            }
        }));

        add(new SimpleCommand("push", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {

                if (args.length > 2) {
                    notificationService.push(new notificationService.Notification(args[0], args[1], 10));
                } else {
                    notificationService.push(new notificationService.Notification("cock", "and bones", 2));
                }
            }
        }));
        add(new SimpleCommand("KokoModPrivacyPolicy", new SimpleCommand.ProcessCommandRunnable() {
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

        add(new SimpleCommand("iAMfacing", new SimpleCommand.ProcessCommandRunnable() {
            boolean between(float variable, float minValueInclusive, float maxValueInclusive) {
                return variable >= minValueInclusive && variable <= maxValueInclusive;
            }
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                //get block in front of player face and feet
                util.checkBlock inFrontofFace = null;
                float yaw = getYaw();

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

        add(new SimpleCommand("ServicesStatus", new SimpleCommand.ProcessCommandRunnable() {
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
        add(new SimpleCommand("purgeCache", new SimpleCommand.ProcessCommandRunnable() {
            public void processCommand(ICommandSender sender, String[] args) {
                chatService.queueClientChatMessage("purging cache ", chatService.chatEnum.CHATPREFIX);
                int a = cacheManager.getInstance().clearCache();
                chatService.queueClientChatMessage("cleaned " + a + " objects from cache", chatService.chatEnum.CHATPREFIX);
            }
        }));
        add(new SimpleCommand("manualloadthejar", new SimpleCommand.ProcessCommandRunnable() {
            public void processCommand(ICommandSender sender, String[] args) {
                if(args.length == 1 && args[0] != null){
                    jarLoader.loadJar(args[0]);

                } else {
                    jarLoader.loadJar(file.configDirectoryPath + File.separator + "stefanUtil" + File.separator + "assets" + File.separator + "premium.jar");

                }

//                FILE>ASSETS>TRUE>ASSHOLE>DOTASSEST>LOADJAR
            }
        }));
        add(new SimpleCommand("unloadallkokomodmodules", new SimpleCommand.ProcessCommandRunnable() {
            public void processCommand(ICommandSender sender, String[] args) {
                moduleRegistery.getModuleRegistery().unloadAllModules();
                ClickGui.getClickGui().resetAllPositions();
                chatService.queueClientChatMessage("unloaded all modules");
            }
        }));

        add(new SimpleCommand("rerunModuleDiscovery", new SimpleCommand.ProcessCommandRunnable() {
            public void processCommand(ICommandSender sender, String[] args) {
                moduleRegistery.getModuleRegistery().findAndLoadModuleRegistry();
            }
        }));

        add(new SimpleCommand("unloadamodule", new SimpleCommand.ProcessCommandRunnable() {
            public void processCommand(ICommandSender sender, String[] args) {
                if(args.length == 1){
                    basicModule module = moduleRegistery.getModuleRegistery().getModuleByClassName(args[0]);
                    module.onUnload();
                    ClickGui.getClickGui().resetAllPositions();
                    chatService.queueClientChatMessage("unloaded " + args[0]);
                }
            }
        }));

        add(new SimpleCommand("resetGuiPoses", new SimpleCommand.ProcessCommandRunnable() {
            public void processCommand(ICommandSender sender, String[] args) {
                ClickGui.getClickGui().resetAllPositions();
            }
        }));

    }};

}