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

    PRINCIPAL(10,"本金","b69a84dc-ed67-4c9f-80bf-89ee8efd5167",1),   //uuid与信贷 固定费用id 一致
    INTEREST(20,"利息","556bce4f-f3a9-4b7a-a8b1-43368bebb49c",2),    //uuid与信贷 固定费用id 一致
    SUB_COMPANY_CHARGE(30,"资产端分公司服务费","3df61688-d5a0-49c5-9c32-46b30855310a",3),
    BOND_COMPANY_CHARGE(40,"担保公司费用","78269549-3c3c-4ed0-937d-c077ee87dbe0",3),
    PLAT_CHARGE(50,"资金端平台服务费","cf7a15e1-9bc8-44fc-b53e-de3c84234211",3),
    OVER_DUE_AMONT(60,"滞纳金","097e7d7b-9671-4027-a329-b07375092028",5), //uuid与信贷 固定费用id 一致
    OVER_DUE_AMONT_ONLINE(60,"线上滞纳金","2e646c87-5721-11e8-8a00-0242ac110002",5),
    OVER_DUE_AMONT_UNDERLINE(60,"线下滞纳金","3131c075-5721-11e8-8a00-0242ac110002",5),
    PENALTY_AMONT(70,"违约金","79069922-e13a-4229-8656-2a1e19b44879",5)
    ,PRINCIPAL_PENALTY(70,"本金违约金","9883ee39-4448-11e8-b4f8-0242ac110003",5)  //uuid与信贷 固定费用id 一致
    ,SUB_COMPANY_PENALTY(70,"分公司月收服务费违约金","9e0a6fc1-4448-11e8-b4f8-0242ac110003",5)  //uuid与信贷 固定费用id 一致
    ,PLAT_PENALTY(70,"平台服务费违约金","a0b666c2-4448-11e8-b4f8-0242ac110003",5)   // uuid与信贷 固定费用id 一致
    ,AGENCY_FEE(80,"中介费","fe27638a-91a8-4555-920e-9c6a535e7781",4),
    DEPOSIT_FEE(90,"押金类费用","3aeef26a-9685-4148-98cb-fa3f88181c67",4)
    ,RUSH_RECEIVABLES(100,"冲应收","d5596127-530f-4083-95c5-01e5a75635aa",6)
    ,REBATE(110,"返点","b8c5c29f-dd2c-4a7c-8520-e1e8eb130021",110)  //UUID为随机生成

    ;

//    9883ee39-4448-11e8-b4f8-0242ac110003  提前结清本金违约金
//9e0a6fc1-4448-11e8-b4f8-0242ac110003  提前结清分公司服务费违约金
//    a0b666c2-4448-11e8-b4f8-0242ac110003  提前结清平台服务费违约金


    //110：返点（返点都是不线上分账的）

    private Integer value;
    private String desc;
    private  String uuid;
    private Integer xd_value;

    RepayPlanFeeTypeEnum(final int value, final String desc, final String uuid,Integer xd_value) {
        this.value = value;
        this.desc = desc;
        this.uuid = uuid;
        this.xd_value =xd_value;
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
    
    public static RepayPlanFeeTypeEnum feeIdOf(String feeId) {
    	for(RepayPlanFeeTypeEnum d : RepayPlanFeeTypeEnum.values()){
            if(d.uuid.equals(feeId)){
                return d;
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

    public Integer getXd_value() {
        return xd_value;
    }

    public void setXd_value(Integer xd_value) {
        this.xd_value = xd_value;
    }
}
