package com.hongte.alms.base.entity;

import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;

public class JobAndTrigger extends Model<JobAndTrigger> {


/*    QRTZ_JOB_DETAILS.JOB_NAME as jobName,
    QRTZ_JOB_DETAILS.JOB_GROUP as jobGroup,
    QRTZ_JOB_DETAILS.JOB_CLASS_NAME as jobClassName,
    QRTZ_TRIGGERS.TRIGGER_NAME as triggerName,
    QRTZ_TRIGGERS.TRIGGER_GROUP as triggerGroup,
    QRTZ_TRIGGERS.TRIGGER_STATE as triggerState,

    QRTZ_CRON_TRIGGERS.CRON_EXPRESSION as cronExpression,
    QRTZ_CRON_TRIGGERS.TIME_ZONE_ID as timeZoneId*/


    private String jobName;
    private String jobGroup;
    private String jobClassName;
    private String triggerName;
    private String triggerGroup;
    private String triggerState;
//    private BigInteger REPEAT_INTERVAL;
//    private BigInteger TIMES_TRIGGERED;
    private String cronExpression;
    private String timeZoneId;



    @Override
    protected Serializable pkVal() {
        return null;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }
}
