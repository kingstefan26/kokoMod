/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newConfig.fileCacheing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

class webCacheTest {

    @Test
    void downloadWithCaching() {
        cacheManager.getInstance().init();

        try {
            Assertions.assertEquals("test a a    a", webCache.downloadWithCaching("https://raw.githubusercontent.com/kingstefan26/cockmod-data/main/test.java"), "staic elemet failed");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    @Test
    void testDownloadWithCaching() {
    }
}