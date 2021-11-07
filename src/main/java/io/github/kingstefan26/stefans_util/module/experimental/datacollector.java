/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.experimental;

import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static io.github.kingstefan26.stefans_util.util.file.configDirectoryPath;

public class datacollector extends basicModule {

    public datacollector() {
        super("datacollector", "hey", moduleManager.Category.MACRO);
    }

    File saveFile = new File(configDirectoryPath + File.separator + "stefanUtil" + File.separator + "dataCollector" + File.separator + "recorder.txt");
    @Override
    public void onEnable() {
        super.onEnable();
        try {
            if(saveFile.createNewFile()){
                logger.info("file already exits");
            }else {
                logger.info("created file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    FileWriter myWriter = new FileWriter(file, false);
//        myWriter.write(e.getContent());
//        myWriter.close();


    static class Recordble{
        public Recordble(long timestamp) {
            this.timestamp = timestamp;
        }

        public long timestamp;
    }

    static class walkRecordble extends Recordble {
        public walkRecordble(long timestamp, int key) {
            super(timestamp);
            this.key = key;
        }

        public int key;

    }

    public void logEvent(Recordble rec){
        if( rec instanceof walkRecordble){
            FileWriter myWriter = null;
            try {
                myWriter = new FileWriter(saveFile, true);
                myWriter.write("t:" + rec.timestamp + " key:" + ((walkRecordble) rec).key + System.lineSeparator());
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent e) {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null)
            return;
        try {
            if (Keyboard.isCreated()) {
                if (Keyboard.getEventKeyState()) {
                    int keyCode = Keyboard.getEventKey();
                    if (keyCode <= 0)
                        return;
                    if(keyCode == Keyboard.KEY_W){
                        logEvent(new walkRecordble(System.currentTimeMillis(), Keyboard.KEY_W));
                    }
                }
            }
        } catch (Exception ignored) {}
    }

}
