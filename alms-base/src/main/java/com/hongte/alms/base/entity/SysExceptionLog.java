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
 * 
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-06-19
 */
@ApiModel
@TableName("tb_sys_exception_log")
public class SysExceptionLog extends Model<SysExceptionLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="log_id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "")
	private Integer logId;
    /**
     * 日志等级，越高越严重
     */
	@TableField("log_level")
	@ApiModelProperty(required= true,value = "日志等级，越高越严重")
	private String logLevel;
    /**
     * 日志类
     */
	@ApiModelProperty(required= true,value = "日志类")
	private String logger;
    /**
     * 报错地址
     */
	@TableField("log_host")
	@ApiModelProperty(required= true,value = "报错地址")
	private String logHost;
    /**
     * 报错日期
     */
	@TableField("log_date")
	@ApiModelProperty(required= true,value = "报错日期")
	private Date logDate;
	@TableField("log_thread")
	@ApiModelProperty(required= true,value = "")
	private String logThread;
    /**
     * 日志信息
     */
	@TableField("log_message")
	@ApiModelProperty(required= true,value = "日志信息")
	private String logMessage;
    /**
     * 系统报错错误内容
     */
	@TableField("log_exception")
	@ApiModelProperty(required= true,value = "系统报错错误内容")
	private String logException;
    /**
     * 业务关联ID
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务关联ID")
	private String businessId;
    /**
     * 日志类型表述
     */
	@TableField("log_type")
	@ApiModelProperty(required= true,value = "日志类型表述")
	private String logType;
    /**
     * 本地数据镜像
     */
	@TableField("send_jason")
	@ApiModelProperty(required= true,value = "本地数据镜像")
	private String sendJason;
    /**
     * 返回数据镜像
     */
	@TableField("return_jason")
	@ApiModelProperty(required= true,value = "返回数据镜像")
	private String returnJason;


	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public String getLogHost() {
		return logHost;
	}

	public void setLogHost(String logHost) {
		this.logHost = logHost;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getLogThread() {
		return logThread;
	}

	public void setLogThread(String logThread) {
		this.logThread = logThread;
	}

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	public String getLogException() {
		return logException;
	}

	public void setLogException(String logException) {
		this.logException = logException;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getSendJason() {
		return sendJason;
	}

	public void setSendJason(String sendJason) {
		this.sendJason = sendJason;
	}

	public String getReturnJason() {
		return returnJason;
	}

	public void setReturnJason(String returnJason) {
		this.returnJason = returnJason;
	}

	@Override
	protected Serializable pkVal() {
		return this.logId;
	}

	@Override
	public String toString() {
		return "SysExceptionLog{" +
			", logId=" + logId +
			", logLevel=" + logLevel +
			", logger=" + logger +
			", logHost=" + logHost +
			", logDate=" + logDate +
			", logThread=" + logThread +
			", logMessage=" + logMessage +
			", logException=" + logException +
			", businessId=" + businessId +
			", logType=" + logType +
			", sendJason=" + sendJason +
			", returnJason=" + returnJason +
			"}";
	}
}
