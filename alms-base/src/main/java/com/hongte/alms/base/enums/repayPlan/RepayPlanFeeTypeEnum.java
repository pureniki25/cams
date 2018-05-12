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
//    本金：b69a84dc-ed67-4c9f-80bf-89ee8efd5167，利息：556bce4f-f3a9-4b7a-a8b1-43368bebb49c，滞纳金：79069922-e13a-4229-8656-2a1e19b44879，
// 冲应收：adede422-4293-4456-8517-5b4c8874b700，展期未结清服务费：f6b645e8-480b-11e7-8ed5-000c2928bb0d，展期未结清其他费用：3a401d0a-480c-11e7-8ed5-000c2928bb0d
    PRINCIPAL(10,"本金","b69a84dc-ed67-4c9f-80bf-89ee8efd5167",1),   //uuid与信贷 固定费用id 一致
    INTEREST(20,"利息","556bce4f-f3a9-4b7a-a8b1-43368bebb49c",2),    //uuid与信贷 固定费用id 一致
    SUB_COMPANY_CHARGE(30,"资产端分公司服务费","3df61688-d5a0-49c5-9c32-46b30855310a",3),
    BOND_COMPANY_CHARGE(40,"担保公司费用","78269549-3c3c-4ed0-937d-c077ee87dbe0",3),
    PLAT_CHARGE(50,"资金端平台服务费","cf7a15e1-9bc8-44fc-b53e-de3c84234211",3),
    OVER_DUE_AMONT(60,"滞纳金","097e7d7b-9671-4027-a329-b07375092028",5), //uuid与信贷 固定费用id 一致
    PENALTY_AMONT(70,"违约金","79069922-e13a-4229-8656-2a1e19b44879",5),
    AGENCY_FEE(80,"中介费","fe27638a-91a8-4555-920e-9c6a535e7781",4),
    DEPOSIT_FEE(90,"押金类费用","3aeef26a-9685-4148-98cb-fa3f88181c67",4)
    ,RUSH_RECEIVABLES(100,"冲应收","adede422-4293-4456-8517-5b4c8874b700",6)   //uuid与信贷 固定费用id 一致
    ;
//1:本金; 2:利息; 3:服务费; 4:其他费用; 5:违约金;6:冲应收
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
