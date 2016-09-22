package com.example.cuiqi.htmlphrase.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cuiqi on 16/7/6.
 */
public class SHA1 {

    private SHA1(){

    }

    public static String getSHA1(String str) throws IOException, NoSuchAlgorithmException {
        //there isn't SHA-1 digest algorithm in sdk algorithm package
        final MessageDigest md = MessageDigest.getInstance("SHA1");
        ByteArrayOutputStream bytesOS = new ByteArrayOutputStream();
        bytesOS.write(str.getBytes("UTF-8"));
        byte[] strToBytes = bytesOS.toByteArray();
        byte[] digestBytes = md.digest(strToBytes);
        return bytesToHex(digestBytes);
    }

    public static String getSHA1(byte[] bytes) throws IOException, NoSuchAlgorithmException{
        //there isn't SHA-1 digest algorithm in sdk algorithm package
        final MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] digestBytes = md.digest(bytes);
        return bytesToHex(digestBytes);
    }

    private static String bytesToHex(byte[] bytes) {
        final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        int len = bytes.length;;
        final char[] str = new char[len * 2];
        int k = 0;
        for (int i = 0; i < len; i++) {
            final byte byte0 = bytes[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0x0f];
            str[k++] = hexDigits[byte0 & 0x0f];
        }
        return new String(str);
    }
}
