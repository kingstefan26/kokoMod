package io.github.kingstefan26.stefans_util.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import io.github.kingstefan26.stefans_util.core.config.attotations.AnnotationProcessorSynchronise;
import io.github.kingstefan26.stefans_util.core.config.prop.Iproperty;
import io.github.kingstefan26.stefans_util.core.config.prop.PropFactory;
import io.github.kingstefan26.stefans_util.core.config.prop.impl.*;
import io.github.kingstefan26.stefans_util.util.FileUtils;
import io.github.kingstefan26.stefans_util.util.StefanutilUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ConfigManager {
    public static final String CONFIG_FILE_NAME = "stefan_util.json";
    public static final String CONFIG_FULL_PATH = FileUtils.configDirectoryPath + File.separator + "stefanUtil" + File.separator + CONFIG_FILE_NAME;
    /**
     * Custom gson that can deserialize our props
     */
    private static final Gson gson;
    private static ConfigManager instance;
    Logger logger = LogManager.getLogger("ConfigManagerz");
    private MasterConfigObj masterObj;

    static {
        final RuntimeTypeAdapterFactory<Iproperty> typeFactory = RuntimeTypeAdapterFactory
                .of(Iproperty.class, "type")
                .registerSubtype(boolProp.class, "bool")
                .registerSubtype(doubleProp.class, "double")
                .registerSubtype(floatProp.class, "float")
                .registerSubtype(intProp.class, "int")
                .registerSubtype(stringProp.class, "string");

        gson = new GsonBuilder().registerTypeAdapterFactory(typeFactory).setPrettyPrinting().create();
    }


    private ConfigManager() {
        MinecraftForge.EVENT_BUS.register(this);
        // Get the file
        File f = new File(CONFIG_FULL_PATH);

        try {
            if (!f.exists()) {
                Path path = Paths.get(FileUtils.configDirectoryPath + File.separator + "stefanUtil");
                logger.info("Creating config directories.");
                Files.createDirectories(path);
            }

            boolean newFile = f.createNewFile();

            if (newFile) logger.info("Created config json file.");

        } catch (IOException e) {
            logger.error("An error occurred while creating stefanutil file structure.", e);
        }

        String configContents = null;

        try (FileInputStream inputStream = new FileInputStream(f)) {
            configContents = IOUtils.toString(inputStream);
        } catch (IOException exception) {
            logger.error("Error while reading json config file", exception);
        }

        if (configContents != null) {
            if (Objects.equals(configContents, "")) {
                masterObj = new MasterConfigObj();
            } else {
                masterObj = gson.fromJson(configContents, MasterConfigObj.class);
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> saveMasterObjectToThaFile(masterObj)));
    }

    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }


    int tickcounter;

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent e) {

        tickcounter = StefanutilUtil.everyXTicks(100, e, tickcounter, () -> {
            if (masterObj.isChanged()) {
                saveMasterObjectToThaFile(masterObj);
            }
        });

    }

    public Iproperty getConfigObject(String name, Object deafultValue) {
        // we check if this object already exists
        if (masterObj.getProps() != null) {
            for (Iproperty c : masterObj.getProps()) {
                if (Objects.equals(c.getName(), name)) {
                    logger.info("property {} already existis under the name of {}", c.getName(), name);
                    return c;
                }
            }
        }

        // if it doesn't we create it yay
        final Iproperty conf = PropFactory.getProperty(name, deafultValue);
        createConfigObject(conf);

        return conf;
    }

    public Iproperty getConfigObjectSpetial(String name, Double deafultValue) {
        // we check if this object already exists
        if (masterObj.getProps() != null) {
            for (Iproperty c : masterObj.getProps()) {
                if (Objects.equals(c.getName(), name)) {
                    logger.info("property {} already existis under the name of {}", c.getName(), name);
                    return c;
                }
            }
        }

        final Iproperty conf = new doubleProp(name, deafultValue);
        createConfigObject(conf);

        return conf;
    }


    private void createConfigObject(Iproperty obj) {
        if (masterObj.getProps() != null) {
            for (Iproperty c : masterObj.getProps()) {
                if (Objects.equals(c.getName(), obj.getName())) {
                    logger.info("config Already exists");
                    return;
                }
            }
        }

        masterObj.addProp(obj);
    }

    private void saveMasterObjectToThaFile(MasterConfigObj master) {

        AnnotationProcessorSynchronise.getInstance().reloadAllProperties();

        try (FileWriter myWriter = new FileWriter(CONFIG_FULL_PATH)) {
            myWriter.write(gson.toJson(master));
            master.setChanged(false);
        } catch (IOException e) {
            logger.error("Error while saving json config to file", e);
        }
    }

    public static class MasterConfigObj {
        @Getter
        @Setter
        transient boolean changed;

        @Getter
        @Expose
        Iproperty[] props;

        void addProp(Iproperty prop) {
            changed = true;
            if (props != null) {
                Iproperty[] temp = new Iproperty[props.length + 1];
                System.arraycopy(props, 0, temp, 0, props.length);
                temp[props.length] = prop;
                props = temp;
            } else {
                props = new Iproperty[]{prop};
            }
        }

    }

}
