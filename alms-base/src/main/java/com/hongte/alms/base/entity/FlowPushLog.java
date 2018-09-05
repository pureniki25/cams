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
 * 
 * </p>
 *
 * @author 刘正全
 * @since 2018-09-05
 */
@ApiModel
@TableName("tb_flow_push_log")
public class FlowPushLog extends Model<FlowPushLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录流水id
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "记录流水id")
	private Long id;
    /**
     * 推送关键字 confireLogId
     */
	@TableField("push_key")
	@ApiModelProperty(required= true,value = "推送关键字 confireLogId")
	private String pushKey;
    /**
     * 推送流水类型 1还款代扣流水 2结清流水 3资金分发流水
     */
	@TableField("push_log_type")
	@ApiModelProperty(required= true,value = "推送流水类型 1还款代扣流水 2结清流水 3资金分发流水")
	private Integer pushLogType;
    /**
     * 推送目标1cams核心账务
     */
	@TableField("push_to")
	@ApiModelProperty(required= true,value = "推送目标1cams核心账务")
	private Integer pushTo;
    /**
     * 推送开始时间
     */
	@TableField("push_starttime")
	@ApiModelProperty(required= true,value = "推送开始时间")
	private Date pushStarttime;
    /**
     * 推送结束时间
     */
	@TableField("push_endtime")
	@ApiModelProperty(required= true,value = "推送结束时间")
	private Date pushEndtime;
    /**
     * 推送状态0开始推送1推送成功2推送失败
     */
	@TableField("push_status")
	@ApiModelProperty(required= true,value = "推送状态0开始推送1推送成功2推送失败")
	private Integer pushStatus;
    /**
     * 推送参数
     */
	@TableField("push_param")
	@ApiModelProperty(required= true,value = "推送参数")
	private String pushParam;
    /**
     * 推送返回
     */
	@TableField("push_ret")
	@ApiModelProperty(required= true,value = "推送返回")
	private String pushRet;
    /**
     * 推送备注
     */
	@TableField("push_remark")
	@ApiModelProperty(required= true,value = "推送备注")
	private String pushRemark;
    /**
     * 推送日志扩展字段1
     */
	@TableField("push_key1")
	@ApiModelProperty(required= true,value = "推送日志扩展字段1")
	private String pushKey1;
    /**
     * 推送日志扩展字段2
     */
	@TableField("push_key2")
	@ApiModelProperty(required= true,value = "推送日志扩展字段2")
	private String pushKey2;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPushKey() {
		return pushKey;
	}

	public void setPushKey(String pushKey) {
		this.pushKey = pushKey;
	}

	public Integer getPushLogType() {
		return pushLogType;
	}

	public void setPushLogType(Integer pushLogType) {
		this.pushLogType = pushLogType;
	}

	public Integer getPushTo() {
		return pushTo;
	}

	public void setPushTo(Integer pushTo) {
		this.pushTo = pushTo;
	}

	public Date getPushStarttime() {
		return pushStarttime;
	}

	public void setPushStarttime(Date pushStarttime) {
		this.pushStarttime = pushStarttime;
	}

	public Date getPushEndtime() {
		return pushEndtime;
	}

	public void setPushEndtime(Date pushEndtime) {
		this.pushEndtime = pushEndtime;
	}

	public Integer getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Integer pushStatus) {
		this.pushStatus = pushStatus;
	}

	public String getPushParam() {
		return pushParam;
	}

	public void setPushParam(String pushParam) {
		this.pushParam = pushParam;
	}

	public String getPushRet() {
		return pushRet;
	}

	public void setPushRet(String pushRet) {
		this.pushRet = pushRet;
	}

	public String getPushRemark() {
		return pushRemark;
	}

	public void setPushRemark(String pushRemark) {
		this.pushRemark = pushRemark;
	}

	public String getPushKey1() {
		return pushKey1;
	}

	public void setPushKey1(String pushKey1) {
		this.pushKey1 = pushKey1;
	}

	public String getPushKey2() {
		return pushKey2;
	}

	public void setPushKey2(String pushKey2) {
		this.pushKey2 = pushKey2;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "FlowPushLog{" +
			", id=" + id +
			", pushKey=" + pushKey +
			", pushLogType=" + pushLogType +
			", pushTo=" + pushTo +
			", pushStarttime=" + pushStarttime +
			", pushEndtime=" + pushEndtime +
			", pushStatus=" + pushStatus +
			", pushParam=" + pushParam +
			", pushRet=" + pushRet +
			", pushRemark=" + pushRemark +
			", pushKey1=" + pushKey1 +
			", pushKey2=" + pushKey2 +
			"}";
	}
}
