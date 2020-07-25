package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 用户表
 * </p>
 *
 * @author wjg
 * @since 2018-12-31
 */
@ApiModel
@TableName("tb_login_user")
@Data
public class LoginUser extends Model<LoginUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
	@ApiModelProperty(required= true,value = "用户ID")
	private String id;
    /**
     * 登陆用户名
     */
	@TableField("user_name")
	@ApiModelProperty(required= true,value = "登陆用户名")
	private String userName;
    /**
     * 密码
     */
	@TableField("user_password")
	@ApiModelProperty(required= true,value = "密码")
	private String userPassword;
	
    /**
     * 令牌
     */
	@TableField("token")
	@ApiModelProperty(required= true,value = "令牌")
	private String token;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;

	@TableField("company_name")
	@ApiModelProperty(required= true,value = "公司名称")
	private String companyName;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
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
		return "LoginUser{" +
			", id=" + id +
			", userName=" + userName +
			", userPassword=" + userPassword +
			", createTime=" + createTime +
			"}";
	}
}
