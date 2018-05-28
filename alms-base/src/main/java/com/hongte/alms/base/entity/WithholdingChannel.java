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
 * 代扣渠道列表
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-05-26
 */
@ApiModel
@TableName("tb_withholding_channel")
public class WithholdingChannel extends Model<WithholdingChannel> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键/自增
     */
	@TableId(value="channel_id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键/自增")
	private Integer channelId;
    /**
     * 优先级
     */
	@ApiModelProperty(required= true,value = "优先级")
	private Integer level;
    /**
     * 代扣平台ID，tb_withholding_platform表的外键
     */
	@TableField("platform_id")
	@ApiModelProperty(required= true,value = "代扣平台ID，tb_withholding_platform表的外键")
	private Integer platformId;
    /**
     * 渠道状态，0：停用，1：启用
     */
	@TableField("channel_status")
	@ApiModelProperty(required= true,value = "渠道状态，0：停用，1：启用")
	private Integer channelStatus;
    /**
     * 每日失败最大次数
     */
	@TableField("fail_times")
	@ApiModelProperty(required= true,value = "每日失败最大次数")
	private Integer failTimes;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
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


	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public Integer getChannelStatus() {
		return channelStatus;
	}

	public void setChannelStatus(Integer channelStatus) {
		this.channelStatus = channelStatus;
	}

	public Integer getFailTimes() {
		return failTimes;
	}

	public void setFailTimes(Integer failTimes) {
		this.failTimes = failTimes;
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
		return this.channelId;
	}

	@Override
	public String toString() {
		return "WithholdingChannel{" +
			", channelId=" + channelId +
			", level=" + level +
			", platformId=" + platformId +
			", channelStatus=" + channelStatus +
			", failTimes=" + failTimes +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
