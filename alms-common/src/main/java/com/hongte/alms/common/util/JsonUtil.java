package com.hongte.alms.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zengkun
 * @since 2018/1/25 json转换配置
 */
public class JsonUtil {
	private static SerializeConfig mapping = new SerializeConfig();
	private static String dateFormat;
	static {
		dateFormat = "yyyy-MM-dd HH:mm:ss";
		mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
	}

	public static SerializeConfig getMapping() {
		return mapping;
	}

	public static <T> T map2obj(Map<?, ?> map, Class<T> clazz) {
		return JSON.parseObject(JSON.toJSONString(map), clazz);
	}

	public static <T> List<T> map2objList(Object map, Class<T> clazz) {
		return JSONArray.parseArray(JSON.toJSONString(map), clazz);
	}

	/**
	 * 判断字符串是否为json格式
	 * 
	 * @author 胡伟骞
	 * @param jsonStr
	 * @return
	 */
	public static boolean isJSONValid(String jsonStr) {
		try {
			JSONObject.parseObject(jsonStr);
		} catch (JSONException ex) {
			try {
				JSONObject.parseArray(jsonStr);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}
}
