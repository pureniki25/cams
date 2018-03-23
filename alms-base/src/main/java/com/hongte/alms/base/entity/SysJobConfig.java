package com.hongte.alms.base.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.hongte.alms.base.enums.ActivateEnum;
import com.hongte.alms.base.enums.FlagLockEnum;
import com.hongte.alms.base.enums.TimeTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 定时器控制表
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-08
 */
@ApiModel
@TableName("tb_sys_job_config")
public class SysJobConfig extends Model<SysJobConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 定时器Id
     */
	@ApiModelProperty(required= true,value = "定时器Id")
	private String id;
    /**
     * 定时器名称
     */
	@TableField("job_name")
	@ApiModelProperty(required= true,value = "定时器名称")
	private String jobName;
    /**
     * 开关：0：开，1：关
     */
	@TableField("flag_lock")
	@ApiModelProperty(required= true,value = "开关：0：开，1：关")
	private FlagLockEnum flagLock;
    /**
     * 时间类型：0：按小时算，1：按分钟算，2：按秒算
     */
	@TableField("time_type")
	@ApiModelProperty(required= true,value = "时间类型：0：按小时算，1：按分钟算，2：按秒算")
	private TimeTypeEnum timeType;
    /**
     * 时间间隔
     */
	@TableField("time_interval")
	@ApiModelProperty(required= true,value = "时间间隔")
	private Integer timeInterval;
    /**
     * 创建者
     */
	@ApiModelProperty(required= true,value = "创建者")
	private String creator;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 更新者
     */
	@ApiModelProperty(required= true,value = "更新者")
	private String updater;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 最后一次成功执行时间
     */
	@TableField("last_run_time")
	@ApiModelProperty(required= true,value = "最后一次成功执行时间")
	private Date lastRunTime;
	@ApiModelProperty(required= true,value = "")
	private ActivateEnum activate;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}


	public Integer getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(Integer timeInterval) {
		this.timeInterval = timeInterval;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(Date lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

	public FlagLockEnum getFlagLock() {
		return flagLock;
	}

	public void setFlagLock(FlagLockEnum flagLock) {
		this.flagLock = flagLock;
	}

	public TimeTypeEnum getTimeType() {
		return timeType;
	}

	public void setTimeType(TimeTypeEnum timeType) {
		this.timeType = timeType;
	}

	public ActivateEnum getActivate() {
		return activate;
	}

	public void setActivate(ActivateEnum activate) {
		this.activate = activate;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysJobConfig{" +
				"id='" + id + '\'' +
				", jobName='" + jobName + '\'' +
				", flagLock=" + flagLock +
				", timeType=" + timeType +
				", timeInterval=" + timeInterval +
				", creator='" + creator + '\'' +
				", createTime=" + createTime +
				", updater='" + updater + '\'' +
				", updateTime=" + updateTime +
				", lastRunTime=" + lastRunTime +
				", activate=" + activate +
				'}';
	}
}
