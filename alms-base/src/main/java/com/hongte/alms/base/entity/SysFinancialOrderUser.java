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
 * 财务跟单设置人员列表
 * </p>
 *
 * @author 曾坤
 * @since 2018-06-10
 */
@ApiModel
@TableName("tb_sys_financial_order_user")
public class SysFinancialOrderUser extends Model<SysFinancialOrderUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 自增
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键 自增")
	private Integer id;
    /**
     * 财务人员跟单设置主键ID（对应 tb_sys_financial_order 表主键） 
     */
	@TableField("finance_order_id")
	@ApiModelProperty(required= true,value = "财务人员跟单设置主键ID（对应 tb_sys_financial_order 表主键） ")
	private Integer financeOrderId;
    /**
     * 跟单人员用户Id
     */
	@TableField("user_id")
	@ApiModelProperty(required= true,value = "跟单人员用户Id")
	private String userId;
    /**
     * 跟单人员用户名
     */
	@TableField("user_name")
	@ApiModelProperty(required= true,value = "跟单人员用户名")
	private String userName;
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


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFinanceOrderId() {
		return financeOrderId;
	}

	public void setFinanceOrderId(Integer financeOrderId) {
		this.financeOrderId = financeOrderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysFinancialOrderUser{" +
			", id=" + id +
			", financeOrderId=" + financeOrderId +
			", userId=" + userId +
			", userName=" + userName +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			", createUser=" + createUser +
			", createTime=" + createTime +
			"}";
	}
}
