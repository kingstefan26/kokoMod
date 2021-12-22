/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import org.junit.jupiter.api.Test;

import java.awt.*;

class testTrayTest {
    @Test
    void traytest(){
        try {
            (new testTray()).startTray();
            Thread.sleep(10000);
        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testNotif(){
        try{
            (new testTray()).testNotif();
            Thread.sleep(10000);
        }catch (Exception ignored){}
    }

}