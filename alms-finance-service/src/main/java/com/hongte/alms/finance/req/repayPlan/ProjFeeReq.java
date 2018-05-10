package com.hongte.alms.finance.req.repayPlan;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@ApiModel("标的费用明细,分标的业务必须填写")
public class ProjFeeReq {

    /**
     * [是否设置期限范围]
     */
    @ApiModelProperty(required= true,value = "[是否设置期限范围,1是，0 否]")
    private Integer isTermRange;
//    @ApiModelProperty(value = "标的出款申请费用明细期限范围明细列表，分段收费的费用，必须填写这一项")
//    private List<ProjFeeDetailReq> ProjFeeDetailInfos;

    @ApiModelProperty(value = "标的出款申请费用明细期限范围明细列表，分段收费的费用，必须填写这一项，期数从1开始计算")
    private List<ProjFeeDetailReq> feeDetailReqMap;


//    /**
//     * 上标编号
//     */
    @ApiModelProperty(required= true,value = "上标编号")
    private String projId;
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
     */
    @ApiModelProperty(required= true,value = "费用项所属分类，" +
            "10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，" +
            "50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收")
    private Integer feeType;


    /**
     * 费用项所属分类名称
     */
    @ApiModelProperty(required= true,value = "费用项所属分类名称")
    private String feeTypeName;

    /**
     * 分账标记(冲应收还款，根据冲应收明细进行分账)，0：不线上分账，10：分账到借款人账户，20：分账到资产端账户，30：分账到资金端账户(平台)，40：分账到担保公司账户
     */
    @TableField("account_status")
    @ApiModelProperty(required= true,value = "分账标记(冲应收还款，根据冲应收明细进行分账)，0：不线上分账，10：分账到借款人账户，20：分账到资产端账户，30：分账到资金端账户(平台)，40：分账到担保公司账户")
    private Integer accountStatus;

    /**
     * 费用收取方式，1为按比例，2为按固定金额
     */
    @ApiModelProperty(required= true,value = "费用收取方式，1为按比例，2为按固定金额")
    private Integer feeChargingType;

    /**
     * 系统默认匹配的费用比例（当收取方式为2时，此字段存零）
     */
    @ApiModelProperty(required= true,value = "系统默认匹配的费用比例（当收取方式为2时，此字段存零）")
    private BigDecimal newSystemDefaultRate;

    /**
     * [业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值]
     */
    @ApiModelProperty(required= true,value = "[业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值]")
    private BigDecimal feeValue;

    //添加进位方式标志位

//    /**
//     * 创建者
//     */
//    @ApiModelProperty(required= true,value = "创建者")
//    private String createUser;
//    /**
//     * 创建时间
//     */
//    @ApiModelProperty(required= true,value = "创建时间")
//    private Date createTime;
//    /**
//     * 更新者
//     */
//    @ApiModelProperty(value = "更新者")
//    private String updateUser;
//    /**
//     * 更新时间
//     */
//    @ApiModelProperty(value = "更新时间")
//    private Date updateTime;
//    /**
//     * 退费申请状态，0或null：退费失败,未申请退费，1：退费成功，2：退费申请中
//     */
//    @ApiModelProperty(required= true,value = "退费申请状态，0或null：退费失败,未申请退费，1：退费成功，2：退费申请中")
//    private Integer RefundStatus;

    /**
     * 对应的提现编号
     */
    @ApiModelProperty(required= true,value = "对应的提现编号")
    private Integer WithdrawId;


