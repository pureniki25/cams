package com.hongte.alms.base.entity;

import java.io.Serializable;

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
 * 渤海还款计划推送日志
 * </p>
 *
 * @author wjg
 * @since 2018-12-11
 */
@ApiModel
@TableName("tb_bh_repay_plan_push_log")
public class BhRepayPlanPushLog extends Model<BhRepayPlanPushLog> {

    private static final long serialVersionUID = 1L;

    @TableId("log_id")
	@ApiModelProperty(required= true,value = "")
	private String logId;
    /**
     * 渤海外部流水号
     */
	@TableField("out_trade_no")
	@ApiModelProperty(required= true,value = "渤海外部流水号")
	private String outTradeNo;
    /**
     * contractNo
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "contractNo")
	private String businessId;
    /**
     * 01=部分提前还款,02=利率调整,03=还款方式变更,04=其他(新增、费用变更等),05=展期,06=缩期
     */
	@ApiModelProperty(required= true,value = "01=部分提前还款,02=利率调整,03=还款方式变更,04=其他(新增、费用变更等),05=展期,06=缩期")
	private String reason;
    /**
     * 本金变动金额,本金没变化填0
     */
	@ApiModelProperty(required= true,value = "本金变动金额,本金没变化填0")
	private BigDecimal sum;
    /**
     * 业务发生日期
     */
	@TableField("occur_date")
	@ApiModelProperty(required= true,value = "业务发生日期")
	private Date occurDate;
    /**
     * 还款计划json字符串
     */
	@TableField("plan_json_str")
	@ApiModelProperty(required= true,value = "还款计划json字符串")
	private String planJsonStr;
    /**
     * 日志创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "日志创建时间")
	private Date createTime;
    /**
     * 日志创建者
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "日志创建者")
	private String createUser;
    /**
     * 推送时间
     */
	@TableField("push_time")
	@ApiModelProperty(required= true,value = "推送时间")
	private Date pushTime;
    /**
     * -1=未推送,0=推送中,1=已推送
     */
	@TableField("push_status")
	@ApiModelProperty(required= true,value = "-1=未推送,0=推送中,1=已推送")
	private Integer pushStatus;
    /**
     * 推送过程发生异常记录于此
     */
	@TableField("push_remark")
	@ApiModelProperty(required= true,value = "推送过程发生异常记录于此")
	private String pushRemark;
    /**
     * 0=渤海处理中,1=渤海处理成功,2=渤海处理失败
     */
	@TableField("return_code")
	@ApiModelProperty(required= true,value = "0=渤海处理中,1=渤海处理成功,2=渤海处理失败")
	private Integer returnCode;
    /**
     * 渤海接口返回的内容
     */
	@TableField("return_json_str")
	@ApiModelProperty(required= true,value = "渤海接口返回的内容")
	private String returnJsonStr;
    /**
     * 上一个日志,null=无上一个日志
     */
	@TableField("pre_log_id")
	@ApiModelProperty(required= true,value = "上一个日志,null=无上一个日志")
	private String preLogId;
    /**
     * 是否生效,0=失效,1=生效
     */
	@TableField("is_active")
	@ApiModelProperty(required= true,value = "是否生效,0=失效,1=生效")
	private Integer isActive;


	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public Date getOccurDate() {
		return occurDate;
	}

	public void setOccurDate(Date occurDate) {
		this.occurDate = occurDate;
	}

	public String getPlanJsonStr() {
		return planJsonStr;
	}

	public void setPlanJsonStr(String planJsonStr) {
		this.planJsonStr = planJsonStr;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	public Integer getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Integer pushStatus) {
		this.pushStatus = pushStatus;
	}

	public String getPushRemark() {
		return pushRemark;
	}

	public void setPushRemark(String pushRemark) {
		this.pushRemark = pushRemark;
	}

	public Integer getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(Integer returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnJsonStr() {
		return returnJsonStr;
	}

	public void setReturnJsonStr(String returnJsonStr) {
		this.returnJsonStr = returnJsonStr;
	}

	public String getPreLogId() {
		return preLogId;
	}

	public void setPreLogId(String preLogId) {
		this.preLogId = preLogId;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	@Override
	protected Serializable pkVal() {
		return this.logId;
	}

	@Override
	public String toString() {
		return "BhRepayPlanPushLog{" +
			", logId=" + logId +
			", outTradeNo=" + outTradeNo +
			", businessId=" + businessId +
			", reason=" + reason +
			", sum=" + sum +
			", occurDate=" + occurDate +
			", planJsonStr=" + planJsonStr +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", pushTime=" + pushTime +
			", pushStatus=" + pushStatus +
			", pushRemark=" + pushRemark +
			", returnCode=" + returnCode +
			", returnJsonStr=" + returnJsonStr +
			", preLogId=" + preLogId +
			", isActive=" + isActive +
			"}";
	}
}
