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
 * 操作日志
 * </p>
 *
 * @author 刘正全
 * @since 2018-11-26
 */
@ApiModel
@TableName("tb_operate_log")
public class OperateLog extends Model<OperateLog> {

    private static final long serialVersionUID = 1L;




    /**
     * id
     */
	@ApiModelProperty(required= true,value = "id")
	private Long id;
    /**
     * 错误编码
     */
	@TableField("err_code")
	@ApiModelProperty(required= true,value = "错误编码")
	private String errCode;
    /**
     * 错误日志级别:1错误2警告3调试信息
     */
	@TableField("err_level")
	@ApiModelProperty(required= true,value = "错误日志级别:1错误2警告3调试信息")
	private Integer errLevel;
    /**
     * app环境
     */
	@TableField("app_env")
	@ApiModelProperty(required= true,value = "app环境")
	private String appEnv;
    /**
     * 项目名 eg:贷后系统
     */
	@TableField("app_name")
	@ApiModelProperty(required= true,value = "项目名 eg:贷后系统")
	private String appName;
    /**
     * 项目编码 eg:ALMS
     */
	@TableField("app_code")
	@ApiModelProperty(required= true,value = "项目编码 eg:ALMS")
	private String appCode;
    /**
     * 记录地址 推荐类名.方法名  TestControl,test1
     */
	@TableField("log_point")
	@ApiModelProperty(required= true,value = "记录地址 推荐类名.方法名  TestControl,test1")
	private String logPoint;
    /**
     * 记录地址参数 param1 = 1
     */
	@TableField("log_point_param")
	@ApiModelProperty(required= true,value = "记录地址参数 param1 = 1")
	private String logPointParam;
    /**
     * 日志信息 告警时候的通知信息
     */
	@TableField("log_console")
	@ApiModelProperty(required= true,value = "日志信息 告警时候的通知信息")
	private String logConsole;
    /**
     * 拓展字段1
     */
	@ApiModelProperty(required= true,value = "拓展字段1")
	private String key1;
    /**
     * 拓展字段2
     */
	@ApiModelProperty(required= true,value = "拓展字段2")
	private String key2;
    /**
     * 拓展字段3
     */
	@ApiModelProperty(required= true,value = "拓展字段3")
	private String key3;
    /**
     * 拓展字段4
     */
	@ApiModelProperty(required= true,value = "拓展字段4")
	private String key4;
    /**
     * 拓展字段5
     */
	@ApiModelProperty(required= true,value = "拓展字段5")
	private String key5;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_man")
	@ApiModelProperty(required= true,value = "创建人")
	private String createMan;
    /**
     * 最后创建时间
     */
	@TableField("last_update_time")
	@ApiModelProperty(required= true,value = "最后创建时间")
	private Date lastUpdateTime;
    /**
     * 最后创建人
     */
	@TableField("last_update_man")
	@ApiModelProperty(required= true,value = "最后创建人")
	private String lastUpdateMan;
    /**
     * 是否告警
     */
	@TableField("alarm_flag")
	@ApiModelProperty(required= true,value = "是否告警")
	private Integer alarmFlag;
    /**
     * 告警方式 1邮件2短信3钉钉
     */
	@TableField("alarm_method")
	@ApiModelProperty(required= true,value = "告警方式 1邮件2短信3钉钉")
	private Integer alarmMethod;
    /**
     * 告警地址
     */
	@TableField("alarm_adr")
	@ApiModelProperty(required= true,value = "告警地址")
	private String alarmAdr;
    /**
     * 告警ip
     */
	@TableField("alarm_ip")
	@ApiModelProperty(required= true,value = "告警ip")
	private String alarmIp;
    /**
     * 告警环境,默认全部告警
     */
	@TableField("alarm_env")
	@ApiModelProperty(required= true,value = "告警环境,默认全部告警")
	private String alarmEnv;

	public OperateLog (){
		this.setAppEnv("");
		this.setAppName("贷后系统");
		this.setAppCode("ALMS");

	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public Integer getErrLevel() {
		return errLevel;
	}

	public void setErrLevel(Integer errLevel) {
		this.errLevel = errLevel;
	}

	public String getAppEnv() {
		return appEnv;
	}

	public void setAppEnv(String appEnv) {
		this.appEnv = appEnv;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getLogPoint() {
		return logPoint;
	}

	public void setLogPoint(String logPoint) {
		this.logPoint = logPoint;
	}

	public String getLogPointParam() {
		return logPointParam;
	}

	public void setLogPointParam(String logPointParam) {
		this.logPointParam = logPointParam;
	}

	public String getLogConsole() {
		return logConsole;
	}

	public void setLogConsole(String logConsole) {
		this.logConsole = logConsole;
	}

	public String getKey1() {
		return key1;
	}

	public void setKey1(String key1) {
		this.key1 = key1;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2(String key2) {
		this.key2 = key2;
	}

	public String getKey3() {
		return key3;
	}

	public void setKey3(String key3) {
		this.key3 = key3;
	}

	public String getKey4() {
		return key4;
	}

	public void setKey4(String key4) {
		this.key4 = key4;
	}

	public String getKey5() {
		return key5;
	}

	public void setKey5(String key5) {
		this.key5 = key5;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateMan() {
		return createMan;
	}

	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getLastUpdateMan() {
		return lastUpdateMan;
	}

	public void setLastUpdateMan(String lastUpdateMan) {
		this.lastUpdateMan = lastUpdateMan;
	}

	public Integer getAlarmFlag() {
		return alarmFlag;
	}

	public void setAlarmFlag(Integer alarmFlag) {
		this.alarmFlag = alarmFlag;
	}

	public Integer getAlarmMethod() {
		return alarmMethod;
	}

	public void setAlarmMethod(Integer alarmMethod) {
		this.alarmMethod = alarmMethod;
	}

	public String getAlarmAdr() {
		return alarmAdr;
	}

	public void setAlarmAdr(String alarmAdr) {
		this.alarmAdr = alarmAdr;
	}

	public String getAlarmIp() {
		return alarmIp;
	}

	public void setAlarmIp(String alarmIp) {
		this.alarmIp = alarmIp;
	}

	public String getAlarmEnv() {
		return alarmEnv;
	}

	public void setAlarmEnv(String alarmEnv) {
		this.alarmEnv = alarmEnv;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "OperateLog{" +
			", id=" + id +
			", errCode=" + errCode +
			", errLevel=" + errLevel +
			", appEnv=" + appEnv +
			", appName=" + appName +
			", appCode=" + appCode +
			", logPoint=" + logPoint +
			", logPointParam=" + logPointParam +
			", logConsole=" + logConsole +
			", key1=" + key1 +
			", key2=" + key2 +
			", key3=" + key3 +
			", key4=" + key4 +
			", key5=" + key5 +
			", createTime=" + createTime +
			", createMan=" + createMan +
			", lastUpdateTime=" + lastUpdateTime +
			", lastUpdateMan=" + lastUpdateMan +
			", alarmFlag=" + alarmFlag +
			", alarmMethod=" + alarmMethod +
			", alarmAdr=" + alarmAdr +
			", alarmIp=" + alarmIp +
			", alarmEnv=" + alarmEnv +
			"}";
	}
}
