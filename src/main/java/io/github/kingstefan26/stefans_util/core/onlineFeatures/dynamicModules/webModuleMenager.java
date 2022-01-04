/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.onlineFeatures.dynamicModules;

import com.google.gson.Gson;
import io.github.kingstefan26.stefans_util.util.file;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class webModuleMenager {
    Logger logger = LogManager.getLogger("webModules");

    private static webModuleMenager instance;

    public static webModuleMenager getInstance() {
        return instance == null ? instance = new webModuleMenager() : instance;
    }

// example index (an array of jar objects):
//    [
//    {
//    "filename":"something.jar"
//    "md5":"cd2f2c223758fd2e3e60e153c993913f",
//    "fileLocation":"https://raw.githubusercontent.com/kingstefan26/cockmod-data/something.jar",
//    }
//    ]

    static class jarObject {
        public String filename;
        // TODO: integrate web cashing with md5 hashes
        public String md5;
        public String fileLocation;
    }

    static class MasterResourceObject {
        public webModuleMenager.jarObject[] jarObjects;
        // first string is download url, second is path where the file is placed
        public HashMap<String, String> resources = new HashMap<>();
    }


    public void init(final String repourl) throws IOException {

        //blocking resource downloading bc i need to load the modules on the main thread
        //BUT i cant signal download finished to the main thread so sad

        Gson gson = new Gson();

        long start = System.currentTimeMillis();

        MasterResourceObject masterResourceObject = new MasterResourceObject();

        logger.info("Downloading web assets index");
        logger.info(repourl);
        masterResourceObject.jarObjects = gson.fromJson(APIHandler.downloadTextFromUrl(repourl), jarObject[].class);

        logger.info("Downloading assets");
        for (jarObject m : masterResourceObject.jarObjects) {
            // loop tru all the "jar" objects and download them
            logger.info(m.fileLocation + m.filename);
            File downloadfile = new File(file.configDirectoryPath + File.separator + "stefanUtil" + File.separator + "assets" + File.separator + m.filename);
            logger.info(file.configDirectoryPath + File.separator + "stefanUtil" + File.separator + "assets" + File.separator + m.filename);
            FileUtils.copyURLToFile(new URL(m.fileLocation), downloadfile);
            masterResourceObject.resources.put(m.fileLocation, downloadfile.getAbsolutePath());
            logger.info("Downloaded " + m.filename);
        }
        long stop = System.currentTimeMillis();


        logger.info("finished downloading assets in " + (stop - start) + "ms, loaded " + masterResourceObject.resources.size() + " objects");


        try {
            start = System.currentTimeMillis();
            int counter = 0;

            logger.info("Starting jar load");
            logger.info("found " + masterResourceObject.resources.size() + " jar/s in the repo");

            for (jarObject jar : masterResourceObject.jarObjects) {
                counter++;
                jarLoader.loadJar(getResourceFromMasterObject(jar.fileLocation, masterResourceObject));
            }


            stop = System.currentTimeMillis();
            logger.info("finished loading jars in " + (stop - start) + "ms, loaded " + counter + " jar/s");


        } catch (Exception e) {
            logger.error("Something bad happened", e);
        }

    }


    private String getResourceFromMasterObject(String resourceUrl, MasterResourceObject master) {
        for (Map.Entry<String, String> entry : master.resources.entrySet()) {
            if (Objects.equals(entry.getKey(), resourceUrl)) {
                return entry.getValue();
            }
        }
        return null;
    }

}
