package io.github.kingstefan26.stefans_util.core.newConfig;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class configManagerz {
    private static configManagerz instance;

    public static configManagerz getInstance() {
        return instance == null ? instance = new configManagerz() : instance;
    }


    public static final String configFileName = "stefan_util.json";
    public static final String configDirectoryPath;
    public static final String configFullPath;

    private final Gson gson = new Gson();

    private masterConfigObj masterObj;

    static {
        configDirectoryPath = System.getProperty("user.dir");
        configFullPath = configDirectoryPath + File.separator + "stefanUtil" + File.separator + configFileName;
    }


    private configManagerz() {
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

                System.out.println(masterObj.version);

                configObj c = new configObj("test");
                c.setaBoolean(true);

                createConfigObject(c);

                configObj test = getExisitingObject("test");

                System.out.println(test.isaBoolean());

                c.setaFloat(20.5F);

            } catch (IOException exception) {
                exception.printStackTrace();
            }



        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> saveMasterObjectToThaFile(masterObj)));
    }



    public void createConfigObject(configObj obj){
        ArrayList<configObj> temp;
        if(masterObj.configObjs != null){
            temp = new ArrayList<>(Arrays.asList(masterObj.configObjs));
            for(configObj c : masterObj.configObjs){
                if(Objects.equals(c.getName(), obj.getName())){
                    System.out.println("config Already exists");
                    return;
                }
            }
        }else{
            temp = new ArrayList<>();
        }
        temp.add(obj);
        masterObj.configObjs = temp.toArray(new configObj[0]);
    }

    public configObj getExisitingObject(String nameOfSaidObject){
        for(configObj c : masterObj.configObjs){
            if(Objects.equals(c.getName(), nameOfSaidObject)){
                return c;
            }
        }
        return null;
    }


    public void saveMasterObjectToThaFile(masterConfigObj master){
        try {
            FileWriter myWriter = new FileWriter(configFullPath);
            myWriter.write(gson.toJson(master));
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
