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
 * API调用失败记录表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-26
 */
@ApiModel
@TableName("tb_sys_api_call_failure_record")
public class SysApiCallFailureRecord extends Model<SysApiCallFailureRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键ID")
	private Long id;
    /**
     * 所属模块
     */
	@TableField("module_name")
	@ApiModelProperty(required= true,value = "所属模块")
	private String moduleName;
    /**
     * 接口名
     */
	@TableField("api_name")
	@ApiModelProperty(required= true,value = "接口名")
	private String apiName;
    /**
     * 接口编码
     */
	@TableField("api_code")
	@ApiModelProperty(required= true,value = "接口编码")
	private String apiCode;
    /**
     * 关联标识
     */
	@TableField("ref_id")
	@ApiModelProperty(required= true,value = "关联标识")
	private String refId;
    /**
     * 接口参数明文串
     */
	@TableField("api_param_plaintext")
	@ApiModelProperty(required= true,value = "接口参数明文串")
	private String apiParamPlaintext;
    /**
     * 接口参数密文串
     */
	@TableField("api_param_ciphertext")
	@ApiModelProperty(required= true,value = "接口参数密文串")
	private String apiParamCiphertext;
    /**
     * 接口返回信息
     */
	@TableField("api_return_info")
	@ApiModelProperty(required= true,value = "接口返回信息")
	private String apiReturnInfo;
    /**
     * 接口url
     */
	@TableField("target_url")
	@ApiModelProperty(required= true,value = "接口url")
	private String targetUrl;
    /**
     * 重试次数
     */
	@TableField("retry_count")
	@ApiModelProperty(required= true,value = "重试次数")
	private Integer retryCount;
    /**
     * 上次重试时间
     */
	@TableField("retry_time")
	@ApiModelProperty(required= true,value = "上次重试时间")
	private Date retryTime;
    /**
     * 是否重试成功
     */
	@TableField("retry_success")
	@ApiModelProperty(required= true,value = "是否重试成功")
	private Integer retrySuccess;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
    /**
     * 创建时间
     */
	@TableField("craete_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date craeteTime;
    /**
     * 修改人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "修改人")
	private String updateUser;
    /**
     * 修改时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "修改时间")
	private Date updateTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiCode() {
		return apiCode;
	}

	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getApiParamPlaintext() {
		return apiParamPlaintext;
	}

	public void setApiParamPlaintext(String apiParamPlaintext) {
		this.apiParamPlaintext = apiParamPlaintext;
	}

	public String getApiParamCiphertext() {
		return apiParamCiphertext;
	}

	public void setApiParamCiphertext(String apiParamCiphertext) {
		this.apiParamCiphertext = apiParamCiphertext;
	}

	public String getApiReturnInfo() {
		return apiReturnInfo;
	}

	public void setApiReturnInfo(String apiReturnInfo) {
		this.apiReturnInfo = apiReturnInfo;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public Date getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(Date retryTime) {
		this.retryTime = retryTime;
	}

	public Integer getRetrySuccess() {
		return retrySuccess;
	}

	public void setRetrySuccess(Integer retrySuccess) {
		this.retrySuccess = retrySuccess;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCraeteTime() {
		return craeteTime;
	}

	public void setCraeteTime(Date craeteTime) {
		this.craeteTime = craeteTime;
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
		return "SysApiCallFailureRecord{" +
			", id=" + id +
			", moduleName=" + moduleName +
			", apiName=" + apiName +
			", apiCode=" + apiCode +
			", refId=" + refId +
			", apiParamPlaintext=" + apiParamPlaintext +
			", apiParamCiphertext=" + apiParamCiphertext +
			", apiReturnInfo=" + apiReturnInfo +
			", targetUrl=" + targetUrl +
			", retryCount=" + retryCount +
			", retryTime=" + retryTime +
			", retrySuccess=" + retrySuccess +
			", createUser=" + createUser +
			", craeteTime=" + craeteTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			"}";
	}
}
