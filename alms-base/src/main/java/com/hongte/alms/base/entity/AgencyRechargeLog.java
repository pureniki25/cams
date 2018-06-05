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
 * 代充值操作记录表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-05
 */
@ApiModel
@TableName("tb_agency_recharge_log")
public class AgencyRechargeLog extends Model<AgencyRechargeLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键ID")
	private Integer id;
    /**
     * 团贷分配，商户唯一号(测试，生产不一样)
     */
	@TableField("oId_partner")
	@ApiModelProperty(required= true,value = "团贷分配，商户唯一号(测试，生产不一样)")
	private String oIdPartner;
    /**
     * 唯一ID，对应cmOrderNo订单号
     */
	@TableField("unique_id")
	@ApiModelProperty(required= true,value = "唯一ID，对应cmOrderNo订单号")
	private String uniqueId;
    /**
     * 1处理中，2成功，3失败
     */
	@TableField("handle_status")
	@ApiModelProperty(required= true,value = "1处理中，2成功，3失败")
	private String handleStatus;
    /**
     * 请求参数JSON
     */
	@TableField("param_json")
	@ApiModelProperty(required= true,value = "请求参数JSON")
	private String paramJson;
    /**
     * 返回结果JSON
     */
	@TableField("result_json")
	@ApiModelProperty(required= true,value = "返回结果JSON")
	private String resultJson;
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

	public String getoIdPartner() {
		return oIdPartner;
	}

	public void setoIdPartner(String oIdPartner) {
		this.oIdPartner = oIdPartner;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}

	public String getParamJson() {
		return paramJson;
	}

	public void setParamJson(String paramJson) {
		this.paramJson = paramJson;
	}

	public String getResultJson() {
		return resultJson;
	}

	public void setResultJson(String resultJson) {
		this.resultJson = resultJson;
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
		return "AgencyRechargeLog{" +
			", id=" + id +
			", oIdPartner=" + oIdPartner +
			", uniqueId=" + uniqueId +
			", handleStatus=" + handleStatus +
			", paramJson=" + paramJson +
			", resultJson=" + resultJson +
			", createUser=" + createUser +
			", createTime=" + createTime +
			"}";
	}
}
