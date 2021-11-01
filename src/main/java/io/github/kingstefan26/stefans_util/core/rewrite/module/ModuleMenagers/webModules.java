package io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers;

import com.google.gson.Gson;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.util.InlineCompiler;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import io.github.kingstefan26.stefans_util.util.util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Objects;
import java.util.concurrent.*;

public class webModules {
    Logger logger = LogManager.getLogger("webModules");
    static final String remoteModuleResourcesURL = "https://raw.githubusercontent.com/kingstefan26/cockmod-data/main/modules.json";
    ArrayList<String> loadedWebClasses = new ArrayList<>();
    ArrayList<moduleJsonObject> missingDependencyWebClasses = new ArrayList<>();
    ArrayList<String> totalDepList = new ArrayList<>();

    static class moduleJsonObject {
        public String type;
        public String classpath;
        public String classLocation;
        public String[] dependecies;
    }
    static class codeResourceObject {
        public String id;
        public String code;
        public codeResourceObject(String id, String code){
            this.code = code;
            this.id = id;
        }
    }

    static class MasterResourceObject {
        public moduleJsonObject[] moduleJsonObject;
        public codeResourceObject[] codeResourceObjects;

    }









    int counter = 0;
    public void init() {
        Future<moduleJsonObject[]> future =
                Executors.newSingleThreadExecutor().submit(() -> {
                    Gson gson = new Gson();
                    return gson.fromJson(APIHandler.downloadTextFromUrl(remoteModuleResourcesURL), moduleJsonObject[].class);
                });
        moduleJsonObject[] moduleJsonObjectArray = new moduleJsonObject[0];
        try {
            moduleJsonObjectArray = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }



        ExecutorService s = Executors.newSingleThreadExecutor();
        //non-blocking resource downloading
        util.submit(s,() -> {
            final MasterResourceObject masterResourceObject = new MasterResourceObject();
            Gson gson = new Gson();
            masterResourceObject.moduleJsonObject = gson.fromJson(APIHandler.downloadTextFromUrl(remoteModuleResourcesURL), moduleJsonObject[].class);
            ArrayList<codeResourceObject> codeResourceObjects = new ArrayList<>();
            for(moduleJsonObject m : masterResourceObject.moduleJsonObject){
                codeResourceObjects.add(new codeResourceObject(m.classLocation, APIHandler.downloadTextFromUrl(m.classLocation)));
            }
            return masterResourceObject;
        }, (MasterResourceObject) -> {

        });



        try {
            long start = System.currentTimeMillis();

            logger.info("Starting web load");

            logger.info("found " + moduleJsonObjectArray.length + " module/s in the repo");
            totalDepList.add("core:" + main.VERSION);
            loadedWebClasses.add("core:" + main.VERSION);
            for (moduleJsonObject mjo : moduleJsonObjectArray) {
                totalDepList.add(mjo.classpath);

                boolean isMissingADep = CheckForMissingDep(mjo);

                if(!isMissingADep){
                    loadModuleFormThing(mjo);
                    loadedWebClasses.add(mjo.classpath);
                }else{
                    missingDependencyWebClasses.add(mjo);
                }
            }

            if(!missingDependencyWebClasses.isEmpty()){
                recheckClasses();
            }

            long stop = System.currentTimeMillis();
            logger.info("finished loading web modules in " + (stop - start) + "ms, loaded " + counter + " module/s");
        } catch (Exception e) {
            logger.error("Something bad happened", e);
            e.printStackTrace();
        }
    }


    private void recheckClasses(){
        ListIterator<moduleJsonObject> iter = missingDependencyWebClasses.listIterator();
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
                    loadModuleFormThing(mjo);
                    loadedWebClasses.add(mjo.classpath);
                    iter.remove();
                }
            }
        }

        if(!missingDependencyWebClasses.isEmpty()) {
            recheckClasses();
        }
    }

    private boolean CheckForMissingDep(moduleJsonObject moduleJsonObject){
        boolean isMissing = false;
        for(String dependency : moduleJsonObject.dependecies){
            if (!loadedWebClasses.contains(dependency)) {
                isMissing = true;
                break;
            }
        }
        return isMissing;
    }

    private void loadModuleFormThing(moduleJsonObject moduleJsonObject){
        logger.info("loading " + moduleJsonObject.classpath);
        if(Objects.equals(moduleJsonObject.type, "module")){
            try {
                final basicModule m = (basicModule) InlineCompiler.compileAndReturnObject(moduleJsonObject.classpath, APIHandler.downloadTextFromUrl(moduleJsonObject.classLocation));
                if (m != null) {
                    m.onLoad();
                    moduleRegistery.loadedModules.add(m);
                    logger.info("successfully loaded " + m.getName());
                    counter++;
                }
            } catch (Exception e) {
                logger.info("failed to load " + moduleJsonObject.classpath);
                e.printStackTrace();
            }
        }else if (Objects.equals(moduleJsonObject.type, "util")){
            try {
                final Object m = InlineCompiler.compileAndReturnObject(moduleJsonObject.classpath, APIHandler.downloadTextFromUrl(moduleJsonObject.classLocation));
                logger.info("successfully loaded " + Objects.requireNonNull(m).getClass().getName());
                counter++;
            } catch (Exception e) {
                logger.info("failed to load " + moduleJsonObject.classpath);
                e.printStackTrace();
            }
        }
    }
}
