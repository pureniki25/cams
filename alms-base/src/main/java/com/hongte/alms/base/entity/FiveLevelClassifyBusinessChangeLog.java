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
 * 业务类别变更记录表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-04-27
 */
@ApiModel
@TableName("tb_five_level_classify_business_change_log")
public class FiveLevelClassifyBusinessChangeLog extends Model<FiveLevelClassifyBusinessChangeLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键ID")
	private Integer id;
    /**
     * 业务源ID
     */
	@TableField("orig_business_id")
	@ApiModelProperty(required= true,value = "业务源ID")
	private String origBusinessId;
    /**
     * 变更来源：1、贷后定时任务；2、贷后跟踪记录；3、风控
     */
	@TableField("op_sourse_type")
	@ApiModelProperty(required= true,value = "变更来源：1、贷后定时任务；2、贷后跟踪记录；3、风控")
	private String opSourseType;
    /**
     * 操作来源ID：若是定时任务触发变更，则为空；否则存入贷后跟踪记录id、风控id
     */
	@TableField("op_sourse_id")
	@ApiModelProperty(required= true,value = "操作来源ID：若是定时任务触发变更，则为空；否则存入贷后跟踪记录id、风控id")
	private String opSourseId;
    /**
     * 操作人ID：若是定时任务触发变更，则为空
     */
	@TableField("op_user_id")
	@ApiModelProperty(required= true,value = "操作人ID：若是定时任务触发变更，则为空")
	private String opUserId;
    /**
     * 操作人姓名：若是定时任务触发变更，则为空
     */
	@TableField("op_username")
	@ApiModelProperty(required= true,value = "操作人姓名：若是定时任务触发变更，则为空")
	private String opUsername;
    /**
     * 记录时间
     */
	@TableField("op_time")
	@ApiModelProperty(required= true,value = "记录时间")
	private Date opTime;
    /**
     * 借款人情况条件描述：若是定时任务触发变更，则为空
     */
	@TableField("borrower_condition_desc")
	@ApiModelProperty(required= true,value = "借款人情况条件描述：若是定时任务触发变更，则为空")
	private String borrowerConditionDesc;
    /**
     * 抵押物情况条件描述：若是定时任务触发变更，则为空
     */
	@TableField("guarantee_condition_desc")
	@ApiModelProperty(required= true,value = "抵押物情况条件描述：若是定时任务触发变更，则为空")
	private String guaranteeConditionDesc;
    /**
     * 分类名称
     */
	@TableField("class_name")
	@ApiModelProperty(required= true,value = "分类名称")
	private String className;
    /**
     * 是否有效：1、有效；0、失效
     */
	@TableField("valid_status")
	@ApiModelProperty(required= true,value = "是否有效：1、有效；0、失效")
	private String validStatus;
    /**
     * 预留字段1
     */
	@TableField("black_value1")
	@ApiModelProperty(required= true,value = "预留字段1")
	private String blackValue1;
    /**
     * 预留字段2
     */
	@TableField("black_value2")
	@ApiModelProperty(required= true,value = "预留字段2")
	private String blackValue2;
    /**
     * 预留字段3
     */
	@TableField("black_value3")
	@ApiModelProperty(required= true,value = "预留字段3")
	private String blackValue3;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrigBusinessId() {
		return origBusinessId;
	}

	public void setOrigBusinessId(String origBusinessId) {
		this.origBusinessId = origBusinessId;
	}

	public String getOpSourseType() {
		return opSourseType;
	}

	public void setOpSourseType(String opSourseType) {
		this.opSourseType = opSourseType;
	}

	public String getOpSourseId() {
		return opSourseId;
	}

	public void setOpSourseId(String opSourseId) {
		this.opSourseId = opSourseId;
	}

	public String getOpUserId() {
		return opUserId;
	}

	public void setOpUserId(String opUserId) {
		this.opUserId = opUserId;
	}

	public String getOpUsername() {
		return opUsername;
	}

	public void setOpUsername(String opUsername) {
		this.opUsername = opUsername;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getBorrowerConditionDesc() {
		return borrowerConditionDesc;
	}

	public void setBorrowerConditionDesc(String borrowerConditionDesc) {
		this.borrowerConditionDesc = borrowerConditionDesc;
	}

	public String getGuaranteeConditionDesc() {
		return guaranteeConditionDesc;
	}

	public void setGuaranteeConditionDesc(String guaranteeConditionDesc) {
		this.guaranteeConditionDesc = guaranteeConditionDesc;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getValidStatus() {
		return validStatus;
	}

	public void setValidStatus(String validStatus) {
		this.validStatus = validStatus;
	}

	public String getBlackValue1() {
		return blackValue1;
	}

	public void setBlackValue1(String blackValue1) {
		this.blackValue1 = blackValue1;
	}

	public String getBlackValue2() {
		return blackValue2;
	}

	public void setBlackValue2(String blackValue2) {
		this.blackValue2 = blackValue2;
	}

	public String getBlackValue3() {
		return blackValue3;
	}

	public void setBlackValue3(String blackValue3) {
		this.blackValue3 = blackValue3;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "FiveLevelClassifyBusinessChangeLog{" +
			", id=" + id +
			", origBusinessId=" + origBusinessId +
			", opSourseType=" + opSourseType +
			", opSourseId=" + opSourseId +
			", opUserId=" + opUserId +
			", opUsername=" + opUsername +
			", opTime=" + opTime +
			", borrowerConditionDesc=" + borrowerConditionDesc +
			", guaranteeConditionDesc=" + guaranteeConditionDesc +
			", className=" + className +
			", validStatus=" + validStatus +
			", blackValue1=" + blackValue1 +
			", blackValue2=" + blackValue2 +
			", blackValue3=" + blackValue3 +
			"}";
	}
}
