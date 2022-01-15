/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.onlineFeatures.dynamicModules;

import com.google.gson.Gson;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.auth.authmenager;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.file;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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

    public webModuleMenager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

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


    @SubscribeEvent
    public void onstefan_utilsconnectedToKokoCloud(stefan_utilEvents.connectedToKokoCloud event) {
        if (authmenager.getInstance().getCashedAuthObject() == null) return;
        switch (authmenager.getInstance().getCashedAuthObject().status) {
            case "dev":
            case "premium":
                startWebModules(authmenager.getInstance().getCashedAuthObject().configurl);
                break;
            case "base":
                chatService.queueCleanChatMessage("Looks like you are base, not loading webmodules");
                break;
        }
    }

    public void startWebModules(String configurationUrl) {
        if (System.getProperty("disablewebJarLoad") != null) {
            if (Objects.equals(System.getProperty("disablewebJarLoad"), "true")) {
                return;
            }
        }
        // -DdisablewebJarLoad="true"
        try {
            if (System.getProperty("loadLocalPremiumJar") != null) {
                if (System.getProperty("loadLocalPremiumJar").equals("true")) {

                    String path = file.configDirectoryPath + File.separator + "stefanUtil" + File.separator + "assets" + File.separator + "premium.jar";
                    if ((new File(path)).exists()) {
                        jarLoader.loadJar(file.configDirectoryPath + File.separator + "stefanUtil" + File.separator + "assets" + File.separator + "premium.jar");
                    }

                }
            } else {
                init(configurationUrl);
            }

        } catch (Exception e) {
            logger.info("something went wrong with webmodules");
            e.printStackTrace();
        }

    }


    static class jarObject {
        public String filename;
        // TODO: integrate web cashing with md5 hashes
        public String md5;
        public String fileLocation;
    }

    public void init(final String repourl) throws IOException {

        MasterResourceObject masterResourceObject = downloadAssetIndex(repourl);

        long start = System.currentTimeMillis();

        logger.info("Downloading assets");

        downloadAsstes(masterResourceObject);

        long stop = System.currentTimeMillis();


        logger.info("finished downloading assets in " + (stop - start) + "ms, loaded " + masterResourceObject.resources.size() + " objects");


        start = System.currentTimeMillis();

        int counter = loadAssets(masterResourceObject);

        stop = System.currentTimeMillis();
        logger.info("finished loading jars in " + (stop - start) + "ms, loaded " + counter + " jar/s");
    }

    public MasterResourceObject downloadAssetIndex(final String url) throws IOException {
        Gson gson = new Gson();

        final MasterResourceObject masterResourceObject = new MasterResourceObject();

        logger.info("Downloading web assets index");
        masterResourceObject.jarObjects = gson.fromJson(APIHandler.downloadTextFromUrl(url), jarObject[].class);
        return masterResourceObject;
    }

    public void downloadAsstes(MasterResourceObject masterResourceObject) throws IOException {
        for (jarObject m : masterResourceObject.jarObjects) {
            // loop tru all the "jar" objects and download them
            logger.info(m.fileLocation + m.filename);
            File downloadfile = new File(file.configDirectoryPath + File.separator + "stefanUtil" + File.separator + "assets" + File.separator + m.filename);
            logger.info(file.configDirectoryPath + File.separator + "stefanUtil" + File.separator + "assets" + File.separator + m.filename);
            FileUtils.copyURLToFile(new URL(m.fileLocation), downloadfile);
            masterResourceObject.resources.put(m.fileLocation, downloadfile.getAbsolutePath());
            logger.info("Downloaded " + m.filename);
        }
    }

    public int loadAssets(MasterResourceObject masterResourceObject) {
        try {

            int counter = 0;

            logger.info("Starting jar load");
            logger.info("found " + masterResourceObject.resources.size() + " jar/s in the repo");

            for (jarObject jar : masterResourceObject.jarObjects) {
                counter++;
                jarLoader.loadJar(MasterResourceObject.getResourceFromMasterObject(jar.fileLocation, masterResourceObject));
            }
            return counter;
        } catch (Exception e) {
            logger.error("Something bad happened", e);
        }
        return 0;
    }

    static class MasterResourceObject {
        public webModuleMenager.jarObject[] jarObjects;
        // first string is download url, second is path where the file is placed
        public HashMap<String, String> resources = new HashMap<>();

        static public String getResourceFromMasterObject(String resourceUrl, MasterResourceObject master) {
            for (Map.Entry<String, String> entry : master.resources.entrySet()) {
                if (Objects.equals(entry.getKey(), resourceUrl)) {
                    return entry.getValue();
                }
            }
            return null;
        }
    }
}
