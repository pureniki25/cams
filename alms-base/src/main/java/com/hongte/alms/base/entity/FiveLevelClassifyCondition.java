package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 五级分类设置业务类别-条件表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-04-22
 */
@ApiModel
@TableName("tb_five_level_classify_condition")
public class FiveLevelClassifyCondition extends Model<FiveLevelClassifyCondition> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(required= true,value = "主键ID")
	private String id;
    /**
     * 业务类型
     */
	@TableField("business_type")
	@ApiModelProperty(required= true,value = "业务类型")
	private String businessType;
    /**
     * 分类名称（对应tb_five_level_classify的class_name）
     */
	@TableField("class_name")
	@ApiModelProperty(required= true,value = "分类名称（对应tb_five_level_classify的class_name）")
	private String className;
    /**
     * 子分类名称
     */
	@TableField("sub_class_name")
	@ApiModelProperty(required= true,value = "子分类名称")
	private String subClassName;
    /**
     * 是否生效：0，失效；1，有效. 默认 1
     */
	@TableField("valid_status")
	@ApiModelProperty(required= true,value = "是否生效：0，失效；1，有效. 默认 1")
	private String validStatus;
    /**
     * 1、满足所有条件；2、满足任一条件
     */
	@TableField("execute_condition")
	@ApiModelProperty(required= true,value = "1、满足所有条件；2、满足任一条件")
	private String executeCondition;
    /**
     * 分类父id
     */
	@TableField("parent_id")
	@ApiModelProperty(required= true,value = "分类父id")
	private String parentId;
    /**
     * tb_sys_parameter的param_name
     */
	@TableField("param_name")
	@ApiModelProperty(required= true,value = "tb_sys_parameter的param_name")
	private String paramName;
    /**
     * 参数类型（对应tb_sys_parameter的param_type）
     */
	@TableField("param_type")
	@ApiModelProperty(required= true,value = "参数类型（对应tb_sys_parameter的param_type）")
	private String paramType;
    /**
     * 参数类型与参数名的关系（1:>=,2:<=,3:>,4:<,5:=）
     */
	@TableField("type_name_relation")
	@ApiModelProperty(required= true,value = "参数类型与参数名的关系（1:>=,2:<=,3:>,4:<,5:=）")
	private String typeNameRelation;
    /**
     * 操作类型1、增加；2、修改；3、删除
     */
	@TableField("op_type")
	@ApiModelProperty(required= true,value = "操作类型1、增加；2、修改；3、删除")
	private String opType;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
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


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSubClassName() {
		return subClassName;
	}

	public void setSubClassName(String subClassName) {
		this.subClassName = subClassName;
	}

	public String getValidStatus() {
		return validStatus;
	}

	public void setValidStatus(String validStatus) {
		this.validStatus = validStatus;
	}

	public String getExecuteCondition() {
		return executeCondition;
	}

	public void setExecuteCondition(String executeCondition) {
		this.executeCondition = executeCondition;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public String getTypeNameRelation() {
		return typeNameRelation;
	}

	public void setTypeNameRelation(String typeNameRelation) {
		this.typeNameRelation = typeNameRelation;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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
		return "FiveLevelClassifyCondition{" +
			", id=" + id +
			", businessType=" + businessType +
			", className=" + className +
			", subClassName=" + subClassName +
			", validStatus=" + validStatus +
			", executeCondition=" + executeCondition +
			", parentId=" + parentId +
			", paramName=" + paramName +
			", paramType=" + paramType +
			", typeNameRelation=" + typeNameRelation +
			", opType=" + opType +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", blackValue1=" + blackValue1 +
			", blackValue2=" + blackValue2 +
			", blackValue3=" + blackValue3 +
			"}";
	}
}
