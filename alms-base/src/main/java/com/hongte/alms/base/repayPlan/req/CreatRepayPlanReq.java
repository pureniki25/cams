package com.hongte.alms.base.repayPlan.req;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/4/20
 * 创建还款计划的接口
 */
@ApiModel("创建还款计划的请求信息")
public class CreatRepayPlanReq {


    /**
     * 创建还款计划，业务的基本信息
     */
    @ApiModelProperty(required= true,value = "业务基本信息")
    private BusinessBasicReqInfo  businessBasicReqInfo;

    /**
     *  创建还款计划，业务的出款计划信息
     */
    @ApiModelProperty(required= true,value = "业务出款计划信息")
    private List<BizOutPutPlanReq> outPutPlanList;


    /**
     *  创建还款计划，业务的出款计划信息
     */
    @ApiModelProperty(required= true,value = "业务出款详情信息")
    private  List<BizOutPutRecordReq >  outPutRecordReqList;






    public BusinessBasicReqInfo getBusinessBasicReqInfo() {
        return businessBasicReqInfo;
    }

    public void setBusinessBasicReqInfo(BusinessBasicReqInfo businessBasicReqInfo) {
        this.businessBasicReqInfo = businessBasicReqInfo;
    }


    public List<BizOutPutPlanReq> getOutPutPlanList() {
        return outPutPlanList;
    }

    public void setOutPutPlanList(List<BizOutPutPlanReq> outPutPlanList) {
        this.outPutPlanList = outPutPlanList;
    }

    public List<BizOutPutRecordReq> getOutPutRecordReqList() {
        return outPutRecordReqList;
    }

    public void setOutPutRecordReqList(List<BizOutPutRecordReq> outPutRecordReqList) {
        this.outPutRecordReqList = outPutRecordReqList;
    }
}
