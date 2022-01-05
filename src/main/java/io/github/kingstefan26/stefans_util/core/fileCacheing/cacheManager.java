package io.github.kingstefan26.stefans_util.core.fileCacheing;

import io.github.kingstefan26.stefans_util.util.cryptography;
import io.github.kingstefan26.stefans_util.util.file;
import io.github.kingstefan26.stefans_util.util.fileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;

public class cacheManager {                                                                                                  
    private static cacheManager instance;

    public static cacheManager getInstance() {
        return instance == null ? instance = new cacheManager() : instance;
    }

    public static final Logger logger = LogManager.getLogger("cacheManager-kokomod");
    private final static String cacheFolderDriactory = System.getProperty("user.dir") + File.separator + "stefanUtil" + File.separator + "cache";
// configDirectoryPath + File.separator + "stefanUtil" + File.separator + configFileName;

    ArrayList<CacheObject.cacheObject> cacheObjects = new ArrayList<>();

    public void init() {
        logger.info("cache directory" + cacheFolderDriactory);
        // Get the file
        File directoryFile = new File(cacheFolderDriactory);

        try {
            if(!directoryFile.exists()){
                Path folder = Paths.get(cacheFolderDriactory);
                Files.createDirectories(folder);
            }

        } catch (Exception ignored) {}

        cacheObjects = readCache();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> dumpCache(cacheObjects)));
    }


    private ArrayList<CacheObject.cacheObject> readCache() {
        final ArrayList<CacheObject.cacheObject> temp = new ArrayList<>();

        int counter = 0;
        for (String a : fileUtils.mapFolder(cacheManager.cacheFolderDriactory, false)) {

            try {
                File filez = new File(a);
                if (filez.exists()) {
//                    FileInputStream inputStream = new FileInputStream(filez);
//                    String everything = IOUtils.toString(inputStream);
//                    inputStream.close();
                    String everything = fileUtils.getFileTextContents(filez);

                    CacheObject.cacheObject tempporary = new CacheObject.cacheObject();

                    tempporary.setContent(everything);
                    tempporary.setMetaData(new CacheObject.metaData(filez.getName(), cryptography.getStringHash(everything, "MD5")));
                    counter++;
                    temp.add(tempporary);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("concluded reading cache files, loaded " + counter + " objects");
        temp.forEach(a -> logger.info("File name: " + a.getMetaData().name + " hash: " + a.getMetaData().hash));


        return temp;
    }

    private void dumpCache(ArrayList<CacheObject.cacheObject> e) {
        int counter = 0;
        for (CacheObject.cacheObject obj : e) {
            dumpToFile(obj);
            counter++;
        }
        logger.info("dumped " + counter + " objects");
        logger.info("concluded the file dump lmao");
    }

    private void dumpToFile(CacheObject.cacheObject e) {
        try {

            fileUtils.writeTextToFile(fileUtils.getFileAtPath(cacheFolderDriactory + File.separator + e.getMetaData().name),
                    e.getContent(),
                    false);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void removeObject(String fileName) {
        cacheObjects.removeIf(obj -> Objects.equals(fileName, obj.getMetaData().name));
    }

    public void updateCacheContent(String fileName, String contents) {
        cacheObjects.removeIf(obj -> Objects.equals(fileName, obj.getMetaData().name));
        try {
            CacheObject.cacheObject tempporary = new CacheObject.cacheObject();
            tempporary.setContent(contents);
            tempporary.setMetaData(new CacheObject.metaData(fileName, cryptography.getStringHash(contents, "MD5")));

            cacheObjects.add(tempporary);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


    public void cache(String fileName, String contents) {
        try {
            CacheObject.cacheObject tempporary = new CacheObject.cacheObject();
            tempporary.setContent(contents);
            tempporary.setMetaData(new CacheObject.metaData(fileName, cryptography.getStringHash(contents, "MD5")));
            cacheObjects.add(tempporary);

            dumpToFile(tempporary);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public boolean isCachedByName(String fileName) {
        for (CacheObject.cacheObject obj : cacheObjects) {
            if (Objects.equals(obj.getMetaData().name, fileName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCachedByHash(String hash) {
        for (CacheObject.cacheObject obj : cacheObjects) {
            if (Objects.equals(obj.getMetaData().hash, hash)) {
                return true;
            }
        }
        return false;
    }

    public String requestCacheByName(String fileName) {
        for (CacheObject.cacheObject obj : cacheObjects) {
            if (Objects.equals(obj.getMetaData().name, fileName)) {
                return obj.getContent();
            }
        }
        return null;
    }

    public String requestCacheByHash(String hash) {
        for (CacheObject.cacheObject obj : cacheObjects) {
            if (Objects.equals(obj.getMetaData().hash, hash)) {
                return obj.getContent();
            }
        }
        return null;
    }

    public int clearCache() {
        int ammout = cacheObjects.size();
        cacheObjects.clear();
        file.emptyFolder(new File(cacheFolderDriactory));
        return ammout;
    }
}
