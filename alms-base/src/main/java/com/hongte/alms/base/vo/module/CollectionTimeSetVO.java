package com.hongte.alms.base.vo.module;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.base.enums.ColTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 清算时间设置表
 * </p>
 *
 * @author czs
 * @since 2018-04-20
 */
@ApiModel
public class CollectionTimeSetVO extends Model<CollectionTimeSetVO> {

    private static final long serialVersionUID = 1L;

    /**
     * 移交时间 配置ID
     */
	private String colTimeId;
    /**
     * 移交类型（1：移交清算一（电催），2：移交清算二（上门催收），3：移交法务诉讼）
     */
	private ColTypeEnum colType;
    /**
     * 逾期天数
     */
	private Integer overDueDays;
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date startTime;
    /**
     * 创建时间
     */
	private Date creatTime;
	private String creatUser;
    /**
     * 更新时间
     */
	private Date updateTime;
    /**
     * 更新用户
     */
	private String updateUser;


	public String getColTimeId() {
		return colTimeId;
	}

	public void setColTimeId(String colTimeId) {
		this.colTimeId = colTimeId;
	}

	public ColTypeEnum getColType() {
		return colType;
	}

	public void setColType(ColTypeEnum colType) {
		this.colType = colType;
	}

	public Integer getOverDueDays() {
		return overDueDays;
	}

	public void setOverDueDays(Integer overDueDays) {
		this.overDueDays = overDueDays;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getCreatUser() {
		return creatUser;
	}

	public void setCreatUser(String creatUser) {
		this.creatUser = creatUser;
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
		return this.colTimeId;
	}

	@Override
	public String toString() {
		return "CollectionTimeSet{" +
			", colTimeId=" + colTimeId +
			", colType=" + colType +
			", overDueDays=" + overDueDays +
			", startTime=" + startTime +
			", creatTime=" + creatTime +
			", creatUser=" + creatUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
