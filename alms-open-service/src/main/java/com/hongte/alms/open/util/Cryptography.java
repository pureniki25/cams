package com.hongte.alms.open.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * 加密解密工具类
 * @author 林子尧
 */
public class Cryptography {

    private static final Logger log = LoggerFactory.getLogger(Cryptography.class);

    private static final String Algorithm = "DESede"; // 定义 加密算法,可用 DES,DESede,Blowfish

    /**
     * ECB加密
     *
     * @param original 源文
     * @param key      秘钥
     * @return
     */
    public static String tripleDESEncrypt(String original, String key) throws Exception {
        byte[] keybyte = key.substring(0, 24).getBytes("UTF-8");
        // 生成密钥
        SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
        // 加密
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.ENCRYPT_MODE, deskey);
        return Base64.getEncoder().encodeToString(c1.doFinal(original.getBytes("UTF-8")));
    }

    /**
     * ECB解密
     *
     * @param cryptograph 密文
     * @param key         秘钥
     * @return
     */
    public static String tripleDESDecrypt(String cryptograph, String key) throws Exception {
        byte[] keybyte = key.substring(0, 24).getBytes();
        byte[] src = Base64.getDecoder().decode(cryptograph);
        // 生成密钥
        SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
        // 解密
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.DECRYPT_MODE, deskey);
        return new String(c1.doFinal(src));
    }

    /**
     * CBC加密
     *
     * @param key      密钥
     * @param keyiv    IV
     * @param original 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static String des3EncodeCBC(byte[] key, byte[] keyiv, String original) throws Exception {
        byte[] data = original.getBytes("UTF-8");
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv, 0, 8);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return Base64.getEncoder().encodeToString(bOut);
    }

    /**
     * CBC解密
     *
     * @param key         密钥
     * @param keyiv       IV
     * @param cryptograph Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    public static String des3DecodeCBC(byte[] key, byte[] keyiv, String cryptograph) throws Exception {
        byte[] data = Base64.getDecoder().decode(cryptograph);
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv, 0, 8);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return new String(bOut, "UTF-8");
    }

}

