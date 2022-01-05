package io.github.kingstefan26.stefans_util.core.newConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.kingstefan26.stefans_util.core.globals;
import io.github.kingstefan26.stefans_util.core.newConfig.impl.*;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class configManagerz {
    private static configManagerz instance;

    public static configManagerz getInstance() {
        return instance == null ? instance = new configManagerz() : instance;
    }


    public static final String configFileName = "stefan_util.json";
    public static final String configDirectoryPath;
    public static final String configFullPath;

    // we make sure we can serialise/deserialize it
    private static final Gson gson;

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


    private masterConfigObj masterObj;

    static {
        configDirectoryPath = System.getProperty("user.dir");
        configFullPath = configDirectoryPath + File.separator + "stefanUtil" + File.separator + configFileName;
    }

    public static class masterConfigObj {
        String version = globals.VERSION;

        @Getter
        Iproperty[] props;

        void addProp(Iproperty prop){
            if(props != null){
                Iproperty[] temp = new Iproperty[props.length + 1];
                System.arraycopy(props, 0, temp, 0, props.length);
                temp[props.length] = prop;
                props = temp;
            } else {
                props = new Iproperty[]{prop};
            }
        }

    }


    int tickcounter;
    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent e){
        if(e.phase != TickEvent.Phase.START) return;

        tickcounter++;
        // this happenes every 200 ticks aka 10 seconds
        if(tickcounter >= 200){
            tickcounter = 0;
            saveMasterObjectToThaFile(masterObj);
        }

    }


    private configManagerz() {
        MinecraftForge.EVENT_BUS.register(this);
        System.out.print(configFullPath);
        // Get the file
        File f = new File(configFullPath);

        try {

            if(!f.exists()) {
                Path path = Paths.get(configDirectoryPath + File.separator + "stefanUtil");

                //java.nio.file.Files;

                Files.createDirectories(path);

                System.out.println("Directory is created!");
            }

            if (f.createNewFile()) {
                System.out.println("File created: " + f.getName());
            } else {
                System.out.println("File already exists.");
            }


            try(FileInputStream inputStream = new FileInputStream(f)) {
                String everything = IOUtils.toString(inputStream);
                inputStream.close();


                // do something with everything string

                if(Objects.equals(everything, "")){
                    masterObj = new masterConfigObj();
                }else{
                    masterObj = gson.fromJson(everything, masterConfigObj.class);
                }

                // silly goffy part

                System.out.println(masterObj.version);

                Iproperty c = getConfigObject("test", false);

                System.out.println(c.getProperty());

            } catch (IOException exception) {
                exception.printStackTrace();
            }



        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> saveMasterObjectToThaFile(masterObj)));
    }


    public Iproperty getConfigObject(String name, Object deafultValue){
        // we check if this object already exists
        if(masterObj.getProps() != null){
            for(Iproperty c : masterObj.getProps()){
                if(Objects.equals(c.getName(), name)){
                    return c;
                }
            }
        }

        // if it dont we create it yay
        final Iproperty conf = configFactory.getInstance().getProperty(name, deafultValue);
        createConfigObject(conf);

        return conf;
    }

    private void createConfigObject(Iproperty obj){
        if(masterObj.getProps() != null){
            for(Iproperty c : masterObj.getProps()){
                if(Objects.equals(c.getName(), obj.getName())){
                    System.out.println("config Already exists");
                    return;
                }
            }
        }

        masterObj.addProp(obj);
    }


    private void saveMasterObjectToThaFile(masterConfigObj master){
        try {
            FileWriter myWriter = new FileWriter(configFullPath);
            myWriter.write(gson.toJson(master));
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
