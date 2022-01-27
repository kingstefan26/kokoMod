/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newconfig.attotations;

import io.github.kingstefan26.stefans_util.core.newconfig.Iproperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConfigProcessorEngine {
    private static ConfigProcessorEngine instance;
    Logger logger = LogManager.getLogger("ConfigProcessorEngine");
    private HashMap<Iproperty, ProcessedFiled> fields;

    private ConfigProcessorEngine() {
        fields = new HashMap<>();
    }

    public static ConfigProcessorEngine getInstance() {
        if (instance == null) instance = new ConfigProcessorEngine();
        return instance;
    }

    void addEntry(Iproperty prop, ProcessedFiled thafield) {
        fields.put(prop, thafield);
    }

    void removeEntry(String name) {
        fields.entrySet().removeIf(entry -> entry.getKey().getName().equals(name));
    }

    public void reloadAllProperties() throws IllegalAccessException {
        for (Iterator<Map.Entry<Iproperty, ProcessedFiled>> iterator = fields.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Iproperty, ProcessedFiled> ipropertyFieldEntry = iterator.next();
            logger.info("field of config : {}", ipropertyFieldEntry.getKey().getName());
            logger.info("name of parent class: {}", ipropertyFieldEntry.getValue().parent.getClass().getName());
            logger.info("name of field: {}", ipropertyFieldEntry.getValue().filed.getName());

            // the parent object eg basicMoodule that the field is located in
            Object parent = ipropertyFieldEntry.getValue().parent;

            // the field of the value we are trying to syncroinse
            Field field = ipropertyFieldEntry.getValue().filed;
            field.setAccessible(true);
            // the value of said field
            Object filedvalue = field.get(parent);

            // we get the prop object
            Iproperty prop = ipropertyFieldEntry.getKey();
            // set its property with our filed value
            prop.setProperty(filedvalue);
        }
    }
}
