package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 还款调用合规化还款记录表
 * </p>
 *
 * @author lxq
 * @since 2018-07-13
 */
@ApiModel
@TableName("tb_repayment_confirm_plat_repay_log")
public class RepaymentConfirmPlatRepayLog extends Model<RepaymentConfirmPlatRepayLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键Id")
	private Integer id;
    /**
     * 还款确认日志主键ID  对应 tb_repayment_confirm_log.confirm_log_id
     */
	@TableField("confirm_log_id")
	@ApiModelProperty(required= true,value = "还款确认日志主键ID  对应 tb_repayment_confirm_log.confirm_log_id")
	private String confirmLogId;
    /**
     * 标还款计划列表主键 对应tb_repayment_proj_plan_list.proj_plan_list_id
     */
	@TableField("proj_plan_list_id")
	@ApiModelProperty(required= true,value = "标还款计划列表主键 对应tb_repayment_proj_plan_list.proj_plan_list_id")
	private String projPlanListId;
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


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConfirmLogId() {
		return confirmLogId;
	}

	public void setConfirmLogId(String confirmLogId) {
		this.confirmLogId = confirmLogId;
	}

	public String getProjPlanListId() {
		return projPlanListId;
	}

	public void setProjPlanListId(String projPlanListId) {
		this.projPlanListId = projPlanListId;
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
		return this.id;
	}

	@Override
	public String toString() {
		return "RepaymentConfirmPlatRepayLog{" +
			", id=" + id +
			", confirmLogId=" + confirmLogId +
			", projPlanListId=" + projPlanListId +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
