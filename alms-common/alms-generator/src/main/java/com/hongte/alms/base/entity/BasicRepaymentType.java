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
 * 还款方式配置表
 * </p>
 *
 * @author 王继光
 * @since 2018-03-10
 */
@ApiModel
@TableName("tb_basic_repayment_type")
public class BasicRepaymentType extends Model<BasicRepaymentType> {

    private static final long serialVersionUID = 1L;

    /**
     * 还款方式id
     */
    @TableId("repayment_type_id")
	@ApiModelProperty(required= true,value = "还款方式id")
	private Integer repaymentTypeId;
    /**
     * 还款方式名称
     */
	@TableField("repayment_type_name")
	@ApiModelProperty(required= true,value = "还款方式名称")
	private String repaymentTypeName;
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


	public Integer getRepaymentTypeId() {
		return repaymentTypeId;
	}

	public void setRepaymentTypeId(Integer repaymentTypeId) {
		this.repaymentTypeId = repaymentTypeId;
	}

	public String getRepaymentTypeName() {
		return repaymentTypeName;
	}

	public void setRepaymentTypeName(String repaymentTypeName) {
		this.repaymentTypeName = repaymentTypeName;
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
		return this.repaymentTypeId;
	}

	@Override
	public String toString() {
		return "BasicRepaymentType{" +
			", repaymentTypeId=" + repaymentTypeId +
			", repaymentTypeName=" + repaymentTypeName +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
