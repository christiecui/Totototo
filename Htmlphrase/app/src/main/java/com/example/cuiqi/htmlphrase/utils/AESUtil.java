package com.example.cuiqi.htmlphrase.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by cuiqi on 16/7/6.
 */
public class AESUtil {
    //private static final String TAG = "MicroMsg.AESUtil";


    public static String genKey() throws Exception {

        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128, new SecureRandom(KeySeed.getBytes()));
        SecretKey key = keygen.generateKey();

        ByteArrayOutputStream bout = new ByteArrayOutputStream(256);
        ObjectOutputStream output = new ObjectOutputStream(bout);
        output.writeObject(key);
        output.close();

        byte[] outBuf = bout.toByteArray();
        String outStr = toHexString(outBuf);
        System.out.println("out key length = " + outStr.length() + " , " + outStr);

        return outStr;
    }

    private static final String KeySeed = "com.tencent.mm.key.MicroMsg.Wechat";
    private static final String AESKey = "aced00057372001f6a617661782e63727970746f2e737065632e5365637265744b6579537065635b470b66e230614d0200024c0009616c676f726974686d7400124c6a6176612f6c616e672f537472696e673b5b00036b65797400025b427870740003414553757200025b42acf317f8060854e0020000787000000010402a2173bd6f2542e5e71ee414b2e1e8";

    private static SecretKey key = null;

    public static void initKeyIfNeed() {
        try {
            if (key == null) {
                ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(toBytes(AESKey)));
                key = (SecretKey)input.readObject();
                input.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(final String str) throws Exception {
        if (str == null || str.length() <=0 ) {
            return null;
        }
        initKeyIfNeed();

        Cipher cp = Cipher.getInstance("AES");
        cp.init(Cipher.ENCRYPT_MODE, key);
        byte[] encResult = cp.doFinal(str.getBytes("UTF8"));
        return encResult;
    }

    public static String encryptToString(final String str) throws Exception {
        return toHexString(encrypt(str));
    }

    public static String encryptToString(final long input) throws Exception {
        return toHexString(encrypt(String.valueOf(input)));
    }

    public static String decrypt(final String srt) throws Exception {
        if (srt == null || srt.length() <=0 ) {
            return null;
        }
        initKeyIfNeed();

        Cipher cp = Cipher.getInstance("AES");
        cp.init(Cipher.DECRYPT_MODE, key);
        byte[] decResult = cp.doFinal(toBytes(srt));
        return new String(decResult, "UTF8");
    }

    public static long decryptToLong(final String srt) throws Exception {
        return Long.valueOf(decrypt(srt).trim());
    }

    private static char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String toHexString(byte[] b) {
        if (b == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
            sb.append(HEXCHAR[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static final byte[] toBytes(String s) {
        if (s == null || s.length()<=0) {
            return null;
        }
        byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
