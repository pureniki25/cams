package com.hongte.alms.open.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hongte.alms.base.RepayPlan.dto.CarBusinessAfterDetailDto;
import com.hongte.alms.base.RepayPlan.dto.PlanReturnInfoDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanListDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizPlanDto;
import com.hongte.alms.base.RepayPlan.req.CreatRepayPlanReq;
import com.hongte.alms.base.RepayPlan.req.trial.TrailRepayPlanReq;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.enums.AlmsServiceNameEnums;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.SectionRepayStatusEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.SysApiCallFailureRecordService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.open.feignClient.CreatRepayPlanRemoteApi;
import com.hongte.alms.open.feignClient.financeService.RepayPlanRemoteApi;
import com.hongte.alms.open.service.CollectionXindaiService;
import com.hongte.alms.open.util.TripleDESDecrypt;
import com.hongte.alms.open.util.XinDaiEncryptUtil;
import com.hongte.alms.open.vo.RepayPlanReq;
import com.hongte.alms.open.vo.RequestData;
import com.hongte.alms.open.vo.ResponseData;
import feign.Feign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@RestController
@RequestMapping("/RepayPlan")
@Api(value = "RepayPlanController", description = "还款计划相关控制器")
public class RepayPlanController {
    Logger  logger = LoggerFactory.getLogger(RepayPlanController.class);

    /**
     * 信贷系统api地址
     */
    @Value(value = "${bmApi.apiUrl}")
    private String apiUrl;

    @Autowired
    private CreatRepayPlanRemoteApi creatRepayPlanRemoteService;
    @Autowired
    private RepayPlanRemoteApi repayPlanRemoteApi;
    @Autowired
    @Qualifier("SysApiCallFailureRecordService")
    private SysApiCallFailureRecordService sysApiCallFailureRecordService;


    static final ConcurrentMap<Integer, String> FEE_TYPE_MAP;

    static {
        //要对费类型进行转换：
        //从 应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收
        //转成 费用类型(  1:本金; 2:利息; 3:服务费; 4:其他费用; 5:违约金;6:冲应收)
        FEE_TYPE_MAP = Maps.newConcurrentMap();
        FEE_TYPE_MAP.put(1, "本金");
        FEE_TYPE_MAP.put(2, "利息");
        FEE_TYPE_MAP.put(3, "服务费");
        FEE_TYPE_MAP.put(4, "其他费用");
        FEE_TYPE_MAP.put(5, "违约金");
        FEE_TYPE_MAP.put(6, "冲应收");
    }


    @ApiOperation(value = "创建还款计划接口,不存储   全字段")
    @PostMapping("/creatRepayPlan")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> creatRepayPlan(@RequestBody CreatRepayPlanReq creatRepayPlanReq){
        return creatRepayPlanRemoteService.creatRepayPlan(creatRepayPlanReq);
    }


    @ApiOperation(value = "创建还款计划并将还款计划及业务和上标信息存储到数据库 接口")
    @PostMapping("/creatAndSaveRepayPlan")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> creatAndSaveRepayPlan(@RequestBody CreatRepayPlanReq creatRepayPlanReq){
        logger.info("创建还款计划并将还款计划及业务和上标信息存储到数据库 接口--开始[{}]" , JSON.toJSONString(creatRepayPlanReq));
        Result<PlanReturnInfoDto> ret= creatRepayPlanRemoteService.creatAndSaveRepayPlan(creatRepayPlanReq);
        logger.info("创建还款计划并将还款计划及业务和上标信息存储到数据库 接口--结束[{}]" , JSON.toJSONString(ret));
        return ret;

    }

//    @ApiOperation(value = "测试时间转换")
//    @PostMapping("/testTime")
//    public Result<PlanReturnInfoDto> testTime(){
//         Result<PlanReturnInfoDto> ret= creatRepayPlanRemoteService.testTime();
//         return ret;
//
//    }



