package com.gonali.task.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by TianyuanPan on 7/1/16.
 */
public class MD5Utils {

    private MD5Utils() {
    }

    public static String getDigest(InputStream is, MessageDigest md, int byteArraySize)
            throws NoSuchAlgorithmException,
            IOException {

        md.reset();

        byte[] bytes = new byte[byteArraySize];
        int numBytes;

        while ((numBytes = is.read(bytes)) != -1) {

            md.update(bytes, 0, numBytes);
        }

        byte[] digest = md.digest();

        String result = "";

        for (byte b : digest) {
            // byte转换成16进制
            result += String.format("%02x", b);
        }

        return result;
    }

    public static String getStringMD5(String msg) {

        MessageDigest md = null;

        try {

            md = MessageDigest.getInstance("MD5");

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
            return "";
        }

        md.reset();

        md.update(msg.getBytes(), 0, msg.getBytes().length);

        byte[] bytes = md.digest();

        String result = "";

        for (byte b : bytes) {
            // byte转换成16进制
            result += String.format("%02x", b);
        }

        return result;
    }
/*
    //test
    public template void main(String[] args) {
        String s = "admin";

        System.out.println(getStringMD5(s));
    }*/

}
