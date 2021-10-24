package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Loader;

import java.lang.reflect.Field;

public class vanillaspofer extends Module {

    //Lnet/minecraftforge/fml/common/Loader;mods:Ljava/util/List;
    public  vanillaspofer(){
        super("vanillaspofer", "cock and bones", ModuleManager.Category.MISC);
    }

    @Override
    public void onEnable(){
        super.onEnable();

//        ClassA instance = classA;
//        // Do the reflection
//        Field list = instance.getClass().getDeclaredField("list");
//        list.setAccessible(true);
//        ArrayList<Object> actualList = (ArrayList<Object>) list.get(instance);
//        for( Object arg : args ){
//            actualList.add(arg);
//        }
//        list.set(instance, actualList);
//        System.out.println(actualList);
//        System.out.println(classA);


        Field modsField = null;
        Loader privateinstance = Loader.instance();
        try {
            modsField = Loader.class.getDeclaredField("modController");
            modsField.setAccessible(true);
            LoadController value = (LoadController) modsField.get(privateinstance);


            chatService.queueClientChatMessage("stefan_util is no more hehe", chatService.chatEnum.CHATPREFIX);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
