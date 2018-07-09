package com.hongte.alms.base.enums;

/**
 * 角色区域控制方式
 * Created by 张贵宏 on 2018/7/9 13:57
 */
public enum RoleAreaMethodEnum {
    /**
     * 无, 默认全部区域性的
     */
    NULL_Area(0),
    /**
     * 财务跟单设置方式
     */
    FINANCIAL_ORDER(1);


    private Integer value;

    private RoleAreaMethodEnum(Integer value) {
        this.value = value;
    }

    public static RoleAreaMethodEnum valueOf(Integer value) {
        for (RoleAreaMethodEnum e : RoleAreaMethodEnum.values()) {
            if (e.value == value) {
                return e;
            }
        }
        return null;
    }

    public Integer value() {
        return this.value;
    }
}
