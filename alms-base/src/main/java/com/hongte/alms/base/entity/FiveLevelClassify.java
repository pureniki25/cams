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
 * 五级分类设置业务类型-类别关系表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-05-03
 */
@ApiModel
@TableName("tb_five_level_classify")
public class FiveLevelClassify extends Model<FiveLevelClassify> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@ApiModelProperty(required= true,value = "主键id")
	private String id;
    /**
     * 业务类型
     */
	@TableField("business_type")
	@ApiModelProperty(required= true,value = "业务类型")
	private String businessType;
    /**
     * 分类名称
     */
	@TableField("class_name")
	@ApiModelProperty(required= true,value = "分类名称")
	private String className;
    /**
     * 分类级别，数值越大，严重等级越高
     */
	@TableField("class_level")
	@ApiModelProperty(required= true,value = "分类级别，数值越大，严重等级越高")
	private String classLevel;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;
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
     * 有效状态：0、无效，1、有效
     */
	@TableField("valid_status")
	@ApiModelProperty(required= true,value = "有效状态：0、无效，1、有效")
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

	public String getClassLevel() {
		return classLevel;
	}

	public void setClassLevel(String classLevel) {
		this.classLevel = classLevel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
		return "FiveLevelClassify{" +
			", id=" + id +
			", businessType=" + businessType +
			", className=" + className +
			", classLevel=" + classLevel +
			", remark=" + remark +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", validStatus=" + validStatus +
			", blackValue1=" + blackValue1 +
			", blackValue2=" + blackValue2 +
			", blackValue3=" + blackValue3 +
			"}";
	}
}
