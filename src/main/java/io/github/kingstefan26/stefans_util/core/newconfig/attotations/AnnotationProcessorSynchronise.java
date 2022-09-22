/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newconfig.attotations;

import io.github.kingstefan26.stefans_util.core.newconfig.prop.Iproperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AnnotationProcessorSynchronise {
    private static AnnotationProcessorSynchronise instance;
    Logger logger = LogManager.getLogger("ConfigProcessorEngine");
    private final HashMap<Iproperty<?>, ProcessedFiled> fields = new HashMap<>();

    private AnnotationProcessorSynchronise() {
    }

    public static AnnotationProcessorSynchronise getInstance() {
        if (instance == null) instance = new AnnotationProcessorSynchronise();
        return instance;
    }

    /**
     * This function adds the given prop and targetField to the internal sync map
     *
     * @param prop        The prop that will be synchronised regularly
     * @param targetField the targetField from which the value will be drawn
     */
    void addEntry(Iproperty<?> prop, ProcessedFiled targetField) {
        fields.put(prop, targetField);
    }

    /**
     * Call this when unloading a module and the filed and associated prop no longer exists
     *
     * @param name name of prop to be deleted from internal sync list
     */
    void removeEntry(String name) {
        fields.entrySet().removeIf(entry -> entry.getKey().getName().equals(name));
    }

    /**
     * this function grabs all annotation config fields and syncs their values to the respective objects
     *
     * @throws IllegalAccessException When trying to access a field that we cant
     */
    public void reloadAllProperties() throws IllegalAccessException {
        for (Map.Entry<Iproperty<?>, ProcessedFiled> iPropertyFieldEntry : fields.entrySet()) {
            logger.info("field of config : {}", iPropertyFieldEntry.getKey().getName());
            logger.info("name of parent class: {}", iPropertyFieldEntry.getValue().parent.getClass().getName());
            logger.info("name of field: {}", iPropertyFieldEntry.getValue().filed.getName());

            // the parent object e.g. basicModule that the field is located in
            Object parent = iPropertyFieldEntry.getValue().parent;

            // the field of the value we are trying to synchronise
            Field field = iPropertyFieldEntry.getValue().filed;
            field.setAccessible(true);
            // the value of said field
            Object filedValue = field.get(parent);

            // we get the prop object
            Iproperty prop = iPropertyFieldEntry.getKey();
            // set its property with our filed value
            prop.setProperty(filedValue);
        }
    }
}