    @ApiOperation(value = "试算还款计划接口, 精简字段")
    @PostMapping("/trailRepayPlan")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> trailRepayPlan(@RequestBody TrailRepayPlanReq trailRepayPlanReq){
        logger.info("试算还款计划接口, 精简字段--开始[{}]" , JSON.toJSONString(trailRepayPlanReq));
        Result<PlanReturnInfoDto> ret = creatRepayPlanRemoteService.trailRepayPlan(trailRepayPlanReq);
        logger.info("试算还款计划接口, 精简字段--结束[{}]" , JSON.toJSONString(ret));



        return ret;
    }

    @ApiOperation(value = "根据businessId查询还款计划")
    @PostMapping("/queryRepayPlanByBusinessId")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> queryRepayPlanByBusinessId(@RequestBody RepayPlanReq req){
        return creatRepayPlanRemoteService.queryRepayPlanByBusinessId(req);
    }

    @ApiOperation(value = "撤销还款计划")
    @PostMapping("/deleteRepayPlanByConditions")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> deleteRepayPlanByConditions(@RequestBody RepayPlanReq req){
        return creatRepayPlanRemoteService.deleteRepayPlanByConditions(req);
    }

    @ApiOperation(value = "根据businessId查询还款计划")
    @PostMapping("/getRepayList")
    @ResponseBody
//    @TripleDESDecrypt
    Result<List<BizDto>> getRepayList(@RequestParam(value = "businessIds") List<String> businessIds){
        return creatRepayPlanRemoteService.getRepayList(businessIds);
    }


     @ApiOperation(value = "根据业务ID查找此业务的历史账单")
    @PostMapping("/getLogBill")
    @ResponseBody
    Result<BizDto> getLogBill(@RequestParam(value = "businessId")String businessId){
        return creatRepayPlanRemoteService.getLogBill(businessId);
    }

    @ApiOperation(value = "根据还款计划ID查找出此还款计划的详情账单信息")
    @PostMapping("/getBizPlanBill")
    @ResponseBody
     Result<BizPlanDto> getBizPlanBill(@RequestParam(value = "planId") String planId){
        return creatRepayPlanRemoteService.getBizPlanBill(planId);
    }


