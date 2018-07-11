package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 代充值操作记录表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-07-09
 */
@ApiModel
@TableName("tb_agency_recharge_log")
public class AgencyRechargeLog extends Model<AgencyRechargeLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键ID")
	private Integer id;
    /**
     * 原业务编号
     */
	@TableField("orig_business_id")
	@ApiModelProperty(required= true,value = "原业务编号")
	private String origBusinessId;
    /**
     * 资产端期数
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "资产端期数")
	private String afterId;
    /**
     * (代充值参数)代充值账户类型
     */
	@TableField("recharge_account_type")
	@ApiModelProperty(required= true,value = "(代充值参数)代充值账户类型")
	private String rechargeAccountType;
    /**
     * (代充值参数)转账类型(1：对公；2：对私)
     */
	@TableField("transfer_type")
	@ApiModelProperty(required= true,value = "(代充值参数)转账类型(1：对公；2：对私)")
	private String transferType;
    /**
     * (代充值参数)充值金额（元）
     */
	@TableField("recharge_amount")
	@ApiModelProperty(required= true,value = "(代充值参数)充值金额（元）")
	private BigDecimal rechargeAmount;
    /**
     * (代充值参数)充值来源账户
     */
	@TableField("recharge_sourse_account")
	@ApiModelProperty(required= true,value = "(代充值参数)充值来源账户")
	private String rechargeSourseAccount;
    /**
     * (代充值参数)银行编码
     */
	@TableField("bank_code")
	@ApiModelProperty(required= true,value = "(代充值参数)银行编码")
	private String bankCode;
    /**
     * 充值来源账户卡号
     */
	@TableField("bank_account")
	@ApiModelProperty(required= true,value = "充值来源账户卡号")
	private String bankAccount;
    /**
     * 代充值账户余额
     */
	@TableField("recharge_account_balance")
	@ApiModelProperty(required= true,value = "代充值账户余额")
	private BigDecimal rechargeAccountBalance;
    /**
     * (调用外联平台接口参数)由资产端生成，作为后续查询的一个标识
     */
	@TableField("cm_order_no")
	@ApiModelProperty(required= true,value = "(调用外联平台接口参数)由资产端生成，作为后续查询的一个标识")
	private String cmOrderNo;
    /**
     * (调用外联平台接口参数)团贷分配，商户唯一号(测试，生产不一样)
     */
	@TableField("oId_partner")
	@ApiModelProperty(required= true,value = "(调用外联平台接口参数)团贷分配，商户唯一号(测试，生产不一样)")
	private String oIdPartner;
    /**
     * (调用外联平台接口参数)代充值账户userID
     */
	@TableField("recharge_user_id")
	@ApiModelProperty(required= true,value = "(调用外联平台接口参数)代充值账户userID")
	private String rechargeUserId;
    /**
     * (调用外联平台接口参数)1：网关、2：快捷、3：代充值
     */
	@TableField("charge_type")
	@ApiModelProperty(required= true,value = "(调用外联平台接口参数)1：网关、2：快捷、3：代充值")
	private String chargeType;
    /**
     * (调用外联平台接口参数)用户IP
     */
	@TableField("client_ip")
	@ApiModelProperty(required= true,value = "(调用外联平台接口参数)用户IP")
	private String clientIp;
    /**
     * 1处理中，2成功，3失败
     */
	@TableField("handle_status")
	@ApiModelProperty(required= true,value = "1处理中，2成功，3失败")
	private String handleStatus;
    /**
     * 请求参数JSON
     */
	@TableField("param_json")
	@ApiModelProperty(required= true,value = "请求参数JSON")
	private String paramJson;
    /**
     * 返回结果JSON,若调用接口异常，则保存异常信息
     */
	@TableField("result_json")
	@ApiModelProperty(required= true,value = "返回结果JSON,若调用接口异常，则保存异常信息")
	private String resultJson;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrigBusinessId() {
		return origBusinessId;
	}

	public void setOrigBusinessId(String origBusinessId) {
		this.origBusinessId = origBusinessId;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public String getRechargeAccountType() {
		return rechargeAccountType;
	}

	public void setRechargeAccountType(String rechargeAccountType) {
		this.rechargeAccountType = rechargeAccountType;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public BigDecimal getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(BigDecimal rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public String getRechargeSourseAccount() {
		return rechargeSourseAccount;
	}

	public void setRechargeSourseAccount(String rechargeSourseAccount) {
		this.rechargeSourseAccount = rechargeSourseAccount;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public BigDecimal getRechargeAccountBalance() {
		return rechargeAccountBalance;
	}

	public void setRechargeAccountBalance(BigDecimal rechargeAccountBalance) {
		this.rechargeAccountBalance = rechargeAccountBalance;
	}

	public String getCmOrderNo() {
		return cmOrderNo;
	}

	public void setCmOrderNo(String cmOrderNo) {
		this.cmOrderNo = cmOrderNo;
	}

	public String getoIdPartner() {
		return oIdPartner;
	}

	public void setoIdPartner(String oIdPartner) {
		this.oIdPartner = oIdPartner;
	}

	public String getRechargeUserId() {
		return rechargeUserId;
	}

	public void setRechargeUserId(String rechargeUserId) {
		this.rechargeUserId = rechargeUserId;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}

	public String getParamJson() {
		return paramJson;
	}

	public void setParamJson(String paramJson) {
		this.paramJson = paramJson;
	}

	public String getResultJson() {
		return resultJson;
	}

	public void setResultJson(String resultJson) {
		this.resultJson = resultJson;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "AgencyRechargeLog{" +
			", id=" + id +
			", origBusinessId=" + origBusinessId +
			", afterId=" + afterId +
			", rechargeAccountType=" + rechargeAccountType +
			", transferType=" + transferType +
			", rechargeAmount=" + rechargeAmount +
			", rechargeSourseAccount=" + rechargeSourseAccount +
			", bankCode=" + bankCode +
			", bankAccount=" + bankAccount +
			", rechargeAccountBalance=" + rechargeAccountBalance +
			", cmOrderNo=" + cmOrderNo +
			", oIdPartner=" + oIdPartner +
			", rechargeUserId=" + rechargeUserId +
			", chargeType=" + chargeType +
			", clientIp=" + clientIp +
			", handleStatus=" + handleStatus +
			", paramJson=" + paramJson +
			", resultJson=" + resultJson +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			"}";
	}
}
