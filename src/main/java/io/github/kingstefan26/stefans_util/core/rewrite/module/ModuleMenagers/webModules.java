package io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers;

import com.google.gson.Gson;
import io.github.kingstefan26.stefans_util.core.globals;
import io.github.kingstefan26.stefans_util.core.newConfig.fileCacheing.webCache;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.util.InlineCompiler;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import io.github.kingstefan26.stefans_util.util.util;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class webModules {
    Logger logger = LogManager.getLogger("webModules");
    static final String remoteModuleResourcesURL = "https://raw.githubusercontent.com/kingstefan26/cockmod-data/main/modules.json";
    ArrayList<String> loadedClasses = new ArrayList<>();
    ArrayList<moduleJsonObject> missingDependencyClasses = new ArrayList<>();
    ArrayList<String> totalDepList = new ArrayList<>();

    private static webModules webModules_;
    public static webModules getInstance(){
        if(webModules_ == null) webModules_ = new webModules();
        return webModules_;
    }

    static class moduleJsonObject {
        public String type;
        public String classpath;
        public String md5;
        public String classLocation;
        public String[] dependecies;
    }
    static class MasterResourceObject {
        public moduleJsonObject[] moduleJsonObject;
        public HashMap<String, String> codeResources = new HashMap<>();
    }


    public void init(){
        init(remoteModuleResourcesURL);
    }

    int counter = 0;
    public void init(final String repourl) {

//        ExecutorService s = Executors.newSingleThreadExecutor();

        //blocking resource downloading bc i need to load the modules on the main thread
        //BUT i cant signal download finished to the main thread so sad
        util.submitSync(() -> {

            long start = System.currentTimeMillis();logger.info("Downloading web assets");
            MasterResourceObject masterResourceObject = new MasterResourceObject();
            Gson gson = new Gson();

            logger.info("Downloading main json file");
            masterResourceObject.moduleJsonObject = gson.fromJson(APIHandler.downloadTextFromUrl(repourl), moduleJsonObject[].class);
            logger.info("Downloading resource files");
            for(moduleJsonObject m : masterResourceObject.moduleJsonObject){
                if (m.dependecies.length == 0) {
                    logger.info("THIS SHOULD NOT BE EMPTY SMHHHH");
                    FMLCommonHandler.instance().exitJava(1, true);
                }
                masterResourceObject.codeResources.put(m.classLocation, webCache.downloadWithCaching(m.classLocation, m.md5));
                logger.info("Downloaded " + m.classLocation);
            }
            long stop = System.currentTimeMillis();
            logger.info("finished downloading web assets in " + (stop - start) + "ms, loaded " + (masterResourceObject.codeResources.size() + 1) + " objects");

            return masterResourceObject;
        }, (MasterResourceObject) -> {
            try {
                long start = System.currentTimeMillis();

                logger.info("Starting web load");
                logger.info("found " + MasterResourceObject.moduleJsonObject.length + " module/s in the repo");
                totalDepList.add(globals.COREVERISON);
                loadedClasses.add(globals.COREVERISON);
                for (moduleJsonObject mjo : MasterResourceObject.moduleJsonObject) {
                    totalDepList.add(mjo.classpath);

                    boolean isMissingADep = CheckForMissingDep(mjo);

                    if(!isMissingADep){
                        loadModuleFormThing(mjo, MasterResourceObject);
                        loadedClasses.add(mjo.classpath);
                    }else{
                        missingDependencyClasses.add(mjo);
                    }
                }

                if(!missingDependencyClasses.isEmpty()){
                    recheckClasses(MasterResourceObject);
                }

                long stop = System.currentTimeMillis();
                logger.info("finished loading web modules in " + (stop - start) + "ms, loaded " + counter + " module/s");


            } catch (Exception e) {
                logger.error("Something bad happened", e);
            }
        });
    }


    private void recheckClasses(MasterResourceObject master){

        long start = System.currentTimeMillis();
        long stop = System.currentTimeMillis();
        logger.info("finished isthinfthen that  in " + (stop - start));

        ListIterator<moduleJsonObject> iter = missingDependencyClasses.listIterator();

        while(iter.hasNext()){
            moduleJsonObject mjo = iter.next();

            boolean missingdep = false;
            for(String dep : mjo.dependecies){
                if (!totalDepList.contains(dep)) {
                    missingdep = true;
                    break;
                }
            }
            if(missingdep) {
                logger.error( mjo.classpath + " is missing dependencies");
                iter.remove();
            }else{
                if(!CheckForMissingDep(mjo)){
                    loadModuleFormThing(mjo, master);
                    loadedClasses.add(mjo.classpath);
                    iter.remove();
                }
            }
        }

        if(!missingDependencyClasses.isEmpty()) {
            recheckClasses(master);
        }
    }

    private boolean CheckForMissingDep(moduleJsonObject moduleJsonObject){
        if(moduleJsonObject == null) throw new NullPointerException("WHY IS THIS NULL !!!!!!");
        boolean isMissing = false;
        for(String dependency : moduleJsonObject.dependecies){
            if (!loadedClasses.contains(dependency)) {
                isMissing = true;
                break;
            }
        }
        return isMissing;
    }

    private String getResourceFromMasterObject(String resourceUrl, MasterResourceObject master){
        for (Map.Entry<String, String> entry : master.codeResources.entrySet()) {
            if(Objects.equals(entry.getKey(), resourceUrl)){
                return entry.getValue();
            }
        }
        return null;
    }

    private void loadModuleFormThing(moduleJsonObject moduleJsonObject, MasterResourceObject master){
        logger.info("loading " + moduleJsonObject.classpath);
        if(Objects.equals(moduleJsonObject.type, "module")){
            try {
                logger.info("module is a standart module");
                String resource = getResourceFromMasterObject(moduleJsonObject.classLocation, master);
                if (resource != null) {
                    logger.info("compiling code");
                    final basicModule m = (basicModule) InlineCompiler.compileAndReturnObject(moduleJsonObject.classpath, resource);
                    if (m != null) {
                        m.onLoad();
                        moduleRegistery.loadedModules.add(m);
                        logger.info("successfully loaded " + m.getName());
                        counter++;
                    }
                }
            } catch (Exception e) {
                logger.info("failed to load " + moduleJsonObject.classpath);
                e.printStackTrace();
            }
        }else if (Objects.equals(moduleJsonObject.type, "util")){
            try {
                logger.info("module is a util module");
                String resource = getResourceFromMasterObject(moduleJsonObject.classLocation, master);
                if (resource != null) {
                    logger.info("compiling code");
                    final Object m = InlineCompiler.compileAndReturnObject(moduleJsonObject.classpath, resource);
                    logger.info("successfully loaded " + Objects.requireNonNull(m).getClass().getName());
                    counter++;
                }
            } catch (Exception e) {
                logger.info("failed to load " + moduleJsonObject.classpath);
                e.printStackTrace();
            }
        }
    }
}
