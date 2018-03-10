package com.hongte.alms.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author tangxs
 * @date 2017/11/15
 * 验证字段
 */
public class FieldValidator {

    private static Logger logger = LoggerFactory.getLogger(FieldValidator.class);
    /**
     *<p>Title: 验证实体类相关字段是否为空</p>
     * @param object 实体类
     * @param filter 类似“remark”,"names",此类属性名的值可以为null
     * @return
     * @author 张鹏
     * @date 2017年12月4日 下午3:42:45
     */
    public static Boolean validaModelRequiredField(Object object,String ...filter){
        for (Field f : object.getClass().getDeclaredFields())
        {
            f.setAccessible(true);
            Object obj = null;
            try {
                obj = f.get(object);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage());
                return false;
            }
            String fieldName = f.getName();
            boolean flag = false;
            for (String fName:filter)
            {
                if (fieldName.equals(fName))
                {
                    flag = true;
                    break;
                }
            }
            if (flag)
            {
                continue;
            }
            if (isEmpty(obj))
            {
            	logger.debug("字段："+fieldName+"为null");
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(Object str) {
        if(str==null) {return true;}
        return str.getClass().getName().contains("String")?
                str == null || "".equals(str.toString().trim()) : str == null;
    }
}
