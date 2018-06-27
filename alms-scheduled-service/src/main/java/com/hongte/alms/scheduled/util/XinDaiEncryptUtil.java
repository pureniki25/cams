package com.hongte.alms.scheduled.util;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.vo.ResponseData;
import com.hongte.alms.common.vo.ResponseEncryptData;

/**
 * @author zengkun
 * @since 2018/5/31
 */
public class XinDaiEncryptUtil {

    // 加密
    public static String encryptPostData(String str) throws Exception {

        DESC desc = new DESC();
        str = desc.Encryption(str);

        return str;
    }

    // 解密
    public static String decryptRespData(ResponseEncryptData data) throws Exception {

        DESC desc = new DESC();
        String str = desc.Decode(data.getA(), data.getUUId());
        return str;
    }

    // 返回数据解密
    public static ResponseData getRespData(String str) throws Exception {
        ResponseEncryptData resp = JSON.parseObject(str, ResponseEncryptData.class);
        String decryptStr = decryptRespData(resp);
        EncryptionResult result = JSON.parseObject(decryptStr, EncryptionResult.class);
        ResponseData respData = JSON.parseObject(result.getParam(), ResponseData.class);

        return respData;

    }
}
