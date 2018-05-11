package com.hongte.alms.base.enums.repayPlan;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author:曾坤
 * @date: 2018/4/24
 * 应还项目所属分类枚举
 */
public enum RepayPlanFeeTypeEnum implements IEnum {
    //应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，
    // 70：违约金，80：中介费，90：押金类费用，100：冲应收

    PRINCIPAL(10,"本金","b69a84dc-ed67-4c9f-80bf-89ee8efd5167"),   //uuid与信贷 固定费用id 一致
    INTEREST(20,"利息","556bce4f-f3a9-4b7a-a8b1-43368bebb49c"),    //uuid与信贷 固定费用id 一致
    SUB_COMPANY_CHARGE(30,"资产端分公司服务费","3df61688-d5a0-49c5-9c32-46b30855310a"),
    BOND_COMPANY_CHARGE(40,"担保公司费用","78269549-3c3c-4ed0-937d-c077ee87dbe0"),
    PLAT_CHARGE(50,"资金端平台服务费","cf7a15e1-9bc8-44fc-b53e-de3c84234211"),
    OVER_DUE_AMONT(60,"滞纳金","097e7d7b-9671-4027-a329-b07375092028"), //uuid与信贷 固定费用id 一致
    OVER_DUE_AMONT_ONLINE(60,"线上滞纳金","097e7d7b-9671-4027-a329-b07375092028"),
    OVER_DUE_AMONT_UNDERLINE(60,"线下滞纳金","097e7d7b-9671-4027-a329-b07375092028"),
    PENALTY_AMONT(70,"违约金","79069922-e13a-4229-8656-2a1e19b44879"),
    AGENCY_FEE(80,"中介费","fe27638a-91a8-4555-920e-9c6a535e7781"),
    DEPOSIT_FEE(90,"押金类费用","3aeef26a-9685-4148-98cb-fa3f88181c67")
    ,RUSH_RECEIVABLES(100,"冲应收","d5596127-530f-4083-95c5-01e5a75635aa")
    ;

    private Integer value;
    private String desc;
    private  String uuid;

    RepayPlanFeeTypeEnum(final int value, final String desc, final String uuid) {
        this.value = value;
        this.desc = desc;
        this.uuid = uuid;
    }

    public static String nameOf(Integer key){
        for(RepayPlanFeeTypeEnum d : RepayPlanFeeTypeEnum.values()){
            if(d.value.equals(key)){
                return d.desc;
            }
        }
        return null;

    }

    public static RepayPlanFeeTypeEnum getByKey(Integer key){
        for(RepayPlanFeeTypeEnum d : RepayPlanFeeTypeEnum.values()){
            if(d.value.equals(key)){
                return d;
            }
        }
        return null;

    }

    public static Integer keyOf(String name){
        for(RepayPlanFeeTypeEnum d : RepayPlanFeeTypeEnum.values()){
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
