package com.hongte.alms.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zengkun
 * @since 2018/1/25
 * json转换配置
 */
public class JsonUtil {
    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;
    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
    }

    public  static  SerializeConfig getMapping(){
        return  mapping;
    }
    public static <T> T map2obj(Map<?, ?> map, Class<T> clazz)  {
        return JSON.parseObject(JSON.toJSONString(map), clazz);
    }
    public static <T> List<T> map2objList(Object map, Class<T> clazz)  {
        return JSONArray.parseArray(JSON.toJSONString(map), clazz);
    }
}
