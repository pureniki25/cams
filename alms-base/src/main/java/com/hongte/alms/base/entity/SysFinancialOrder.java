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
 * 财务跟单设置
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-29
 */
@ApiModel
@TableName("tb_sys_financial_order")
public class SysFinancialOrder extends Model<SysFinancialOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "自增主键")
	private Integer id;
    /**
     * 区域ID
     */
	@TableField("area_id")
	@ApiModelProperty(required= true,value = "区域ID")
	private String areaId;
    /**
     * 分公司ID
     */
	@TableField("company_id")
	@ApiModelProperty(required= true,value = "分公司ID")
	private String companyId;
    /**
     * 业务类型
     */
	@TableField("business_type")
	@ApiModelProperty(required= true,value = "业务类型")
	private String businessType;
    /**
     * 跟进人的ID
     */
	@TableField("user_ids")
	@ApiModelProperty(required= true,value = "跟进人的ID")
	private String userIds;
    /**
     * 跟进人的名字
     */
	@TableField("user_names")
	@ApiModelProperty(required= true,value = "跟进人的名字")
	private String userNames;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
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

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getUserNames() {
		return userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
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
		return "SysFinancialOrder{" +
			", id=" + id +
			", areaId=" + areaId +
			", companyId=" + companyId +
			", businessType=" + businessType +
			", userIds=" + userIds +
			", userNames=" + userNames +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			", createUser=" + createUser +
			", createTime=" + createTime +
			"}";
	}
}
