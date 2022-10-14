/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.prototypeModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.FileUtils;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static io.github.kingstefan26.stefans_util.util.FileUtils.configDirectoryPath;

public class RecordMouse extends prototypeModule {
    static File saveFile;

    static {
        try {
            FileUtils.makeSureDiractoriesExist(configDirectoryPath + File.separator + "stefanUtil" + File.separator + "dataCollector");
            saveFile = FileUtils.getFileAtPath(configDirectoryPath + File.separator + "stefanUtil" + File.separator + "dataCollector" + File.separator + UUID.randomUUID() + "mouse.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean recording = false;
    Gson gson = new Gson();

    public RecordMouse() {
        super(RecordMouse.class.getName());
    }

    @Override
    protected void PROTOTYPETEST() {

        recording = !recording;
        if (!recording) {
            chatService.queueCleanChatMessage("Stopping recording");
            try {
                saveFile = FileUtils.getFileAtPath(configDirectoryPath + File.separator + "stefanUtil" + File.separator + "dataCollector" + File.separator + UUID.randomUUID() + "mouse.txt");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            chatService.queueClientChatMessage("Starting recording");
        }
    }

    public void logEvent(String content, long timestamp) {
        try {
            FileWriter myWriter = new FileWriter(saveFile, true);
            myWriter.write("t:" + timestamp + " " + content + System.lineSeparator());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END && mc.thePlayer == null) return;

        if (recording) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("yaw", new JsonPrimitive(mc.thePlayer.rotationYaw));
            jsonObject.add("pitch", new JsonPrimitive(mc.thePlayer.rotationPitch));

            this.logEvent(gson.toJson(jsonObject), System.currentTimeMillis());

        }

    }
}