    /**
     * 将指定业务的还款计划的变动通过信贷接口推送给信贷系统
     *
     * @param repayPlanReq 请求参数对象
     * @return 响应结果对象
     * @author 张贵宏
     * @date 2018/6/21 11:01
     */
    @ApiOperation("将指定业务的还款计划的变动通过信贷接口推送给信贷系统")
    @PostMapping("/updateRepayPlanToLMS")
    @ResponseBody
    public Result updateRepayPlanToLMS(@RequestBody RepayPlanReq repayPlanReq) {
        logger.info("[开始] 还款计划-将指定业务的还款计划的变动通过信贷接口推送给信贷系统：参数repayPlanReq=[{}]", JSON.toJSONString(repayPlanReq));
        String businessId = repayPlanReq.getBusinessId();
        //String afterId = repayPlanReq.getAfterId();
        if (repayPlanReq == null || StringUtils.isBlank(businessId)) {
            return Result.error("业务ID参数缺失");
        }
//        if (repayPlanReq == null || StringUtils.isBlank(afterId)) {
//            return Result.error("期数ID参数缺失");
//        }
        try {
            //1，调用alms-finance-service获取还款计划相关数据
            Result<PlanReturnInfoDto> planReturnInfoDtoResult = repayPlanRemoteApi.queryRepayPlanByBusinessId(repayPlanReq);
            logger.info(JSON.toJSONString(planReturnInfoDtoResult));
            if (planReturnInfoDtoResult == null || !"1".equals(planReturnInfoDtoResult.getCode()) || planReturnInfoDtoResult.getData() == null) {
                logger.info("[处理] 还款计划-调用alms-finance-service获取还款计划相关数据失败：result=[{}]", JSON.toJSONString(planReturnInfoDtoResult));
                return Result.error(planReturnInfoDtoResult.getMsg());
            }

            //2, 处理接口需要的数据
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<Map<String, Object>> paramMapList = Lists.newArrayList();
            List<RepaymentBizPlanDto> repaymentBizPlanDtos = planReturnInfoDtoResult.getData().getRepaymentBizPlanDtos();
            for (RepaymentBizPlanDto repaymentBizPlanDto : repaymentBizPlanDtos) {
                RepaymentBizPlan bizPlan = repaymentBizPlanDto.getRepaymentBizPlan();
                for (RepaymentBizPlanListDto bizPlanListDto : repaymentBizPlanDto.getBizPlanListDtos()) {
                    RepaymentBizPlanList bizPlanList = bizPlanListDto.getRepaymentBizPlanList();
//                    if (!afterId.equals(bizPlanList.getAfterId())) {
//                        continue;
//                    }
                    Map<String, Object> paramMap = Maps.newHashMap();
                    paramMap.put("businessId", bizPlanList.getBusinessId());
                    paramMap.put("afterId", bizPlanList.getAfterId());
                    paramMap.put("overdueDays", bizPlanList.getOverdueDays() != null ? bizPlanList.getOverdueDays().intValue() : 0);
                    paramMap.put("currentStatus", bizPlanList.getCurrentStatus());
                    /*同步过程中,若线上已还款,则同步信贷后为已还款,9.7与胡青松,曾坤,肖莹环商议*/
                    if (bizPlanList.getRepayStatus()!=null && bizPlanList.getRepayStatus() >= SectionRepayStatusEnum.ONLINE_REPAID.getKey() ) {
                    	paramMap.put("currentStatus",RepayCurrentStatusEnums.已还款.toString());
					}
                    /*同步过程中,若线上已还款,则同步信贷后为已还款,9.7与胡青松,曾坤,肖莹环商议*/
                    
                    //源数据: 已还款类型标记，null或0：还款中，6：申请展期已还款，10：线下确认已还款，20：自动线下代扣已还款，21，人工线下代扣已还款，30：自动银行代扣已还款，31：人工银行代扣已还款，
                    // 40：用户APP主动还款，50：线下财务确认全部结清，60：线下代扣全部结清，70：银行代扣全部结清
                    // =>>>>>
                    //接口需要：0:还款中1:财务确认已还款 2:自动匹配已还款 3:财务确认全部结清,4:财务代扣已还款,5:自动代扣已还款,6:标识展期已还款,7:当期部分已还款,8:用户APP主动还款,9:代扣全部结清
                    Integer repayFlag = 0;
                    if (bizPlanList.getRepayFlag() != null) {
                        switch (bizPlanList.getRepayFlag()) {
                            case 0:
                                repayFlag = 0;
                                break;
                            case 6:
                                repayFlag = 6;
                                break;
                            case 10:
                                repayFlag = 1;
                                break;
                            case 21:
                            case 31:
                                repayFlag = 4;
                                break;
                            case 30:
                            case 20:
                                repayFlag = 5;
                                break;
                            case 40:
                                repayFlag = 8;
                                break;
                            case 50:
                                repayFlag = 3;
                                break;
                            case 70:
                            case 60:
                                repayFlag = 9;
                                break;
                        }
                    }else {
                    	 /*线上部分已还款的置位为 财务确认已还款*/
                        if (bizPlanList.getRepayStatus()!=null && bizPlanList .getRepayStatus() > 1) {
							repayFlag = 1 ;
						}
					}
                    paramMap.put("repayedFlag", repayFlag);
                    paramMap.put("factRepayDate", bizPlanList.getFactRepayDate());

                    List<CarBusinessAfterDetailDto> afterDetailDtos = Lists.newArrayList();
                    BigDecimal subCompanyServiceFeeTotal = BigDecimal.ZERO;
                    BigDecimal factSubCompanyServiceFeeTotal = BigDecimal.ZERO;
                    BigDecimal subGuaranteeFeeTotal = BigDecimal.ZERO;
                    BigDecimal factGuaranteeFeeTotal = BigDecimal.ZERO;
                    BigDecimal planOtherExpenses = BigDecimal.ZERO;
                    BigDecimal actualOtherExpernses = BigDecimal.ZERO;
                    for (RepaymentBizPlanListDetail planListDetail : bizPlanListDto.getBizPlanListDetails()) {
                        switch (planListDetail.getPlanItemType()) {
                            //本金
                            case 10:
                                paramMap.put("factPrincipa", planListDetail.getFactAmount());
                                paramMap.put("currentPrincipa", planListDetail.getPlanAmount());
                                break;
                            //利息
                            case 20:
                                paramMap.put("factAccrual", planListDetail.getFactAmount());
                                paramMap.put("currentAccrual", planListDetail.getPlanAmount());
                                break;
                            //滞纳金
                            case 60:
                                paramMap.put("currentBreach", planListDetail.getPlanAmount());
                                paramMap.put("overdueMoney", planListDetail.getFactAmount());
                                break;
                            //分公司服务费：资产端分公司服务费
                            case 30:
                            case 50:  //资金端平台服务费  属于服务费
                                //paramMap.put("subCompanyServiceFee", planListDetail.getPlanAmount());
                                //paramMap.put("factSubCompanyServiceFee", planListDetail.getFactAmount());
                                //可能有多个分公司服务费，要循环累加
                                subCompanyServiceFeeTotal = subCompanyServiceFeeTotal.add(planListDetail.getPlanAmount());
                                factSubCompanyServiceFeeTotal = factSubCompanyServiceFeeTotal.add(planListDetail.getFactAmount()==null?BigDecimal.valueOf(0):planListDetail.getFactAmount());
                                break;
                            //担保费    
                            case 40:
                                //paramMap.put("subCompanyServiceFee", planListDetail.getPlanAmount());
                                //paramMap.put("factSubCompanyServiceFee", planListDetail.getFactAmount());
                                //可能有多个分公司服务费，要循环累加
                            	subGuaranteeFeeTotal = subGuaranteeFeeTotal.add(planListDetail.getPlanAmount());
                            	factGuaranteeFeeTotal = factGuaranteeFeeTotal.add(planListDetail.getFactAmount()==null?BigDecimal.valueOf(0):planListDetail.getFactAmount());
                                break;     
                            //其它费用
                            default:
                                if (planListDetail.getPlanAmount() != null) {
                                    planOtherExpenses = planOtherExpenses.add(planListDetail.getPlanAmount());
                                }
                                if (planListDetail.getFactAmount() != null) {
                                    actualOtherExpernses = actualOtherExpernses.add(planListDetail.getFactAmount());
                                }
                        }

                        CarBusinessAfterDetailDto afterDetail = new CarBusinessAfterDetailDto();
                        afterDetail.setBusinessId(planListDetail.getBusinessId());
                        afterDetail.setBusinessAfterId(bizPlanList.getAfterId());
                        afterDetail.setFeeId(planListDetail.getFeeId());
                        //要对费类型进行转换：
                        //从 应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收
                        //转成 费用类型(  1:本金; 2:利息; 3:服务费; 4:其他费用; 5:违约金;6:冲应收)
                        // Integer feeType = 4;
                        // switch (planListDetail.getPlanItemType()) {
                        //     case 10:
                        //         feeType = 1;
                        //         break;
                        //     case 20:
                        //         feeType = 2;
                        //         break;
                        //     case 30:
                        //         feeType = 3;
                        //         break;
                        //     case 40:
                        //     case 50:
                        //     case 60:
                        //     case 80:
                        //     case 90:
                        //         feeType = 4;
                        //         break;
                        //     case 70:
                        //         feeType = 5;
                        //         break;
                        //     case 100:
                        //         feeType = 6;
                        //         break;
                        // }
                        //afterDetail.setAfterFeeType(feeType);
                        Integer feeType =RepayPlanFeeTypeEnum.getByKey(planListDetail.getPlanItemType()).getXd_value();
                        afterDetail.setAfterFeeType(feeType);
                        afterDetail.setFeeName(planListDetail.getPlanItemName());
                        afterDetail.setPlanFeeValue(planListDetail.getPlanAmount());
                        afterDetail.setActualFeeValue(planListDetail.getFactAmount());
                        afterDetail.setPlanRepaymentDate(planListDetail.getDueDate());
                        afterDetail.setActualRepaymentDate(planListDetail.getFactRepayDate());
                        afterDetail.setDerateAmount(planListDetail.getDerateAmount());
                        afterDetail.setCreateTime(planListDetail.getCreateDate());
                        afterDetail.setCreateUser(planListDetail.getCreateUser());
                        afterDetail.setUpdateTime(planListDetail.getUpdateDate());
                        afterDetail.setUpdateUser(planListDetail.getUpdateUser());
                        afterDetailDtos.add(afterDetail);
                    }
                    //如果项目中无滞纳鑫则取期数中的滞纳鑫
                    if (!paramMap.containsKey("currentBreach")) {
                        paramMap.put("currentBreach", bizPlanList.getOverdueAmount());
                    }
                    paramMap.put("remark", bizPlanList.getRemark());
                    paramMap.put("subCompanyServiceFee", subCompanyServiceFeeTotal);
                    paramMap.put("factSubCompanyServiceFee", factSubCompanyServiceFeeTotal);
                    paramMap.put("subGuaranteeFee", subGuaranteeFeeTotal);
                    paramMap.put("factGuaranteeFee", factGuaranteeFeeTotal);
                    paramMap.put("currentOtherMoney", planOtherExpenses);
                    paramMap.put("otherMoney", actualOtherExpernses);
                    paramMap.put("confirmFlag", bizPlanList.getConfirmFlag());
                    paramMap.put("financeConfirmDate", bizPlanList.getFinanceComfirmDate());
                    paramMap.put("financeConfirmUser", bizPlanList.getFinanceConfirmUser());
                    paramMap.put("autoWithholdingConfirmedDate", bizPlanList.getAutoWithholdingConfirmedDate());
                    paramMap.put("autoWithholdingConfirmedUser", bizPlanList.getAutoWithholdingConfirmedUser());
                    paramMap.put("accountantConfirmStatus", bizPlanList.getAccountantConfirmStatus());
                    paramMap.put("accountantConfirmUser", bizPlanList.getAccountantConfirmUser());
                    paramMap.put("accountantConfirmDate", bizPlanList.getAccountantConfirmDate());
                    paramMap.put("createTime", bizPlanList.getCreateTime());
                    paramMap.put("dueDate", bizPlanList.getDueDate());
                    paramMap.put("borrowMoney", bizPlanList.getTotalBorrowAmount());
                    paramMap.put("updateTime", bizPlanList.getUpdateTime());
                    paramMap.put("updateUser", bizPlanList.getUpdateUser());
                    paramMap.put("carBizDetailDtos", afterDetailDtos);

                    paramMapList.add(paramMap);
                }


            }

            //3，将指定业务的还款计划的变动通过信贷接口推送给信贷系统
            JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
            //RequestData requestData = new RequestData(JSON.toJSONString(paramMapList, SerializerFeature.WriteDateUseDateFormat), "Api4Alms_UpdateRepaymentPlan");
            String paramStr = JSON.toJSONString(paramMapList, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty,
                    SerializerFeature.WriteDateUseDateFormat);
            RequestData requestData = new RequestData(
                    paramStr, "Api4Alms_UpdateRepaymentPlan");
            String ciphertext = XinDaiEncryptUtil.encryptPostData(JSON.toJSONString(requestData));
            logger.info(ciphertext);
            CollectionXindaiService collectionXindaiService = Feign.builder().target(CollectionXindaiService.class, apiUrl);
            String respStr = collectionXindaiService.updateRepaymentPlan(ciphertext);
            // 返回数据解密
            ResponseData respData = XinDaiEncryptUtil.getRespData(respStr);
            if (respData == null || !Constant.LMS_SUCCESS_CODE.equals(respData.getReturnCode())) {
                logger.info("[处理] 还款计划-将指定业务的还款计划的变动通过信贷接口推送给信贷系统失败：result=[{}]", JSON.toJSONString(respData));
                logger.info("[处理] 还款计划-将指定业务的还款计划的变动通过信贷接口推送给信贷系统失败：ciphertext=[{}]", ciphertext);
                logger.info("[处理] 还款计划-将指定业务的还款计划的变动通过信贷接口推送给信贷系统失败：paramStr=[{}]", paramStr);
                /*
                // 这里不再记录失败记录，让调用方去记录，避免微服务导致的多次调用 
                try {
                    //接口调用失败记录写入数据库以便定时生推
                    sysApiCallFailureRecordService.save(AlmsServiceNameEnums.OPEN, Constant.INTERFACE_CODE_OPEN_REPAYPLAN_UPDATEREPAYPLANTOLMS, Constant.INTERFACE_NAME_OPEN_REPAYPLAN_UPDATEREPAYPLANTOLMS,
                            businessId, paramStr, ciphertext, JSON.toJSONString(respData), apiUrl + "api/ltgproject/dod", this.getClass().getSimpleName());
                } catch (ServiceRuntimeException e) {
                    logger.info("[处理] 还款计划-记录接口调用日志失败");
                } */

                return Result.error(respData.getReturnMessage());
            }

            logger.info("[结束] 还款计划-将指定业务的还款计划的变动通过信贷接口推送给信贷系统：参数repayPlanReq=[{}]", JSON.toJSONString(repayPlanReq));
            return Result.success();
        } catch (Exception e) {
            logger.error("[异常] 还款计划-向LMS推送数据异常", e);
            return Result.error(e.getMessage());
        }
    }



//    @RequestMapping(value = "/RepayPlan/getRepayList",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
//    Result<PlanReturnInfoDto> getRepayList(@RequestParam(value = "businessIds") List<String> businessIds) ;



//
//    @Autowired
//    @Qualifier("CreatRepayPlanService")
//    CreatRepayPlanService creatRepayPlanService;
//
//
//    @ApiOperation(value = "创建还款计划接口,不存储   全字段")
//    @PostMapping("/creatRepayPlan")
//    @ResponseBody
//    public Result<PlanReturnInfoDto> creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq){
//        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--开始[{}]" , JSON.toJSONString(creatRepayPlanReq));
//        PlanReturnInfoDto  planReturnInfoDto;
////        List<RepaymentBizPlanDto>  list ;
//        try{
//            planReturnInfoDto = creatRepayPlanService.creatRepayPlan(creatRepayPlanReq);
//        }catch (CreatRepaymentExcepiton e){
//            logger.info("传入数据格式不对[{}]" , e);
//            return Result.error(e.getCode(),e.getMsg());
//        }catch (Exception e){
//            logger.info("程序异常[{}]" , e);
//            return Result.error("9889",e.getMessage());
//        }
//        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , JSON.toJSONString(planReturnInfoDto));
//
//        return Result.success(planReturnInfoDto);
//    }
//
////    @ApiOperation(value = "测试Ret")
////    @GetMapping("/testRet")
////    @ResponseBody
////    public  Result testRet(){
////        return Result.error("111","111");
////    }
//
//    @ApiOperation(value = "创建还款计划并将还款计划及业务和上标信息存储到数据库 接口")
//    @PostMapping("/creatAndSaveRepayPlan")
//    @ResponseBody
//    public Result<PlanReturnInfoDto> creatAndSaveRepayPlan(CreatRepayPlanReq creatRepayPlanReq){
//        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--开始[{}]" , JSON.toJSONString(creatRepayPlanReq));
//        PlanReturnInfoDto  planReturnInfoDto;
//        List<RepaymentBizPlanDto>  list ;
//        try{
//            planReturnInfoDto = creatRepayPlanService.creatAndSaveRepayPlan(creatRepayPlanReq);
//        }catch (CreatRepaymentExcepiton e){
//            logger.info("传入数据格式不对[{}]" , e);
//            return Result.error(e.getCode(),e.getMsg());
//        }catch (Exception e){
//            logger.info("程序异常[{}]" , e);
//            return Result.error("9889",e.getMessage());
//        }
//
//        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , JSON.toJSONString(creatRepayPlanReq));
//
//        return  Result.success(planReturnInfoDto);
//    }
//
//
//    @ApiOperation(value = "试算还款计划接口, 精简字段")
//    @PostMapping("/trailRepayPlan")
//    @ResponseBody
//    public Result<PlanReturnInfoDto> trailRepayPlan(TrailRepayPlanReq trailRepayPlanReq){
//        logger.info("@还款计划@试算还款计划接口,对业务和标的还款计划进行试算--开始[{}]" , JSON.toJSONString(trailRepayPlanReq));
//        CreatRepayPlanReq creatRepayPlanReq= null;
//        try{
//            creatRepayPlanReq =  ClassCopyUtil.copy(trailRepayPlanReq,TrailRepayPlanReq.class,CreatRepayPlanReq.class);
//
//            TrailBizInfoReq trailBizInfoReq = trailRepayPlanReq.getTrailBizInfoReq();
//
//            BusinessBasicInfoReq businessBasicInfoReq = ClassCopyUtil.copy(trailBizInfoReq,TrailBizInfoReq.class,BusinessBasicInfoReq.class);
//            creatRepayPlanReq.setBusinessBasicInfoReq(businessBasicInfoReq);
//            businessBasicInfoReq.setBusinessId("111");
//            businessBasicInfoReq.setOrgBusinessId(businessBasicInfoReq.getBusinessId());
//
//            List<TrailProjInfoReq> trailProjInfoReqs = trailRepayPlanReq.getProjInfoReqs();
//
//            List<ProjInfoReq>  projInfoReqs = new LinkedList<>();
//            creatRepayPlanReq.setProjInfoReqs(projInfoReqs);
//            for(TrailProjInfoReq trailProjInfoReq:trailProjInfoReqs){
//                ProjInfoReq projInfoReq =ClassCopyUtil.copy(trailProjInfoReq,TrailProjInfoReq.class,ProjInfoReq.class);
//                projInfoReqs.add(projInfoReq);
//
//                List<TrailProjFeeReq> trailProjFeeReqs = trailProjInfoReq.getProjFeeInfos();
//                List<ProjFeeReq> projFeeReqs = new LinkedList<>();
//                projInfoReq.setProjFeeInfos(projFeeReqs);
//
//                for(TrailProjFeeReq trailProjFeeReq:trailProjFeeReqs){
//                    ProjFeeReq projFeeReq = ClassCopyUtil.copy(trailProjFeeReq,TrailProjFeeReq.class,ProjFeeReq.class);
//                    projFeeReqs.add(projFeeReq);
//                }
//
//            }
//        }catch (Exception e){
//            logger.info("@还款计划@试算还款计划接口,对业务和标的还款计划进行试算--异常[{}]" , e);
//            return Result.error("9889",e.getMessage());
//        }
////        logger.info("@还款计划@试算还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , trailRepayPlanReq);
//        return creatRepayPlan(creatRepayPlanReq);

//        List<RepaymentBizPlanDto>  list ;
//        try{
//            list = creatRepayPlanService.creatRepayPlan(creatRepayPlanReq);
//        }catch (CreatRepaymentExcepiton e){
//            logger.info("传入数据格式不对[{}]" , e);
//            return Result.error(e.getCode(),e.getMsg());
//        }catch (Exception e){
//            logger.info("程序异常[{}]" , e);
//            return Result.error("9889",e.getMessage());
//        }
//        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , list);
//
//        return Result.success(list);
//    }


}
