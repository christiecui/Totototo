package com.example.cuiqi.htmlphrase.utils;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by cuiqi on 16/7/7.
 */
public class AppBrandNetWorkDataEncryptLogic {

    public static final String KEY_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    //generate iv
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception{
        AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
        params.init(new IvParameterSpec(iv));
        return params;
    }

    //encrypt
    public static byte[] encrypt(byte[] data,byte[] keyBytes,AlgorithmParameters iv) throws Exception {
        Key key  = new SecretKeySpec(keyBytes,KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key,iv);
        return cipher.doFinal(data);
    }

    //decrypt
    public static byte[] decrypt(byte[] encryptedData,byte[] keyBytes,AlgorithmParameters iv) throws Exception{
        Key key = new SecretKeySpec(keyBytes,KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key,iv);
        return cipher.doFinal(encryptedData);
    }

    //encrypt with seed
    public static byte[] doEncrypt(byte[] data, byte[] seed) {
        byte[] encryptData = new byte[0];
        try {
            byte[] key = seed;
            AlgorithmParameters iv = generateIV(seed);
            encryptData = encrypt(data,key,iv);
            return encryptData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptData;
    }

    public static byte[] doDecrypt(byte[] data, byte[] seed) {
        byte[] decryptData = new byte[0];
        try {
            byte[] key = seed;
            AlgorithmParameters iv = generateIV(seed);
            decryptData = decrypt(data,key,iv);
            return decryptData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptData;
    }
}
