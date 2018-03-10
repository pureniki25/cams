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
 * api对接配置
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-01
 */
@ApiModel
@TableName("tb_api")
public class Api extends Model<Api> {

    private static final long serialVersionUID = 1L;

    /**
     * API接口配置ID
     */
    @TableId("api_id")
	@ApiModelProperty(required= true,value = "API接口配置ID")
	private String apiId;
    /**
     * api类型
     */
	@ApiModelProperty(required= true,value = "api类型")
	private String apikey;
    /**
     * api类型名称
     */
	@ApiModelProperty(required= true,value = "api类型名称")
	private String apiname;
    /**
     * token值
     */
	@ApiModelProperty(required= true,value = "token值")
	private String token;
    /**
     * 开发用户
     */
	@ApiModelProperty(required= true,value = "开发用户")
	private String developname;
    /**
     * api类型
     */
	@TableField("api_type")
	@ApiModelProperty(required= true,value = "api类型")
	private String apiType;
    /**
     * 启用状态 0不启用  1启用
     */
	@ApiModelProperty(required= true,value = "启用状态 0不启用  1启用")
	private Integer status;
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


	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getApiname() {
		return apiname;
	}

	public void setApiname(String apiname) {
		this.apiname = apiname;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDevelopname() {
		return developname;
	}

	public void setDevelopname(String developname) {
		this.developname = developname;
	}

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
		return this.apiId;
	}

	@Override
	public String toString() {
		return "Api{" +
			", apiId=" + apiId +
			", apikey=" + apikey +
			", apiname=" + apiname +
			", token=" + token +
			", developname=" + developname +
			", apiType=" + apiType +
			", status=" + status +
			", createUser=" + createUser +
			", createTime=" + createTime +
			"}";
	}
}
