package com.hongte.alms.base.process.vo;

import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.entity.ProcessLog;
import com.hongte.alms.base.vo.module.ApplyDerateProcessReq;

import java.math.BigDecimal;

/**
 * @author zengkun
 * @since 2018/2/7
 */
public class ProcessLogReq extends ProcessLog{

    //流程信息
    private Process process;

    //减免申请信息保存对象
    private ApplyDerateProcessReq applyInfo;

    //抄送的用户ID
    private String[] sendUserIds;

    //减免后实收
    private BigDecimal  realReceiveMoney;

    //当前审批者是否是创建者的标志位
    private Boolean isCreaterFlage;

//    isPass			:'',//是否同意
//    isPassFlage			:'',//是否同意界面显示标志位
//    isRockBack:'',        //是否回退
//    isRockFlage:'',        //是否回退标界面显示志位
//    rockBackStepId:'',      //回退的步骤ID


    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String[] getSendUserIds() {
        return sendUserIds;
    }

    public void setSendUserIds(String[] sendUserIds) {
        this.sendUserIds = sendUserIds;
    }

    public BigDecimal getRealReceiveMoney() {
        return realReceiveMoney;
    }

    public void setRealReceiveMoney(BigDecimal realReceiveMoney) {
        this.realReceiveMoney = realReceiveMoney;
    }

    public Boolean getIsCreaterFlage() {
        return isCreaterFlage;
    }

    public void setIsCreaterFlage(Boolean createrFlage) {
        isCreaterFlage = createrFlage;
    }

    public ApplyDerateProcessReq getApplyInfo() {
        return applyInfo;
    }

    public void setApplyInfo(ApplyDerateProcessReq applyInfo) {
        this.applyInfo = applyInfo;
    }
}
