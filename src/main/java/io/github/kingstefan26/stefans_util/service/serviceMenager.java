package io.github.kingstefan26.stefans_util.service;

import com.google.common.reflect.ClassPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.ArrayList;


public class serviceMenager {
    private final Logger logger;
    public static ArrayList<Service> services = new ArrayList<>();

    {
        logger = LogManager.getLogger("kokomod-serviceManager");
    }

    public void start() {
        loadServices();
        for(Service s : services){
            s.rootStart();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down Services");
            shutdown();
        }));
    }

    public void shutdown(){
        for(Service s : services){
            if(s == null) continue;
            s.rootStop();
        }
        services.clear();
    }


    private void loadServices() {
        ArrayList<String> clazzlist = new ArrayList<>();
        try {
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
            for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                if (info.getName().startsWith("io.github.kingstefan26.stefans_util.service.impl")) {
                    final Class<?> clazz = info.load();
                    if (Service.class.isAssignableFrom(clazz)) {
                        if (!clazzlist.contains(clazz.getName())) {
                            logger.info("found service: " + clazz.getName());
                            if(clazzlist.contains(clazz.getName())) continue;

                            clazzlist.add(clazz.getName());

                            try {
                                Class<?> theSerivceClass = Class.forName(clazz.getName());
                                Constructor<?> TheServiceClassConstructor = theSerivceClass.getConstructor();
                                Object object = TheServiceClassConstructor.newInstance();
                                services.add((Service) object);
                                logger.info("loaded service: " + ((Service) object).name);
                            } catch (Exception e) {
                                logger.warn("Failed to load service " + clazz.getName());
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

}