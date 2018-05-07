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

//    /**
//     *  业务的出款计划信息
//     */
//    @ApiModelProperty(required= true,value = "业务出款计划信息")
//    private List<BizOutPutPlanReq> outPutPlanList;
//
//
//    /**
//     *  业务的出款记录信息
//     */
//    @ApiModelProperty(required= true,value = "业务出款详情信息")
//    private  List<BizOutPutRecordReq >  outPutRecordReqList;


    /**
     * 进位方式标志位
     0：进一位
     1：不进位  舍入在丢弃某部分之前始终不增加数字
     2：正数进一位，负数不进位
        接近正无穷大的舍入模式。
        如果 BigDecimal 为正，则舍入行为与 ROUND_UP 相同;
        如果为负，则舍入行为与 ROUND_DOWN 相同。
        注意，此舍入模式始终不会减少计算值。
     3：正数不进位，负数进一位
         接近负无穷大的舍入模式。
         如果 BigDecimal 为正，则舍入行为与 ROUND_DOWN 相同;
         如果为负，则舍入行为与 ROUND_UP 相同。
         注意，此舍入模式始终不会增加计算值。
     4：四舍五入
     5：五舍六入
        ROUND_HALF_DOWN
     6：银行家舍入法，前一位为奇数则入位，为偶数则舍去
        如果前一位为奇数，则入位，否则舍去。
        以下例子为保留小数点1位，那么这种舍入方式下的结果。
        1.15>1.2 1.25>1.2
     7：断言请求的操作具有精确的结果，因此不需要舍入
     */
    @ApiModelProperty(required= true,value = "进位方式标志位")
    private Integer Rondmode;


    @ApiModelProperty(required= true,value = "计算保留的小数位数")
    private Integer smallNum;


    @ApiModelProperty(required= true,value = "平台标志位：1，团贷网； 2，你我金融")
    private Integer plateType;



    /**
     *  团贷上标信息
     */
    @ApiModelProperty(required= true,value = "团贷上标信息")
    private  List<ProjInfoReq> tuandaiProjReqInfos;


    public BusinessBasicInfoReq getBusinessBasicInfoReq() {
        return businessBasicInfoReq;
    }

    public void setBusinessBasicInfoReq(BusinessBasicInfoReq businessBasicInfoReq) {
        this.businessBasicInfoReq = businessBasicInfoReq;
    }


//    public List<BizOutPutPlanReq> getOutPutPlanList() {
//        return outPutPlanList;
//    }
//
//    public void setOutPutPlanList(List<BizOutPutPlanReq> outPutPlanList) {
//        this.outPutPlanList = outPutPlanList;
//    }
//
//    public List<BizOutPutRecordReq> getOutPutRecordReqList() {
//        return outPutRecordReqList;
//    }
//
//    public void setOutPutRecordReqList(List<BizOutPutRecordReq> outPutRecordReqList) {
//        this.outPutRecordReqList = outPutRecordReqList;
//    }

    public List<ProjInfoReq> getTuandaiProjReqInfos() {
        return tuandaiProjReqInfos;
    }

    public void setTuandaiProjReqInfos(List<ProjInfoReq> tuandaiProjReqInfos) {
        this.tuandaiProjReqInfos = tuandaiProjReqInfos;
    }

    public Integer getRondmode() {
        return Rondmode;
    }

    public void setRondmode(Integer rondmode) {
        Rondmode = rondmode;
    }

    public Integer getSmallNum() {
        return smallNum;
    }

    public void setSmallNum(Integer smallNum) {
        this.smallNum = smallNum;
    }

    public Integer getPlateType() {
        return plateType;
    }

    public void setPlateType(Integer plateType) {
        this.plateType = plateType;
    }
}
