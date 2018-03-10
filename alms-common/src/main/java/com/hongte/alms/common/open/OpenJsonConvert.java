package com.hongte.alms.common.open;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 用于第三方接口的序列化和反序列化
 */
public class OpenJsonConvert {

    public static <T> T  deserializeTripleDESEncryptText(String encryptText,String key,Class<T> clazz) throws Exception {
        String decodedText = URLDecoder.decode(encryptText, "UTF-8");
        String decryptedText = OpenSecureUtil.tripleDESDecrypt(key, decodedText);
        return JSON.parseObject(decryptedText,clazz);
    }
}
