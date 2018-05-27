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
 * 还款来源表
 * </p>
 *
 * @author 王继光
 * @since 2018-05-14
 */
@ApiModel
@TableName("tb_repayment_resource")
public class RepaymentResource extends Model<RepaymentResource> {

    private static final long serialVersionUID = 1L;

    /**
     * 还款来源id,主键
     */
    @TableId("resource_id")
	@ApiModelProperty(required= true,value = "还款来源id,主键")
	private String resourceId;
    /**
     * 业务id
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务id")
	private String businessId;
    /**
     * 原业务id
     */
	@TableField("org_business_id")
	@ApiModelProperty(required= true,value = "原业务id")
	private String orgBusinessId;
    /**
     * 期数id
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "期数id")
	private String afterId;
    /**
     * 还款日期
     */
	@TableField("repay_date")
	@ApiModelProperty(required= true,value = "还款日期")
	private Date repayDate;
    /**
     * 还款金额,单位:元
     */
	@TableField("repay_amount")
	@ApiModelProperty(required= true,value = "还款金额,单位:元")
	private BigDecimal repayAmount;
    /**
     * 还款来源类型,外键,参考tb_sys_parameter,还款来源的记录
     */
	@TableField("repay_source")
	@ApiModelProperty(required= true,value = "还款来源类型,外键,参考tb_sys_parameter,还款来源的记录")
	private String repaySource;
    /**
     * 还款来源关联的记录ID
     */
	@TableField("repay_source_ref_id")
	@ApiModelProperty(required= true,value = "还款来源关联的记录ID")
	private String repaySourceRefId;
    /**
     * 是否已撤销,1=已撤销,0=正常
     */
	@TableField("is_cancelled")
	@ApiModelProperty(required= true,value = "是否已撤销,1=已撤销,0=正常")
	private Integer isCancelled;
    /**
     * 记录创建人id,操作人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "记录创建人id,操作人")
	private String createUser;
    /**
     * 创建日期
     */
	@TableField("create_date")
	@ApiModelProperty(required= true,value = "创建日期")
	private Date createDate;
    /**
     * 记录更新人id,更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "记录更新人id,更新人")
	private String updateUser;
    /**
     * 记录更新时间
     */
	@TableField("udpate_time")
	@ApiModelProperty(required= true,value = "记录更新时间")
	private Date udpateTime;
	
	/**
	 * 被分配到的线上滞纳金,用于财务确认分润
	 */
	@TableField(exist=false)
	private BigDecimal dOfflineOverDue ;
	/**
	 * 被分配到的线下滞纳金,用于财务确认分润
	 */
	@TableField(exist=false)
	private BigDecimal dOnlineOverDue ;


	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOrgBusinessId() {
		return orgBusinessId;
	}

	public void setOrgBusinessId(String orgBusinessId) {
		this.orgBusinessId = orgBusinessId;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public Date getRepayDate() {
		return repayDate;
	}

	public void setRepayDate(Date repayDate) {
		this.repayDate = repayDate;
	}

	public BigDecimal getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(BigDecimal repayAmount) {
		this.repayAmount = repayAmount;
	}

	public String getRepaySource() {
		return repaySource;
	}

	public void setRepaySource(String repaySource) {
		this.repaySource = repaySource;
	}

	public String getRepaySourceRefId() {
		return repaySourceRefId;
	}

	public void setRepaySourceRefId(String repaySourceRefId) {
		this.repaySourceRefId = repaySourceRefId;
	}

	public Integer getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Integer isCancelled) {
		this.isCancelled = isCancelled;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUdpateTime() {
		return udpateTime;
	}

	public void setUdpateTime(Date udpateTime) {
		this.udpateTime = udpateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.resourceId;
	}

	@Override
	public String toString() {
		return "RepaymentResource{" +
			", resourceId=" + resourceId +
			", businessId=" + businessId +
			", orgBusinessId=" + orgBusinessId +
			", afterId=" + afterId +
			", repayDate=" + repayDate +
			", repayAmount=" + repayAmount +
			", repaySource=" + repaySource +
			", repaySourceRefId=" + repaySourceRefId +
			", isCancelled=" + isCancelled +
			", createUser=" + createUser +
			", createDate=" + createDate +
			", updateUser=" + updateUser +
			", udpateTime=" + udpateTime +
			"}";
	}

	/**
	 * @return the dOfflineOverDue
	 */
	public BigDecimal getdOfflineOverDue() {
		return dOfflineOverDue;
	}

	/**
	 * @param dOfflineOverDue the dOfflineOverDue to set
	 */
	public void setdOfflineOverDue(BigDecimal dOfflineOverDue) {
		this.dOfflineOverDue = dOfflineOverDue;
	}

	/**
	 * @return the dOnlineOverDue
	 */
	public BigDecimal getdOnlineOverDue() {
		return dOnlineOverDue;
	}

	/**
	 * @param dOnlineOverDue the dOnlineOverDue to set
	 */
	public void setdOnlineOverDue(BigDecimal dOnlineOverDue) {
		this.dOnlineOverDue = dOnlineOverDue;
	}
}
