package com.hongte.alms.base.process.vo;

import com.hongte.alms.base.process.entity.ProcessLog;

/**
 * @author zengkun
 * @since 2018/2/8
 */
public class ProcessLogVo  extends ProcessLog{
    //审核人姓名
    private String approveUserName;


    public String getApproveUserName() {
        return approveUserName;
    }

    public void setApproveUserName(String approveUserName) {
        this.approveUserName = approveUserName;
    }


}
