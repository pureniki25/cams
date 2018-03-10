package com.hongte.alms.common.open;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * 用于对外接口的加解密类
 */
public class OpenSecureUtil {

    /**
     * 3DES加密
     *
     * @param key
     *            密钥
     * @param source
     *            明文
     * @return Base64编码后的密文
     * @throws Exception
     */
    public static String tripleDESEncrypt(String key, String source) throws Exception {
        if (key == null) {
            throw new NullPointerException(key);
        }
        if (source == null) {
            throw new NullPointerException(source);
        }
        if (key.length() > 24) {
            key = key.substring(0, 24);
        }

        DESedeKeySpec spec = new DESedeKeySpec(key.getBytes("utf-8"));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        Key deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] resultBytes = cipher.doFinal(source.getBytes("utf-8"));
        return Base64.getEncoder().encodeToString(resultBytes);
    }

    /**
     * 3DES解密
     *
     * @param key
     *            密钥
     * @param source
     *            Base64编码后的密文
     * @return 明文
     * @throws Exception
     */
    public static String tripleDESDecrypt(String key, String source) throws Exception {
        if (key == null) {
            throw new NullPointerException(key);
        }
        if (source == null) {
            throw new NullPointerException(source);
        }
        if (key.length() > 24) {
            key = key.substring(0, 24);
        }

        DESedeKeySpec spec = new DESedeKeySpec(key.getBytes("utf-8"));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        Key deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] resultBytes = cipher.doFinal(Base64.getDecoder().decode(source));
        return new String(resultBytes);
    }

}
