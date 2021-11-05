package io.github.kingstefan26.stefans_util.core.onlineFeatures.repo;

import com.google.gson.Gson;
import io.github.kingstefan26.stefans_util.core.globals;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.repo.objects.mainRepoObject;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class mainRepoManager {
    Logger logger;
    public Gson gson;

    public static mainRepoManager mainRepoManager_;
    public static mainRepoManager getMainRepoManager(){
        if(mainRepoManager_ == null) mainRepoManager_ = new mainRepoManager();
        return mainRepoManager_;
    }
    private mainRepoManager(){
        mainrepoobject = new mainRepoObject();
        logger = LogManager.getLogger("mainRepoManager");
        gson = new Gson();
    }

    public static class customRepoReloadedEvent extends Event{ }
    public static class repoReloadedEvent extends Event{ }


    private mainRepoObject mainrepoobject;
    private static boolean finishedInitDownloadFlag;

    public void startup(){
        (new Thread(() -> {
            long start = System.currentTimeMillis();
            mainrepoobject = reloadRepo();
            finishedInitDownloadFlag = true;
            MinecraftForge.EVENT_BUS.post(new repoReloadedEvent());
            long stop = System.currentTimeMillis();
            logger.info("finished repo startup in " + (stop - start));
        })).start();
    }

    public void loadCustomRepoData(String url){
        (new Thread(() -> {
            long start = System.currentTimeMillis();
            mainrepoobject = reloadRepo(url);
            finishedInitDownloadFlag = true;
            MinecraftForge.EVENT_BUS.post(new customRepoReloadedEvent());
            long stop = System.currentTimeMillis();
            logger.info("finished loadCustomRepoData in " + (stop - start));
        })).start();
    }

    private mainRepoObject reloadRepo(){
        return reloadRepo(globals.mainRepoUrl);
    }

    private mainRepoObject reloadRepo(String url){
        try {
            return gson.fromJson(APIHandler.downloadTextFromUrl(url), mainRepoObject.class);
        } catch (Exception e) {
            logger.error("error during repo reload hehe", e);
        }
        return null;
    }

    public mainRepoObject getMainrepoobject() {
        if(finishedInitDownloadFlag){
            return mainrepoobject;
        }
        return null;
    }
}
