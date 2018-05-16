package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 
 * </p>
 *
 * @author 喻尊龙
 * @since 2018-03-29
 */
@ApiModel
@TableName("tb_transfer_fail_log")
public class TransferFailLog extends Model<TransferFailLog> {

    private static final long serialVersionUID = 1L;

	@TableField("business_id")
	@ApiModelProperty(required= true,value = "")
	private String businessId;
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "")
	private String afterId;
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "")
	private Integer id;
    /**
     * 失败原因 1：贷后不存在该业务 2：催收人员在贷后管理系统不存在
     */
	@TableField("fail_reason")
	@ApiModelProperty(required= true,value = "失败原因 1：贷后不存在该业务 2：催收人员在贷后管理系统不存在")
	private Integer failReason;

	/**
	 * 同步的类型：null/1:电催 催收数据同步； 2: 贷后跟踪记录同步
	 */
	@TableField("trans_type")
	@ApiModelProperty(required= true,value = "同步的类型：null/1:电催 催收数据同步； 2: 贷后跟踪记录同步")
	private Integer transType;



	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFailReason() {
		return failReason;
	}

	public void setFailReason(Integer failReason) {
		this.failReason = failReason;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TransferFailLog{" +
			", businessId=" + businessId +
			", afterId=" + afterId +
			", id=" + id +
			", failReason=" + failReason +
			", transType=" + transType +
			"}";
	}

	public Integer getTransType() {
		return transType;
	}

	public void setTransType(Integer transType) {
		this.transType = transType;
	}
}
