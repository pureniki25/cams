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
 * 业务展期续借信息表
 * </p>
 *
 * @author 王继光
 * @since 2018-03-06
 */
@ApiModel
@TableName("tb_renewal_business")
public class RenewalBusiness extends Model<RenewalBusiness> {

    private static final long serialVersionUID = 1L;

    /**
     * 展期业务编号
     */
    @TableId("renewal_business_id")
	@ApiModelProperty(required= true,value = "展期业务编号")
	private String renewalBusinessId;
    /**
     * 展期对应的原业务编号
     */
	@TableField("original_business_id")
	@ApiModelProperty(required= true,value = "展期对应的原业务编号")
	private String originalBusinessId;
    /**
     * 当前申请展期的上一次展期编号，若当前为第一次展期则存原业务编号
     */
	@TableField("last_business_id")
	@ApiModelProperty(required= true,value = "当前申请展期的上一次展期编号，若当前为第一次展期则存原业务编号")
	private String lastBusinessId;
    /**
     * 展期备注
     */
	@ApiModelProperty(required= true,value = "展期备注")
	private String remark;
    /**
     * 展期其他费用说明
     */
	@TableField("other_cost_remark")
	@ApiModelProperty(required= true,value = "展期其他费用说明")
	private String otherCostRemark;
    /**
     * 是否减免逾期 0：不减免 1:减免
     */
	@TableField("is_deduce_overdue")
	@ApiModelProperty(required= true,value = "是否减免逾期 0：不减免 1:减免")
	private Integer isDeduceOverdue;
    /**
     * 减免原因
     */
	@TableField("deduce_overdue_remark")
	@ApiModelProperty(required= true,value = "减免原因")
	private String deduceOverdueRemark;
    /**
     * 减免后逾期费用
     */
	@TableField("deduce_after_overdue_money")
	@ApiModelProperty(required= true,value = "减免后逾期费用")
	private BigDecimal deduceAfterOverdueMoney;
    /**
     * 1合同确定，0是合同未确定
     */
	@TableField("contract_confirm")
	@ApiModelProperty(required= true,value = "1合同确定，0是合同未确定")
	private Integer contractConfirm;
    /**
     * 当前流程状态
     */
	@TableField("current_process_name")
	@ApiModelProperty(required= true,value = "当前流程状态")
	private String currentProcessName;
    /**
     * 创建日期
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建日期")
	private Date createTime;
    /**
     * 创建用户
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户")
	private String createUser;
    /**
     * 更新日期
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新日期")
	private Date updateTime;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;


	public String getRenewalBusinessId() {
		return renewalBusinessId;
	}

	public void setRenewalBusinessId(String renewalBusinessId) {
		this.renewalBusinessId = renewalBusinessId;
	}

	public String getOriginalBusinessId() {
		return originalBusinessId;
	}

	public void setOriginalBusinessId(String originalBusinessId) {
		this.originalBusinessId = originalBusinessId;
	}

	public String getLastBusinessId() {
		return lastBusinessId;
	}

	public void setLastBusinessId(String lastBusinessId) {
		this.lastBusinessId = lastBusinessId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOtherCostRemark() {
		return otherCostRemark;
	}

	public void setOtherCostRemark(String otherCostRemark) {
		this.otherCostRemark = otherCostRemark;
	}

	public Integer getIsDeduceOverdue() {
		return isDeduceOverdue;
	}

	public void setIsDeduceOverdue(Integer isDeduceOverdue) {
		this.isDeduceOverdue = isDeduceOverdue;
	}

	public String getDeduceOverdueRemark() {
		return deduceOverdueRemark;
	}

	public void setDeduceOverdueRemark(String deduceOverdueRemark) {
		this.deduceOverdueRemark = deduceOverdueRemark;
	}

	public BigDecimal getDeduceAfterOverdueMoney() {
		return deduceAfterOverdueMoney;
	}

	public void setDeduceAfterOverdueMoney(BigDecimal deduceAfterOverdueMoney) {
		this.deduceAfterOverdueMoney = deduceAfterOverdueMoney;
	}

	public Integer getContractConfirm() {
		return contractConfirm;
	}

	public void setContractConfirm(Integer contractConfirm) {
		this.contractConfirm = contractConfirm;
	}

	public String getCurrentProcessName() {
		return currentProcessName;
	}

	public void setCurrentProcessName(String currentProcessName) {
		this.currentProcessName = currentProcessName;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	protected Serializable pkVal() {
		return this.renewalBusinessId;
	}

	@Override
	public String toString() {
		return "RenewalBusiness{" +
			", renewalBusinessId=" + renewalBusinessId +
			", originalBusinessId=" + originalBusinessId +
			", lastBusinessId=" + lastBusinessId +
			", remark=" + remark +
			", otherCostRemark=" + otherCostRemark +
			", isDeduceOverdue=" + isDeduceOverdue +
			", deduceOverdueRemark=" + deduceOverdueRemark +
			", deduceAfterOverdueMoney=" + deduceAfterOverdueMoney +
			", contractConfirm=" + contractConfirm +
			", currentProcessName=" + currentProcessName +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
