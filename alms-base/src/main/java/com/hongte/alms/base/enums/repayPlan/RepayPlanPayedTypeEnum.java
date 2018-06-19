package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 还款计划支付类型枚举
 */
public enum RepayPlanPayedTypeEnum implements IEnum {

    //null或0：还款中，6：申请展期已还款，10：线下确认已还款，20：自动线下代扣已还款，21，人工线下代扣已还款，
    // 30：自动银行代扣已还款，31：人工银行代扣已还款，40：用户APP主动还款，50：线下财务确认全部结清，60：线下代扣全部结清，70：银行代扣全部结清

    PAYING(0,"还款中",0),
    RENEW_PAY(6,"申请展期已还款",0),
    OFFLINE_CHECK_PAY(10,"线下确认已还款",1),
    OFFLINE_AUTO_WITH_HOLD_PAY(20,"自动线下代扣已还款",2),
    OFFLINE_MANUAL_WITH_HOLD_PAY(21,"人工线下代扣已还款",2),
    BANK_AUTO_WITH_HOLD_PAY(30,"自动银行代扣已还款",3),
    BANK_MANUAL_WITH_HOLD_PAY(31,"人工银行代扣已还款",3),
    APP_PAY(40,"用户APP主动还款",4),
    OFFLINE_CHECK_SETTLE(50,"线下财务确认全部结清",1),
    OFFLINE_WITH_HOLD_SETTLE(60,"线下代扣全部结清",2),
    BANK_WITH_HOLD_SETTLE(70,"银行代扣全部结清",3),
    GET_MONEY_PAY(80,"提款时已还款",0);

//1:线下转账,2:第三方代扣,3:银行代扣,4:APP网关充值,5:协议代扣
    private Integer value;
    private String desc;
    private  Integer  classifyId;

    RepayPlanPayedTypeEnum(final int value, final String desc,final int classifyId) {
        this.value = value;
        this.desc = desc;
        this.classifyId = classifyId;
    }



    public static String descOf(Integer key){
        for(RepayPlanPayedTypeEnum d : RepayPlanPayedTypeEnum.values()){
            if(d.value.equals(key)){
                return d.desc;
            }
        }
        return null;

    }

    public static RepayPlanPayedTypeEnum getByValue(Integer value){
        for(RepayPlanPayedTypeEnum d : RepayPlanPayedTypeEnum.values()){
            if(d.value.equals(value)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(RepayPlanPayedTypeEnum d : RepayPlanPayedTypeEnum.values()){
            if(d.desc.equals(name)){
                return d.value;
            }
        }
        return null;

    }


    @Override
    public Integer getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }


    public Integer getClassifyId() {
        return classifyId;
    }
}
