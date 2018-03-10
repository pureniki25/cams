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
 * 资产端区域列表，树状结构
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-24
 */
@ApiModel
@TableName("tb_basic_company")
public class BasicCompany extends Model<BasicCompany> {

    private static final long serialVersionUID = 1L;

    /**
     * 资产端区域ID
     */
    @TableId("area_id")
	@ApiModelProperty(required= true,value = "资产端区域ID")
	private String areaId;
    /**
     * 资产端ID
     */
	@TableField("asset_side_id")
	@ApiModelProperty(required= true,value = "资产端ID")
	private String assetSideId;
    /**
     * 区域名称
     */
	@TableField("area_name")
	@ApiModelProperty(required= true,value = "区域名称")
	private String areaName;
    /**
     * 区域简称
     */
	@TableField("area_short_name")
	@ApiModelProperty(required= true,value = "区域简称")
	private String areaShortName;
    /**
     * 区域级别，40：区域级别，60：分公司级别，80：部门级别，100：小组级别
     */
	@TableField("area_level")
	@ApiModelProperty(required= true,value = "区域级别，40：区域级别，60：分公司级别，80：部门级别，100：小组级别")
	private Integer areaLevel;
    /**
     * 父级区域ID
     */
	@TableField("area_pid")
	@ApiModelProperty(required= true,value = "父级区域ID")
	private String areaPid;
    /**
     * 记录该区域所属顶级区域ID（级别为40）,如：常州分公司业务4组的district_id为华东片区对应的area_id，加快查询速度
     */
	@TableField("district_id")
	@ApiModelProperty(required= true,value = "记录该区域所属顶级区域ID（级别为40）,如：常州分公司业务4组的district_id为华东片区对应的area_id，加快查询速度")
	private String districtId;
    /**
     * 创建日期
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建日期")
	private Date createTime;
    /**
     * 创建用户
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户")
	private String createUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;


	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAssetSideId() {
		return assetSideId;
	}

	public void setAssetSideId(String assetSideId) {
		this.assetSideId = assetSideId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaShortName() {
		return areaShortName;
	}

	public void setAreaShortName(String areaShortName) {
		this.areaShortName = areaShortName;
	}

	public Integer getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(Integer areaLevel) {
		this.areaLevel = areaLevel;
	}

	public String getAreaPid() {
		return areaPid;
	}

	public void setAreaPid(String areaPid) {
		this.areaPid = areaPid;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
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

	@Override
	protected Serializable pkVal() {
		return this.areaId;
	}

	@Override
	public String toString() {
		return "BasicCompany{" +
			", areaId=" + areaId +
			", assetSideId=" + assetSideId +
			", areaName=" + areaName +
			", areaShortName=" + areaShortName +
			", areaLevel=" + areaLevel +
			", areaPid=" + areaPid +
			", districtId=" + districtId +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
