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
 * [系统参数表]
 * </p>
 *
 * @author 黄咏康
 * @since 2018-02-01
 */
@ApiModel
@TableName("tb_sys_parameter")
public class SysParameter extends Model<SysParameter> {

    private static final long serialVersionUID = 1L;

    /**
     * 参数编号
     */
    @TableId("param_id")
	@ApiModelProperty(required= true,value = "参数编号")
	private String paramId;
    /**
     * 参数名称
     */
	@TableField("param_name")
	@ApiModelProperty(required= true,value = "参数名称")
	private String paramName;
    /**
     * 参数类型
     */
	@TableField("param_type")
	@ApiModelProperty(required= true,value = "参数类型")
	private String paramType;
    /**
     * 参数类型名称
     */
	@TableField("param_type_name")
	@ApiModelProperty(required= true,value = "参数类型名称")
	private String paramTypeName;
    /**
     * 默认参数值
     */
	@TableField("param_value")
	@ApiModelProperty(required= true,value = "默认参数值")
	private String paramValue;
    /**
     * 父类参数ID(用于树形数据)
     */
	@TableField("parent_paramId")
	@ApiModelProperty(required= true,value = "父类参数ID(用于树形数据)")
	private String parentParamId;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;
    /**
     * 状态
     */
	@ApiModelProperty(required= true,value = "状态")
	private Integer status;
    /**
     * 排序
     */
	@TableField("row_Index")
	@ApiModelProperty(required= true,value = "排序")
	private Integer rowIndex;
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


	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getParamTypeName() {
		return paramTypeName;
	}

	public void setParamTypeName(String paramTypeName) {
		this.paramTypeName = paramTypeName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParentParamId() {
		return parentParamId;
	}

	public void setParentParamId(String parentParamId) {
		this.parentParamId = parentParamId;
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

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
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
		return this.paramId;
	}

	@Override
	public String toString() {
		return "SysParameter{" +
			", paramId=" + paramId +
			", paramName=" + paramName +
			", paramType=" + paramType +
			", paramTypeName=" + paramTypeName +
			", paramValue=" + paramValue +
			", parentParamId=" + parentParamId +
			", remark=" + remark +
			", status=" + status +
			", rowIndex=" + rowIndex +
			", createUser=" + createUser +
			", createTime=" + createTime +
			"}";
	}
}
