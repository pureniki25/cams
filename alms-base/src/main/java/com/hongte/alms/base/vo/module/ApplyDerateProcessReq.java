package com.hongte.alms.base.vo.module;

import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.common.util.ClassCopyUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zengkun
 * @since 2018/2/6
 */
@ApiModel
public class ApplyDerateProcessReq extends ApplyDerateProcess {

    @ApiModelProperty(required= true,value = "流程状态")
    private  Integer processStatus;



    public ApplyDerateProcess  copyInfoApplyDerateProcess(){

        ApplyDerateProcess p = new ApplyDerateProcess();
//        ClassCopyUtil.copy(this,this.getClass(),p.getClass());

        ClassCopyUtil.fatherToChild(this,p);

/*        p.setUpdateUser(this.getUpdateUser());
        p.setUpdateTime(this.getUpdateTime());
        p.setCreateUser(this.getCreateUser());
        p.setCreateTime(this.getCreateTime());
        p.setProcessId(this.getProcessId());
        p.setApplyDerateProcessId(this.getApplyDerateProcessId());
        p.setBusinessId(this.getBusinessId());
        p.setCrpId(this.getCrpId());
        p.se*/

        return p;

    }


    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }
}