    /**
     * [是否一次收取，1为按月收取，2为一次收取]
     */
    @ApiModelProperty(required= true,value = "[是否一次收取，1为按月收取，2为一次收取]")
    private Integer isOneTimeCharge;
    /**
     * 放款去处 1:提到银行卡  2：转到可用金额
     */
    @ApiModelProperty(required= true,value = "放款去处 1:提到银行卡  2：转到可用金额")
    private Integer withdrawPlace;
    /**
     * [标记该项费用是否单独收取，null或0:不单独收取，1:单独收取]
     */
    @ApiModelProperty(required= true,value = "[标记该项费用是否单独收取，null或0:不单独收取，1:单独收取]")
    private Integer outputFlag;
    /**
     * [标记该项费用的还款类型，1:期初收取,2:期末收取]
     */
    @ApiModelProperty(required= true,value = "[标记该项费用的还款类型，1:期初收取,2:期末收取]")
    private Integer repaymentFlag;
    /**
     * [是否P2P主标收取,1为是，0为否]
     */
    @ApiModelProperty(required= true,value = "[是否P2P主标收取,1为是，0为否]")
    private Integer isP2PMainmarkCollect;
    /**
     * [利率,1为年利率，2为月利率，3为日利率]
     */
    @ApiModelProperty(required= true,value = "[利率,1为年利率，2为月利率，3为日利率]")
    private Integer interestRate;
//    /**
//     * [是否允许修改,1为是，0为否]
//     */
//    @ApiModelProperty(required= true,value = "[是否允许修改,1为是，0为否]")
//    private Integer isAllowModify;
    /**
     * [是否可记为收入,1为是，0为否]
     */
    @ApiModelProperty(required= true,value = "[是否可记为收入,1为是，0为否]")
    private Integer isRecordIncome;
//    /**
//     * [费用是否可退,0或null表示不可退，1表示可退]
//     */
//    @ApiModelProperty(required= true,value = "[费用是否可退,0或null表示不可退，1表示可退]")
//    private Integer canRefund;

//    /**
//     * [可退剩余金额]
//     */
//    @ApiModelProperty(required= true,value = "[可退剩余金额]")
//    private BigDecimal canRefundMoney;



    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

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

//    public String getCreateUser() {
//        return createUser;
//    }
//
//    public void setCreateUser(String createUser) {
//        this.createUser = createUser;
//    }
//
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getUpdateUser() {
//        return updateUser;
//    }
//
//    public void setUpdateUser(String updateUser) {
//        this.updateUser = updateUser;
//    }
//
//    public Date getUpdateTime() {
//        return updateTime;
//    }
//
//    public void setUpdateTime(Date updateTime) {
//        this.updateTime = updateTime;
//    }

//    public Integer getRefundStatus() {
//        return RefundStatus;
//    }
//
//    public void setRefundStatus(Integer refundStatus) {
//        RefundStatus = refundStatus;
//    }

//    public String getRefundRemark() {
//        return RefundRemark;
//    }
//
//    public void setRefundRemark(String refundRemark) {
//        RefundRemark = refundRemark;
//    }
//
//    public String getRefundInfoId() {
//        return refundInfoId;
//    }
//
//    public void setRefundInfoId(String refundInfoId) {
//        this.refundInfoId = refundInfoId;
//    }

    public Integer getWithdrawId() {
        return WithdrawId;
    }

    public void setWithdrawId(Integer withdrawId) {
        WithdrawId = withdrawId;
    }

    public Integer getFeeChargingType() {
        return feeChargingType;
    }

    public void setFeeChargingType(Integer feeChargingType) {
        this.feeChargingType = feeChargingType;
    }

    public Integer getIsOneTimeCharge() {
        return isOneTimeCharge;
    }

    public void setIsOneTimeCharge(Integer isOneTimeCharge) {
        this.isOneTimeCharge = isOneTimeCharge;
    }

    public Integer getWithdrawPlace() {
        return withdrawPlace;
    }

    public void setWithdrawPlace(Integer withdrawPlace) {
        this.withdrawPlace = withdrawPlace;
    }

    public Integer getOutputFlag() {
        return outputFlag;
    }

    public void setOutputFlag(Integer outputFlag) {
        this.outputFlag = outputFlag;
    }

    public Integer getRepaymentFlag() {
        return repaymentFlag;
    }

    public void setRepaymentFlag(Integer repaymentFlag) {
        this.repaymentFlag = repaymentFlag;
    }

    public Integer getIsP2PMainmarkCollect() {
        return isP2PMainmarkCollect;
    }

    public void setIsP2PMainmarkCollect(Integer isP2PMainmarkCollect) {
        this.isP2PMainmarkCollect = isP2PMainmarkCollect;
    }

    public Integer getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Integer interestRate) {
        this.interestRate = interestRate;
    }

//    public Integer getIsAllowModify() {
//        return isAllowModify;
//    }
//
//    public void setIsAllowModify(Integer isAllowModify) {
//        this.isAllowModify = isAllowModify;
//    }

    public Integer getIsRecordIncome() {
        return isRecordIncome;
    }

    public void setIsRecordIncome(Integer isRecordIncome) {
        this.isRecordIncome = isRecordIncome;
    }

//    public Integer getCanRefund() {
//        return canRefund;
//    }
//
//    public void setCanRefund(Integer canRefund) {
//        this.canRefund = canRefund;
//    }

    public Integer getIsTermRange() {
        return isTermRange;
    }

    public void setIsTermRange(Integer isTermRange) {
        this.isTermRange = isTermRange;
    }

//    public BigDecimal getCanRefundMoney() {
//        return canRefundMoney;
//    }
//
//    public void setCanRefundMoney(BigDecimal canRefundMoney) {
//        this.canRefundMoney = canRefundMoney;
//    }

    public BigDecimal getNewSystemDefaultRate() {
        return newSystemDefaultRate;
    }

    public void setNewSystemDefaultRate(BigDecimal newSystemDefaultRate) {
        this.newSystemDefaultRate = newSystemDefaultRate;
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


    public List<ProjFeeDetailReq> getFeeDetailReqMap() {
        return feeDetailReqMap;
    }

    public void setFeeDetailReqMap(List<ProjFeeDetailReq> feeDetailReqMap) {
        this.feeDetailReqMap = feeDetailReqMap;
    }
}
