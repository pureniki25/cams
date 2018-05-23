package com.hongte.alms.finance.req.repayPlan.trial;

import com.baomidou.mybatisplus.annotations.TableField;
import com.hongte.alms.finance.req.repayPlan.ProjFeeDetailReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@ApiModel("标的费用明细,分标的业务必须填写")
public class TrailProjFeeReq {

    /**
     * [是否设置期限范围]
     */
    @ApiModelProperty(required= true,value = "[是否设置期限范围,1是，0 否]")
    private Integer isTermRange;
//    @ApiModelProperty(value = "标的出款申请费用明细期限范围明细列表，分段收费的费用，必须填写这一项")
//    private List<ProjFeeDetailReq> ProjFeeDetailInfos;

    @ApiModelProperty(value = "标的出款申请费用明细期限范围明细列表，分段收费的费用，必须填写这一项，期数从1开始计算")
    private List<ProjFeeDetailReq> feeDetailReqMap;


    /**
     * 费用项目ID
     */
    @ApiModelProperty(value = "费用项目ID")
    private String feeItemId;
    /**
     * 费用项目名称
     */
    @ApiModelProperty(value = "费用项目名称")
    private String feeItemName;


//    /**
//     * 费用项类型
//     */
//    @ApiModelProperty(required= true,value = "费用项所属分类，" +
//            "10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，" +
//            "50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收")
//    private Integer feeType;


    /**
     * 费用项所属分类名称
     */
    @ApiModelProperty(required= true,value = "费用项所属分类名称")
    private String feeTypeName;

    /**
     * [业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值]
     */
    @ApiModelProperty(required= true,value = "[业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值]")
    private BigDecimal feeValue;

    /**
     * [是否一次收取，1为按月收取，2为一次收取]
     */
    @ApiModelProperty(required= true,value = "[是否一次收取，1为按月收取，2为一次收取]")
    private Integer isOneTimeCharge;

    /**
     * [标记该项费用的还款类型，1:期初收取,2:期末收取]
     */
    @ApiModelProperty(required= true,value = "[标记该项费用的还款类型，1:期初收取,2:期末收取]")
    private Integer repaymentFlag;

    public String getFeeItemId() {
        return feeItemId;
    }

    public void setFeeItemId(String feeItemId) {
        this.feeItemId = feeItemId;
    }

    public String getFeeItemName() {
        return feeItemName;
    }

    public void setFeeItemName(String feeItemName) {
        this.feeItemName = feeItemName;
    }

    public String getFeeTypeName() {
        return feeTypeName;
    }

    public void setFeeTypeName(String feeTypeName) {
        this.feeTypeName = feeTypeName;
    }

    public BigDecimal getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(BigDecimal feeValue) {
        this.feeValue = feeValue;
    }



    public Integer getIsOneTimeCharge() {
        return isOneTimeCharge;
    }

    public void setIsOneTimeCharge(Integer isOneTimeCharge) {
        this.isOneTimeCharge = isOneTimeCharge;
    }


    public Integer getRepaymentFlag() {
        return repaymentFlag;
    }

    public void setRepaymentFlag(Integer repaymentFlag) {
        this.repaymentFlag = repaymentFlag;
    }


    public Integer getIsTermRange() {
        return isTermRange;
    }

    public void setIsTermRange(Integer isTermRange) {
        this.isTermRange = isTermRange;
    }




//    public Integer getFeeType() {
//        return feeType;
//    }
//
//    public void setFeeType(Integer feeType) {
//        this.feeType = feeType;
//    }
//


    public List<ProjFeeDetailReq> getFeeDetailReqMap() {
        return feeDetailReqMap;
    }

    public void setFeeDetailReqMap(List<ProjFeeDetailReq> feeDetailReqMap) {
        this.feeDetailReqMap = feeDetailReqMap;
    }
}
