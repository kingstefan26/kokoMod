/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class cryptography {


    public static String getMD5Hash(String str) throws NoSuchAlgorithmException {
        // hash string into byte array
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashbytes = md.digest(str.getBytes());

        // convert byte array into hex string and return
        StringBuilder stringBuffer = new StringBuilder();
        for (byte hashbyte : hashbytes) {
            stringBuffer.append(Integer.toString((hashbyte & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    public static String getStringHash(String str, String hashType) throws NoSuchAlgorithmException {
        // hash string into byte array
        MessageDigest md = MessageDigest.getInstance(hashType);
        byte[] hashbytes = md.digest(str.getBytes());

        // convert byte array into hex string and return
        StringBuilder stringBuffer = new StringBuilder();
        for (byte hashbyte : hashbytes) {
            stringBuffer.append(Integer.toString((hashbyte & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}
