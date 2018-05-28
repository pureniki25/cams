package com.hongte.alms.base.RepayPlan.req;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@ApiModel("标的费用明细,分标的业务必须填写")
public class ProjFeeReq {

    /**
     * [是否设置期限范围]
     */
    @ApiModelProperty(required= true,value = "是否分段收费,1是，0 否")
    @NotNull(message = "是否设置期限范围(isTermRange)不能为空")
    private Integer isTermRange;

    @ApiModelProperty(value = "每期收费列表，分段收费的费用，必须填写这一项，期数从1开始计算")
    private List<ProjFeeDetailReq> feeDetailReqList;

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


    /**
     * 费用项类型
     *             "10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，" +
     "50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收,110:返点"
     */
    @ApiModelProperty(required= true,value = "费用项所属分类，" +
            "30：资产端分公司服务费，40：担保公司费用，" +
            "50：资金端平台服务费，80：中介费，90：押金类费用，110：返点（返点都是不线上分账的）")
    @NotNull(message = "费用项类型(feeType)不能为空")
    private Integer feeType;


    /**
     * 费用项所属分类名称
     */
    @ApiModelProperty(required= true,value = "费用项所属分类名称")
    @NotNull(message = "费用项所属分类名称(feeTypeName)不能为空")
    private String feeTypeName;

    /**
     * 分账标记 0：不线上分账，10：分账到借款人账户，20：分账到资产端账户，30：分账到资金端账户(平台)，40：分账到担保公司账户
     */
    @TableField("account_status")
    @ApiModelProperty(required= true,value = "分账标记 0：不线上分账，10：分账到借款人账户，20：分账到资产端账户，30：分账到资金端账户(平台)，40：分账到担保公司账户")
    @NotNull(message = "分账标记(accountStatus)不能为空")
    private Integer accountStatus;


    /**
     * [业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值]
     */
    @ApiModelProperty(required= true,value = "业务应收取费用值")
    @NotNull(message = "业务应收取费用值(feeValue)不能为空")
    private BigDecimal feeValue;

    //添加进位方式标志位




    /**
     * [收取费用方式，1为按月收取，2为一次收取]
     */
    @ApiModelProperty(required= true,value = "收取费用方式，1为按月收取，2为一次收取")
    @NotNull(message = "收取费用方式(chargeType)不能为空")
    private Integer chargeType;

    /**
     * [标记该项费用的还款类型，1:期初收取,2:期末收取]
     */
    @ApiModelProperty(required= true,value = "[标记该项费用的还款类型，1:期初收取,2:期末收取]")
    @NotNull(message = "标记该项费用的还款类型(repaymentFlag)不能为空")
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



    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
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



    public Integer getFeeType() {
        return feeType;
    }

    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }


    public List<ProjFeeDetailReq> getFeeDetailReqList() {
        return feeDetailReqList;
    }

    public void setFeeDetailReqList(List<ProjFeeDetailReq> feeDetailReqList) {
        this.feeDetailReqList = feeDetailReqList;
    }

}
