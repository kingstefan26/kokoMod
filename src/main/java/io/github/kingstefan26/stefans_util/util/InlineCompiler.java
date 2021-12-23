package io.github.kingstefan26.stefans_util.util;

import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import net.openhft.compiler.CompilerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class InlineCompiler {
    static Logger logger = LogManager.getLogger("inlineCompiler");
    @Deprecated
    public static void someOldTestingShit() {
        // https://raw.githubusercontent.com/kingstefan26/cockmod-data/main/sprint.java
        // dynamically you can call
        String className = "io.github.kingstefan26.stefans_util.module.player.newSprint";

        String javaCode = null;

        chatService.queueCleanChatMessage("§9Downloadign code from: https://raw.githubusercontent.com/kingstefan26/cockmod-data/main/webModules/modules/sprint.java");
        try {
            javaCode = APIHandler.downloadTextFromUrl("https://raw.githubusercontent.com/kingstefan26/cockmod-data/main/webModules/modules/sprint.java");
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            for(String line : sw.toString().split("\\r?\\n")){
                chatService.queueCleanChatMessage(line);
            }
        }
        if(javaCode==null) return;

        String[] lines = javaCode.split("\\r?\\n");

        chatService.queueCleanChatMessage("§9code:");
        for(String s : lines){
            chatService.queueCleanChatMessage(s);
        }

        Class<?> aClass = null;
        try {
            aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode);
        } catch (ClassNotFoundException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            for(String line : sw.toString().split("\\r?\\n")){
                chatService.queueCleanChatMessage(line);
            }
        }
        basicModule runner = null;
        try {
            if (aClass != null) {
                runner = (basicModule) aClass.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            for(String line : sw.toString().split("\\r?\\n")){
                chatService.queueCleanChatMessage(line);
            }
        }
        if (runner != null) {
            runner.onLoad();
        }
    }

    public static Class<?> compileAndReturnClass(@NotNull String classname, @NotNull String classCode) {
        Class<?> clazz = null;
        try {
            clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(classname, classCode);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Object compileAndReturnObject(@NotNull String classname, @NotNull String classCode) {
        Class<?> clazz;
        Object object = null;
        try {
            InlineCompiler.logger.info("compiling class " + classname);
            clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(classname, classCode);
            InlineCompiler.logger.info("compiled successfully");
            if(clazz == null) return null;
            object = clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }
}
