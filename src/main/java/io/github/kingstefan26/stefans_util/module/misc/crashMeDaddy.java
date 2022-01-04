/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import sun.misc.Unsafe;

import java.lang.reflect.Field;


public class crashMeDaddy extends basicModule {
    public crashMeDaddy() {
        super("crash me daddy", "crashes you cutely", moduleManager.Category.MISC);
    }

    private static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        return (Unsafe) theUnsafe.get(null);
    }

    @Override
    public void onEnable() {
        try {
            getUnsafe().getByte(0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        super.onEnable();
    }
}
