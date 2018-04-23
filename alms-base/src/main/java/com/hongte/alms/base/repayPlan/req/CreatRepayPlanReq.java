package com.hongte.alms.base.repayPlan.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/4/20
 * 创建还款计划的接口
 */
@ApiModel("创建还款计划的请求信息")
public class CreatRepayPlanReq {


    /**
     * 业务的基本信息
     */
    @ApiModelProperty(required= true,value = "业务基本信息")
    private BusinessBasicInfoReq businessBasicInfoReq;

    /**
     *  业务的出款计划信息
     */
    @ApiModelProperty(required= true,value = "业务出款计划信息")
    private List<BizOutPutPlanReq> outPutPlanList;


    /**
     *  业务的出款记录信息
     */
    @ApiModelProperty(required= true,value = "业务出款详情信息")
    private  List<BizOutPutRecordReq >  outPutRecordReqList;

    /**
     *  团贷上标信息
     */
    @ApiModelProperty(required= true,value = "业务出款详情信息")
    private  List<TuandaiProjInfoReq> tuandaiProjReqInfos;




    public BusinessBasicInfoReq getBusinessBasicInfoReq() {
        return businessBasicInfoReq;
    }

    public void setBusinessBasicInfoReq(BusinessBasicInfoReq businessBasicInfoReq) {
        this.businessBasicInfoReq = businessBasicInfoReq;
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

    public List<TuandaiProjInfoReq> getTuandaiProjReqInfos() {
        return tuandaiProjReqInfos;
    }

    public void setTuandaiProjReqInfos(List<TuandaiProjInfoReq> tuandaiProjReqInfos) {
        this.tuandaiProjReqInfos = tuandaiProjReqInfos;
    }
}
