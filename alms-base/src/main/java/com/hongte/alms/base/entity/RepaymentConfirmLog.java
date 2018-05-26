package com.hongte.alms.base.entity;

import java.io.Serializable;

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
 * 还款确认日志记录表
 * </p>
 *
 * @author 王继光
 * @since 2018-05-25
 */
@ApiModel
@TableName("tb_repayment_confirm_log")
public class RepaymentConfirmLog extends Model<RepaymentConfirmLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 还款确认日志主键ID
     */
    @TableId("confirm_log_id")
	@ApiModelProperty(required= true,value = "还款确认日志主键ID")
	private String confirmLogId;
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
     * 期数
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "期数")
	private String afterId;
    /**
     * 日志序号,表示当前是第几次还款确认
     */
	@ApiModelProperty(required= true,value = "日志序号,表示当前是第几次还款确认")
	private Integer index;
    /**
     * 能否撤销,代扣和资金分发后的不能撤销
     */
	@TableField("can_revoke")
	@ApiModelProperty(required= true,value = "能否撤销,代扣和资金分发后的不能撤销")
	private Integer canRevoke;
    /**
     * 标的还款分润后的镜像
     */
	@TableField("proj_plan_json")
	@ApiModelProperty(required= true,value = "标的还款分润后的镜像")
	private String projPlanJson;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
	/**
     * 创建人
     */
	@TableField("create_user_name")
	@ApiModelProperty(required= true)
	private String createUserName;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;


	public String getConfirmLogId() {
		return confirmLogId;
	}

	public void setConfirmLogId(String confirmLogId) {
		this.confirmLogId = confirmLogId;
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

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getCanRevoke() {
		return canRevoke;
	}

	public void setCanRevoke(Integer canRevoke) {
		this.canRevoke = canRevoke;
	}

	public String getProjPlanJson() {
		return projPlanJson;
	}

	public void setProjPlanJson(String projPlanJson) {
		this.projPlanJson = projPlanJson;
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
		return this.confirmLogId;
	}

	@Override
	public String toString() {
		return "RepaymentConfirmLog{" +
			", confirmLogId=" + confirmLogId +
			", businessId=" + businessId +
			", orgBusinessId=" + orgBusinessId +
			", afterId=" + afterId +
			", index=" + index +
			", canRevoke=" + canRevoke +
			", projPlanJson=" + projPlanJson +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
}
