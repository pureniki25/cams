package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 还款来源枚举
 */
public enum RepayPlanRepaySrcEnum implements IEnum {

//还款来源，10：线下转账，20：线下代扣，30：银行代扣
//合规化还款对应的状态：1:线下转账,2:第三方代扣,3:银行代扣,4:PC网关充值,6:App快捷充值,5:协议代扣
	/**
	 * 0减免
	 */
	DERATE(0,"减免",1),
    /**
     * 10线下转账
     */
    OFFLINE_TRANSFER(10,"线下转账",1),
    /**
     * 11用结余还款
     */
    SURPLUS_REPAY(11,"用结余还款",1),
    /**
     * 20线下代扣
     */
    OFFLINE_WITHHOLD(20,"线下代扣",2),//第三方代扣即为线下代扣
    /**
     * 21人工线下代扣
     */
    OFFLINE_WITHHOLD_MAN(21,"人工线下代扣",2),
    /**
     * 30银行代扣
     */
    BNAK_WITHHOLD(30,"银行代扣",3),
    /**
     * 31人工银行代扣
     */
    BNAK_WITHHOLD_MAN(31,"人工银行代扣",3),
    /**
     * 40PC网关充值
     */
    PC_GATEWAY(40,"PC网关充值",4),
    /**
     * 50App快捷充值
     */
    APP_FAST_CHARGE(50,"App快捷充值",6),
    /**
     * 60协议代扣
     */
    BY_AGGREMENT(60,"协议代扣",5)
    ;

    private int value;
    private String desc;
    private int platRepayVal;//合规化还款对应的还款来源

    RepayPlanRepaySrcEnum(final int value, final String desc,final int platRepayVal) {
        this.value = value;
        this.desc = desc;
        this.platRepayVal = platRepayVal;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }


    public static String descOf(Integer value){
        for(RepayPlanRepaySrcEnum d : RepayPlanRepaySrcEnum.values()){
            if(value.equals(d.value)){
                return d.desc;
            }
        }
        return null;

    }

    public static RepayPlanRepaySrcEnum getByValue(Integer value){
        for(RepayPlanRepaySrcEnum d : RepayPlanRepaySrcEnum.values()){
            if(value.equals(d.value)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(RepayPlanRepaySrcEnum d : RepayPlanRepaySrcEnum.values()){
            if(d.desc.equals(name)){
                return d.value;
            }
        }
        return null;

    }


    public int getPlatRepayVal() {
        return platRepayVal;
    }

    public void setPlatRepayVal(int platRepayVal) {
        this.platRepayVal = platRepayVal;
    }
}
