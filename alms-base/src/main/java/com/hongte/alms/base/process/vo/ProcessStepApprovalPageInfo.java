/*
 * Copyright (C), 2017-2018 广东鸿特信息咨询有限公司
 * FileName: ProcessStepApprovalPageInfo.java
 * Author:   伦惠峰
 * Date:     2018/1/22 16:34
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间         版本号             描述
 */
package com.hongte.alms.base.process.vo;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.entity.ProcessTypeStep;

import java.util.List;

/**
 *流程节点审批页面相关信息
 * @author 伦惠峰
 * @Date 2018/1/22 16:34
 */
public class ProcessStepApprovalPageInfo
{
    /**
     * 当前流程相关信息
     */
     private  Process process;

    /**
     * 可以回退的步骤列表
     */
     private List<ProcessTypeStep> canReturnStepList;


    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public List<ProcessTypeStep> getCanReturnStepList() {
        return canReturnStepList;
    }

    public void setCanReturnStepList(List<ProcessTypeStep> canReturnStepList) {
        this.canReturnStepList = canReturnStepList;
    }
}
