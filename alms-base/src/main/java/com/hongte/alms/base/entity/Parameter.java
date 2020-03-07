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
 * 
 * </p>
 *
 * @author 刘正全
 * @since 2018-08-03
 */
@ApiModel
@TableName("tb_parameter")
public class Parameter extends Model<Parameter> {

    private static final long serialVersionUID = 1L;

    @TableId("PARA_ID")
	@ApiModelProperty(required= true,value = "")
	private String paraId;
    /**
     * [参数名称]
     */
	@TableField("PARA_NAME")
	@ApiModelProperty(required= true,value = "[参数名称]")
	private String paraName;
    /**
     * [参数类型]
     */
	@TableField("PARA_TYPE")
	@ApiModelProperty(required= true,value = "[参数类型]")
	private String paraType;
	@TableField("PARA_VALUE")
	@ApiModelProperty(required= true,value = "")
	private String paraValue;
    /**
     * [备注]
     */
	@TableField("REMARK")
	@ApiModelProperty(required= true,value = "[备注]")
	private String remark;
	@TableField("STATUS")
	@ApiModelProperty(required= true,value = "")
	private Integer status;
    /**
     * [RESERVE_1]
     */
	@TableField("RESERVE_1")
	@ApiModelProperty(required= true,value = "[RESERVE_1]")
	private String reserve1;
    /**
     * [RESERVE_2]
     */
	@TableField("RESERVE_2")
	@ApiModelProperty(required= true,value = "[RESERVE_2]")
	private String reserve2;
    /**
     * [RESERVE_3]
     */
	@TableField("RESERVE_3")
	@ApiModelProperty(required= true,value = "[RESERVE_3]")
	private String reserve3;
    /**
     * [RESERVE_4]
     */
	@TableField("RESERVE_4")
	@ApiModelProperty(required= true,value = "[RESERVE_4]")
	private String reserve4;
    /**
     * [RESERVE_5]
     */
	@TableField("RESERVE_5")
	@ApiModelProperty(required= true,value = "[RESERVE_5]")
	private String reserve5;
	@TableField("UPDATE_USER")
	@ApiModelProperty(required= true,value = "")
	private String updateUser;
	@TableField("UPDATE_TIME")
	@ApiModelProperty(required= true,value = "")
	private Date updateTime;
	@TableField("para_order")
	@ApiModelProperty(required= true,value = "")
	private Integer paraOrder;
    /**
     * [RESERVE_6]
     */
	@TableField("RESERVE_6")
	@ApiModelProperty(required= true,value = "[RESERVE_6]")
	private String reserve6;
    /**
     * [RESERVE_7]
     */
	@TableField("RESERVE_7")
	@ApiModelProperty(required= true,value = "[RESERVE_7]")
	private String reserve7;
    /**
     * [RESERVE_8]
     */
	@TableField("RESERVE_8")
	@ApiModelProperty(required= true,value = "[RESERVE_8]")
	private String reserve8;
    /**
     * [RESERVE_9]
     */
	@TableField("RESERVE_9")
	@ApiModelProperty(required= true,value = "[RESERVE_9]")
	private String reserve9;
    /**
     * [RESERVE_10]
     */
	@TableField("RESERVE_10")
	@ApiModelProperty(required= true,value = "[RESERVE_10]")
	private String reserve10;
    /**
     * [创建人]
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "[创建人]")
	private String createUser;
    /**
     * [创建时间]
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "[创建时间]")
	private Date createTime;


	public String getParaId() {
		return paraId;
	}

	public void setParaId(String paraId) {
		this.paraId = paraId;
	}

	public String getParaName() {
		return paraName;
	}

	public void setParaName(String paraName) {
		this.paraName = paraName;
	}

	public String getParaType() {
		return paraType;
	}

	public void setParaType(String paraType) {
		this.paraType = paraType;
	}

	public String getParaValue() {
		return paraValue;
	}

	public void setParaValue(String paraValue) {
		this.paraValue = paraValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	public String getReserve5() {
		return reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
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

	public Integer getParaOrder() {
		return paraOrder;
	}

	public void setParaOrder(Integer paraOrder) {
		this.paraOrder = paraOrder;
	}

	public String getReserve6() {
		return reserve6;
	}

	public void setReserve6(String reserve6) {
		this.reserve6 = reserve6;
	}

	public String getReserve7() {
		return reserve7;
	}

	public void setReserve7(String reserve7) {
		this.reserve7 = reserve7;
	}

	public String getReserve8() {
		return reserve8;
	}

	public void setReserve8(String reserve8) {
		this.reserve8 = reserve8;
	}

	public String getReserve9() {
		return reserve9;
	}

	public void setReserve9(String reserve9) {
		this.reserve9 = reserve9;
	}

	public String getReserve10() {
		return reserve10;
	}

	public void setReserve10(String reserve10) {
		this.reserve10 = reserve10;
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
		return this.paraId;
	}

	@Override
	public String toString() {
		return "Parameter{" +
			", paraId=" + paraId +
			", paraName=" + paraName +
			", paraType=" + paraType +
			", paraValue=" + paraValue +
			", remark=" + remark +
			", status=" + status +
			", reserve1=" + reserve1 +
			", reserve2=" + reserve2 +
			", reserve3=" + reserve3 +
			", reserve4=" + reserve4 +
			", reserve5=" + reserve5 +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			", paraOrder=" + paraOrder +
			", reserve6=" + reserve6 +
			", reserve7=" + reserve7 +
			", reserve8=" + reserve8 +
			", reserve9=" + reserve9 +
			", reserve10=" + reserve10 +
			", createUser=" + createUser +
			", createTime=" + createTime +
			"}";
	}
}
