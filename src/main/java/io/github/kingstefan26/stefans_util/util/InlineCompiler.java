package io.github.kingstefan26.stefans_util.util;

import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import net.openhft.compiler.CompilerUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class InlineCompiler {
    @Deprecated
    public static void someOldTestingShit() {
        // https://raw.githubusercontent.com/kingstefan26/cockmod-data/main/sprint.java
        // dynamically you can call
        String className = "io.github.kingstefan26.stefans_util.module.player.newSprint";

        String javaCode = null;

        chatService.queueCleanChatMessage("§9Downloadign code from: https://raw.githubusercontent.com/kingstefan26/cockmod-data/main/modules/sprint.java");
        try {
            javaCode = APIHandler.downloadTextFromUrl("https://raw.githubusercontent.com/kingstefan26/cockmod-data/main/modules/sprint.java");
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        basicModule runner = null;
        try {
            if (aClass != null) {
                runner = (basicModule) aClass.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
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
            clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(classname, classCode);
            if(clazz == null) return null;
            object = clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }
}
