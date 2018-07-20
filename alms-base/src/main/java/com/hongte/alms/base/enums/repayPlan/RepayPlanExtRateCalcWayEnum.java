package com.hongte.alms.base.enums.repayPlan;

/**
 * @author zengkun
 * @since 2018/5/28
 * 额外费率计算方式枚举
 */
public enum RepayPlanExtRateCalcWayEnum {

//	:1.借款金额*费率值；2剩余本金*费率值;3,1*费率值

    BY_BORROW_MONEY(1,"借款金额*费率值"),
    BY_REMIND_MONEY(2,"剩余本金*费率值")
    ,RATE_VALUE(3,"1*费率值")
    ,REMIND_PLAT_FEE(4,"剩余的平台服务费合计")
    ,BY_MONTH_COM_FEE(5,"费率值*月收分公司服务费")
    ,BY_MONTH_PLAT_FEE(6,"费率值*月收平台服务费")
    ,BY_REM_MONEY_AND_FEE(7,"(剩余本金*费率值) - 分公司服务费违约金 - 平台服务费违约金")
            ;

    private Integer key; // 数据保存的值
    private String name; // 名称

    private RepayPlanExtRateCalcWayEnum(int key, String name) {
        this.name = name;
        this.key = key;
    }


    public static String nameOf(Integer key){
        for(RepayPlanExtRateCalcWayEnum d : RepayPlanExtRateCalcWayEnum.values()){
            if(d.key.equals(key)){
                return d.name;
            }
        }
        return null;

    }

    public static RepayPlanExtRateCalcWayEnum getByKey(String key){
        for(RepayPlanExtRateCalcWayEnum d : RepayPlanExtRateCalcWayEnum.values()){
            if(d.key.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(RepayPlanExtRateCalcWayEnum d : RepayPlanExtRateCalcWayEnum.values()){
            if(d.name.equals(name)){
                return d.key;
            }
        }
        return null;

    }

    public String getName() {
        return name;
    }

    public int getKey() {
        return key;
    }


}
