package com.hongte.alms.common.util;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author zengkun
 * @since 2018/2/1
 * 类对象拷贝帮助类
 */
public class ClassCopyUtil {

    private static Logger logger = LoggerFactory.getLogger(ClassCopyUtil.class);

    public static void fatherToChild (Object father,Object child){
       if(!(child.getClass().getSuperclass()==father.getClass())){
            System.err.println("child不是father的子类");
        }
        Class fatherClass= father.getClass();
//        Class childClass
        Field ff[]= fatherClass.getDeclaredFields();
        for(int i=0;i<ff.length;i++){
            Field f=ff[i];//取出每一个属性，如deleteDate
            Class type=f.getType();
            try {
                Method m = fatherClass.getMethod("get"+upperHeadChar(f.getName()));//方法getDeleteDate
                Object obj=m.invoke(father);//取出属性值
                f.setAccessible(true);
                f.set(child,obj);
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            }
        }
    }

    /**
     * 首字母大写，in:deleteDate，out:DeleteDate
     */
    private static String upperHeadChar(String in){
        String head=in.substring(0,1);
        String out=head.toUpperCase()+in.substring(1,in.length());
        return out;
    }

    public static <T> T copyObject(Object objSource,Class<T> clazzDes) throws InstantiationException, IllegalAccessException{
        return JSONObject.parseObject(JSONObject.toJSONString(objSource),clazzDes);
    }

    /**
     * 拷贝对象方法（适合不同类型的转换）<br/>
     * 前提是，源类中的所有属性在目标类中都存在
     *
     * @param objSource 源对象
     * @param clazzSrc 源对象所属class
     * @param clazzDes 目标class
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T, K> T copy(K objSource,Class<K> clazzSrc,Class<T> clazzDes) throws InstantiationException, IllegalAccessException{


            if(null == objSource) return null;//如果源对象为空，则直接返回null

            T objDes = clazzDes.newInstance();

            // 获得源对象所有属性
            Field[] fields = clazzSrc.getDeclaredFields();

            // 循环遍历字段，获取字段对应的属性值
            for ( Field field : fields )
            {
                // 如果不为空，设置可见性，然后返回
                field.setAccessible( true );

                try
                {
                    String fieldName = field.getName();// 属性名
                    String firstLetter = fieldName.substring(0, 1).toUpperCase();// 获取属性首字母

                    // 拼接set方法名
                    String setMethodName = "set" + firstLetter + fieldName.substring(1);
                    // 获取set方法对象
                    Method setMethod = clazzDes.getMethod(setMethodName,new Class[]{field.getType()});
                    // 对目标对象调用set方法装入属性值
                    setMethod.invoke(objDes, field.get(objSource));
                }
                catch ( Exception e )
                {
                    logger.error("执行{}类的{}属性的set方法时出错。{}",clazzDes.getSimpleName(),field.getName(),e);
                }
            }
            return objDes;
        }




/*

    *//**
         * 根据传入的cls复制整个对象的属性值到另外一个对象的对应的同名和同类型的属性（只复制cls存在的且属同名和同类型的属性值，名为sguid的属性不拷贝）
         * @param vo<Object> 源值对象
         * @param target<Object> 目标对象
         * @param cls<Class> 被拷贝值的对象类型
         * @param not_copy<String> 用于指定不拷贝值的属性，可传多个属性名，之间用逗号隔开
         * @return void
         *//*
    private static void copyObjectValue(Object vo, Object target, Class cls, String not_copy, boolean isCopyNull){
        int flag=0;
        if(StringUtils.isNotBlank(not_copy)){
            not_copy=","+not_copy+",";//前后加逗号是为了后面能够准确的判断所包含的属性名称
        }

        try{
            String sname = "";
            Field[] fields = cls.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                sname = fields[i].getName();

                //如果属性为id或属性名存在于not_copy指的属性名范围中，则不拷贝访属性值
                if(sname.toLowerCase().equals("id") || (StringUtils.isNotBlank(not_copy) && not_copy.indexOf(","+sname+",")!=-1))
                    continue;

                if(fields[i].getType().toString().startsWith("class")
                        && !fields[i].getType().getName().equals("java.lang.String")){ //对象类型字段值拷贝
                    try{
                        BeanUtils.setProperty(target, sname,
                                MethodUtils.invokeMethod(vo,"get"+sname.substring(0, 1).toUpperCase()+sname.substring(1),null));
                    }catch(Exception ne){
                        flag=1;
                        continue;
                    }
                }else{ //基本类型字段值拷贝
                    try{
                        if(isCopyNull==false && BeanUtils.getProperty(vo, sname)==null){
                            continue;
                        }else{
                            BeanUtils.setProperty(target, sname, BeanUtils.getProperty(vo, sname));
                        }
                    }catch(Exception ne){
                        flag=1;
                        continue;
                    }
                }
            }
        }catch(Exception e){
            flag=1;
            e.printStackTrace();
        }

        if(flag==1){
            flag=0;
            System.gc();
        }
    }

    *//**
         * 复制整个对象的属性值到另外一个对象的对应的同名和同类型的属性
         * @param vo<Object> 源值对象
         * @param target<Object> 目标对象
         * @return void
         *//*
    public static void copyObjValue(Object vo, Object target){
        Class cls=vo.getClass();
        while(!cls.getName().equals("java.lang.Object")){
            copyObjectValue(vo, target, cls, null, false);
            cls=cls.getSuperclass();
        }
    }

    *//**
         * 复制整个对象的属性值到另外一个对象的对应的同名和同类型的属性
         * @param vo<Object> 源值对象
         * @param target<Object> 目标对象
         * @param isCopyNull<boolean> 是否拷贝NULL值
         * @return void
         *//*
    public static void copyObjValue(Object vo, Object target, boolean isCopyNull){
        Class cls=vo.getClass();
        while(!cls.getName().equals("java.lang.Object")){
            copyObjectValue(vo, target, cls, null, isCopyNull);
            cls=cls.getSuperclass();
        }
    }

    *//**
         * 复制整个对象的属性值到另外一个对象的对应的同名和同类型的属性，但名为sguid的属性不拷贝
         * @param vo<Object> 源值对象
         * @param target<Object> 目标对象
         * @param not_copy<String> 用于指定不拷贝值的属性，可传多个属性名，之间用逗号隔开
         * @return void
         *//*
    public static void copyObjValue(Object vo, Object target, String not_copy){
        Class cls=vo.getClass();
        while(!cls.getName().equals("java.lang.Object")){
            copyObjectValue(vo, target, cls, not_copy, false);
            cls=cls.getSuperclass();
        }
    }

    *//**
         * 复制整个对象的属性值到另外一个对象的对应的同名和同类型的属性，但名为sguid的属性不拷贝
         * @param vo<Object> 源值对象
         * @param target<Object> 目标对象
         * @param not_copy<String> 用于指定不拷贝值的属性，可传多个属性名，之间用逗号隔开
         * @param isCopyNull<boolean> 是否拷贝NULL值
         * @return void
         *//*
    public static void copyObjValue(Object vo, Object target, String not_copy, boolean isCopyNull){
        Class cls=vo.getClass();
        while(!cls.getName().equals("java.lang.Object")){
            copyObjectValue(vo, target, cls, not_copy, isCopyNull);
            cls=cls.getSuperclass();
        }
    }*/


    }

