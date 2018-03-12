package com.hongte.alms.base.process.enums;

/**
 * @author zengkun
 * @since 2018/3/2
 * 流程是否定向打回的枚举
 */
public enum ProcessIsDerateBackEnums {


    YES(1,"是"),
    NO(0,"否");


    private Integer key; // 数据保存的值
    private String name; // 名称

    private ProcessIsDerateBackEnums(Integer key, String name) {
        this.name = name;
        this.key = key;
    }


    public static String nameOf(Integer key){
        for(ProcessIsDerateBackEnums d : ProcessIsDerateBackEnums.values()){
            if(d.key.equals(key)){
                return d.name;
            }
        }
        return null;

    }

    public static ProcessIsDerateBackEnums getByKey(String key){
        for(ProcessIsDerateBackEnums d : ProcessIsDerateBackEnums.values()){
            if(d.key.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(ProcessIsDerateBackEnums d : ProcessIsDerateBackEnums.values()){
            if(d.name.equals(name)){
                return d.key;
            }
        }
        return null;

    }


    public String getName() {
        return name;
    }



    public Integer getKey() {
        return key;
    }




}
