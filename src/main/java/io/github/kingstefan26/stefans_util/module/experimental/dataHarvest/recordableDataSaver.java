/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.experimental.dataHarvest;

import io.github.kingstefan26.stefans_util.util.fileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static io.github.kingstefan26.stefans_util.util.file.configDirectoryPath;

public class recordableDataSaver {

    private static recordableDataSaver instance;
    public static recordableDataSaver getInstance() {
        return instance == null ? instance = new recordableDataSaver() : instance;
    }


    Logger logger = LogManager.getLogger("recordableDataSaver");

    static File saveFile;

    static {
        try {
            fileUtils.makeSureDiractoriesExist(configDirectoryPath + File.separator + "stefanUtil" + File.separator + "dataCollector");
            saveFile = fileUtils.getFileAtPath(configDirectoryPath + File.separator + "stefanUtil" + File.separator + "dataCollector" + File.separator + "recorder.txt");
        } catch (IOException e) {
            e.printStackTrace();
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


}
