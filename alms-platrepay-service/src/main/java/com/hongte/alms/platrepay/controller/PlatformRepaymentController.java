package com.hongte.alms.platrepay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.AlmsServiceNameEnums;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanPayedTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanRepaySrcEnum;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.vo.compliance.TdrepayRechargeInfoVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * 平台合规化还款服务控制器
 *
 * @author 张贵宏
 * @date 2018/6/14 10:37
 */
//@CrossOrigin
@RestController
@RequestMapping("/platformRepayment")
@Api(description = "平台合规化还款服务")
public class PlatformRepaymentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformRepaymentController.class);

    @Autowired
    private EipRemote eipRemote;

    @Autowired
    @Qualifier("AgencyRechargeLogService")
    private AgencyRechargeLogService agencyRechargeLogService;

    @Autowired
    @Qualifier("TdrepayRechargeService")
    private TdrepayRechargeService tdrepayRechargeService;

    @Autowired
    @Qualifier("DepartmentBankService")
    private DepartmentBankService departmentBankService;

    @Autowired
    @Qualifier("TuandaiProjectInfoService")
    private TuandaiProjectInfoService tuandaiProjectInfoService;


    @Autowired
    @Qualifier("BasicBusinessService")
    private BasicBusinessService basicBusinessService;


    @Autowired
    @Qualifier("RepaymentProjPlanService")
    RepaymentProjPlanService repaymentProjPlanService;

    @Autowired
    @Qualifier("RepaymentBizPlanService")
    RepaymentBizPlanService repaymentBizPlanService;

    @Autowired
    @Qualifier("RepaymentProjPlanListService")
    RepaymentProjPlanListService repaymentProjPlanListService;
    
    @Autowired
    @Qualifier("SysApiCallFailureRecordService")
    private SysApiCallFailureRecordService sysApiCallFailureRecordService;

//    @Autowired
//    @Qualifier("RepaymentProjFactRepayService")
//    RepaymentProjFactRepayService repaymentProjFactRepayService;

    //@Qualifier("TdrepayRechargeController")
    @Autowired
    TdrepayRechargeController tdrepayRechargeController;

    @Autowired
    @Qualifier("RepaymentConfirmLogService")
    RepaymentConfirmLogService repaymentConfirmLogService;

    @Autowired
    @Qualifier("RepaymentProjFactRepayService")
    RepaymentProjFactRepayService repaymentProjFactRepayService;

    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    RepaymentBizPlanListService repaymentBizPlanListService;

    @Autowired
    @Qualifier("RepaymentResourceService")
    RepaymentResourceService repaymentResourceService;


    @Autowired
    @Qualifier("RepaymentConfirmPlatRepayLogService")
    RepaymentConfirmPlatRepayLogService   repaymentConfirmPlatRepayLogService;

    @Autowired
    private LoginUserInfoHelper loginUserInfoHelper;

    static final ConcurrentMap<Integer, String> FEE_TYPE_MAP;

    static {

        //要对费类型进行转换：
        //从 应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收
        //转成 费用类型(10:本金,20:利息;30:平台服务费;40:资产端服务费;50:担保公司服务费;60:仲裁服务费;70:逾期费用（罚息）;80: 中介服务费)
        FEE_TYPE_MAP = Maps.newConcurrentMap();
        FEE_TYPE_MAP.put(10, "本金");
        FEE_TYPE_MAP.put(20, "利息");
        FEE_TYPE_MAP.put(30, "平台服务费");
        FEE_TYPE_MAP.put(40, "资产端服务费");
        FEE_TYPE_MAP.put(50, "担保公司服务费");
        FEE_TYPE_MAP.put(60, "仲裁服务费");
        FEE_TYPE_MAP.put(70, "逾期费用(罚息)");
        FEE_TYPE_MAP.put(80, "中介服务费");
    }


    /**
     * 对接合规还款接口
     *
     * @return com.hongte.alms.common.result.Result  Rest接口请求结果对象
     * @author 张贵宏
     * @date 2018/6/14 10:30
     */
    @ApiOperation(value = "对接合规还款接口")
    @PostMapping("/repayment")
    @ResponseBody
    public Result repayment(@RequestBody Map<String, Object> paramMap) {   //TdrepayRechargeInfoVO
        if (paramMap == null || paramMap.isEmpty()) {
            return Result.error("参数不能为空");
        }
        String projPlanListId = (String) paramMap.get("projPlanListId");
        LOGGER.info("@对接合规还款接口 开始 @输入参数 projPlanListId:[{}] ", projPlanListId);
        //参数验证
        if (StringUtils.isBlank(projPlanListId)) {
            return Result.error("标的还款计划列表Id不能为空");
        }
        try {
            //查标的还款期数信息并验证
            List<RepaymentProjPlanList> projPlanLists = repaymentProjPlanListService.selectList(
                    new EntityWrapper<RepaymentProjPlanList>()
                            .eq("proj_plan_list_id", projPlanListId)
            );
            if (projPlanLists == null || projPlanLists.size() == 0) {
                LOGGER.error("@对接合规还款接口@  查不到标的还款计划列表信息 输入参数 projPlanListId:[{}]  ", projPlanListId);
                return Result.error("500", "查不到标的还款计划列表信息");
            }
            if (projPlanLists.size() > 1) {
                LOGGER.error("@对接合规还款接口@  查到两条以上标的还款计划列表信息 输入参数 projPlanListId:[{}]   ", projPlanListId);
                return Result.error("500", "查到两条以上标的还款计划列表信息");
            }
            RepaymentProjPlanList projPlanList = projPlanLists.get(0);
            //查业务信息
            BasicBusiness basicBusiness = basicBusinessService.selectById(projPlanList.getBusinessId());
            //查标的还款计划
            RepaymentProjPlan projPlan = repaymentProjPlanService.selectOne(
                    new EntityWrapper<RepaymentProjPlan>()
                            .eq("proj_plan_id", projPlanList.getProjPlanId())
            );
            if (projPlan == null) {
                LOGGER.error("@对接合规还款接口@  查不到标的还款计划信息 输入参数 projPlanListId:[{}]  ", projPlanListId);
                return Result.error("查不到标的还款计划信息");
            }
            //****************************************************
            //验证这块以后要移除,通用功能不能只针对某一个平台，目前只针对团贷平台. zgh 20180614
            TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService.selectById(projPlan.getProjectId());
            if (tuandaiProjectInfo == null) {
                LOGGER.error("@对接合规还款接口@  查不到平台的上标信息 输入参数 projPlanListId:[{}]  ", projPlanListId);
                return Result.error("查不到平台的上标信息");
            }
            //****************************************************
            RepaymentBizPlanList bizPlanList = repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("plan_list_id", projPlanList.getPlanListId()));
            //部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款
            //资产端内部在分润后将先还完线上部分之后再进行合规化还款，即部分还款子状态为2或者3时
            if (projPlanList.getRepayStatus() == null || projPlanList.getRepayStatus() == 1) {
                LOGGER.error("@对接合规还款接口@  应该在还完线上部分后再调用合规化还款接口 输入参数 projPlanListId:[{}]  ", projPlanListId);
                return Result.error("500", "应该在还完线上部分后再调用合规化还款接口");
            }
            TdrepayRechargeInfoVO vo = new TdrepayRechargeInfoVO();
            vo.setProjectId(projPlan.getProjectId());
            //业务所属资产端，1、鸿特信息，2、 一点车贷
            vo.setAssetType(1);
            //原业务编号
            vo.setOrigBusinessId(projPlanList.getOrigBusinessId());
            //http://wiki.hongte.info/pages/viewpage.action?pageId=3867818
            //业务类型： 业主信用贷用信 -> 业主贷;  小微企业贷用信 -> 商银贷; tb_tuandai_project_info的project_id与master_issue_id相等则 -> 商贸共借， 业主共借同理
            //目标类型： 业务类型(1:车易贷展期,2:房速贷展期,3:金融仓储,4:三农金融,9:车易贷,11:房速贷,12车全垫资代采,13:扶贫贷,14:汽车融资租赁,15:二手车商贷,20:一点车贷,25:商贸贷,26:业主贷,27:家装分期,28:商贸共借;29:业主共借)
            Integer businessType = basicBusiness.getBusinessType();
            // 25 信用贷 特殊处理
            if (businessType == 25) {
                switch (basicBusiness.getBusinessCtype()) {
                    case "小微企业贷用信":
                        if (tuandaiProjectInfo.getProjectId().equals(tuandaiProjectInfo.getMasterIssueId())) {
                            businessType = 30;
                        } else {
                            businessType = 28;
                        }
                        break;
                    case "业主信用贷用信":
                        if (tuandaiProjectInfo.getProjectId().equals(tuandaiProjectInfo.getMasterIssueId())) {
                            businessType = 26;
                        } else {
                            businessType = 29;
                        }
                        break;
                }
            }
            vo.setBusinessType(businessType);

            //实还日期
            vo.setFactRepayDate(projPlanList.getFactRepayDate());
            //借款人,平台只关心标的的每个共借款人姓名
            vo.setCustomerName(tuandaiProjectInfo.getRealName());
            //分公司
            vo.setCompanyName(basicBusiness.getCompanyName());
            //部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款
            //新需求： 资产端内部在分润后将还款状态改为线上已还款之后，再调用合规化还款接口去给资金端还款，如果 repay_status为2线上已还款时repayFlog是没有值的
            //所以要从实还流水中查还款来源
            List<RepaymentProjFactRepay> projFactRepayList =
                    repaymentProjFactRepayService.selectList(
                            new EntityWrapper<RepaymentProjFactRepay>().
                                    eq("proj_plan_list_id",projPlanListId).
                                    orderBy("create_date",false));
            if (projFactRepayList == null || projFactRepayList.size() == 0) {
                LOGGER.error("@对接合规还款接口@ 查询实还流水为空 输入参数projPlanListId:[{}]", projPlanListId);
                return Result.error("查询实还流水为空");
            }
            vo.setRepaySource(RepayPlanRepaySrcEnum.getByValue(projFactRepayList.get(0).getRepaySource()).getPlatRepayVal());
            if (bizPlanList.getFinanceComfirmDate() == null) {
                LOGGER.error("@对接合规还款接口@ 财务确认还款操作日期为空 输入参数plan_list_id:[{}]", bizPlanList.getPlanListId());
                return Result.error("财务确认还款操作日期为空");
            } else {
                vo.setConfirmTime(bizPlanList.getFinanceComfirmDate());
            }
            vo.setAfterId(projPlanList.getAfterId());
            vo.setPeriod(projPlanList.getPeriod());

            //处理标的计划结清状态
            //还款计划状态，0:还款中，10:提前结清，20:已结清，30:亏损结清，40:坏账结清, 50:已申请展期  => 0：非结清，10：正常结清，11：逾期结清，20：展期原标结清，30：坏账结清
            Integer tempProjPlanStatus = 0;
            switch (projPlan.getPlanStatus()) {
                case 0:
                    tempProjPlanStatus = 0;
                    break;
                case 10:
                case 20:
                    tempProjPlanStatus = 10;
                    break;
                case 30:
                case 40:
                    tempProjPlanStatus = 30;
                    break;
                case 50:
                    tempProjPlanStatus = 20;
                    break;
                default:
                    LOGGER.error("@对接合规还款接口@  标的计划结清状态值plan_status:[{}]错误 输入参数 projPlanListId:[{}] ", projPlan.getPlanStatus(), projPlanListId);
                    return Result.error("500", "标的计划结清状态值错误");
            }
            //标的还款计划结清状态
            vo.setSettleType(tempProjPlanStatus);

            //一, 有垫付则按垫付传费用项，否则按查询的标的还未计划信息传费用项
            Map<String, Object> params = Maps.newHashMap();
            params.put("projectId", projPlan.getProjectId());

            //二, 通过外联平台eip调用团贷查询标的还款计划信息：/repayment/queryRepaymentSchedule
            BigDecimal planRepaymentRechargeAmount = BigDecimal.ZERO;
            Map<String, BigDecimal> planRepaymentMap = Maps.newHashMap();
            com.ht.ussp.core.Result ret = eipRemote.queryRepaymentSchedule(params);
            if (Constant.REMOTE_EIP_SUCCESS_CODE.equals(ret.getReturnCode()) && ret.getData() != null) {
                Map<String, Map<String, Object>> retMap = (Map<String, Map<String, Object>>) ret.getData();
                if (retMap.containsKey("repaymentScheduleList")) {
                    List<Map<String, Object>> retList = (List<Map<String, Object>>) retMap.get("repaymentScheduleList");
                    if (retList != null && retList.size() > 0) {
                        for (Map<String, Object> map : retList) {
                            if (map.containsKey("period") && (projPlanList.getPeriod() != (Integer) map.get("period"))) {
                                continue;
                            }
                            // Map<String, Object> repaymentScheduleMap = (Map<String, Object>) retList.get(0);
                            map.remove("period");
                            map.remove("cycDate");
                            for (Map.Entry<String, Object> ent : map.entrySet()) {
                                String key = ent.getKey();
                                BigDecimal value = new BigDecimal(ent.getValue().toString());
                                planRepaymentRechargeAmount = planRepaymentRechargeAmount.add(value);
                                /*需还款本金	amount	Decimal
                                需还款利息	interestAmount	Decimal
                                保证金	depositAmount	Decimal     //不会有这个
                                担保费	guaranteeAmount	Decimal
                                仲 裁费	arbitrationAmount	Decimal  //不会有这个
                                资产端服务费	orgAmount	Decimal
                                平台服务费	tuandaiAmount	Decimal
                                中介服务费	agencyAmount	Decimal
                                其他费用	otherAmount	Decimal*/    //不会有这个
                                //转成 费用类型(10:本金,20:利息;30:平台服务费;40:资产端服务费;50:担保公司服务费;60:仲裁服务费;70:逾期费用（罚息）;80: 中介服务费)
                                if (key.equals("amount")) {
                                    planRepaymentMap.put("10", value);
                                }
                                if (key.equals("interestAmount")) {
                                    planRepaymentMap.put("20", value);
                                }
                                //if(key.equals("depositAmount")){  planRepaymentMap.put("", value); }
                                if (key.equals("guaranteeAmount")) {
                                    planRepaymentMap.put("50", value);
                                }
                                if (key.equals("arbitrationAmount")) {
                                    planRepaymentMap.put("60", value);
                                }
                                if (key.equals("orgAmount")) {
                                    planRepaymentMap.put("40", value);
                                }
                                if (key.equals("tuandaiAmount")) {
                                    planRepaymentMap.put("30", value);
                                }
                                if (key.equals("agencyAmount")) {
                                    planRepaymentMap.put("80", value);
                                }
                                //if(key.equals("otherAmount")){  planRepaymentMap.put("", value); }
                            }
                        }
                    }
                }
            } else {
                LOGGER.error("@对接合规还款接口@  通过外联平台eip(查询标的还款计划:/repayment/queryRepaymentSchedule)调用团贷查询标的还款计划错误 输入参数 projectPlanId:[{}] projectId:[{}]  afterId[{}] ", projPlanListId, projPlan.getProjectId(), bizPlanList.getAfterId());
                return Result.error("500", "通过外联平台eip(查询标的还款计划:/repayment/queryRepaymentSchedule)调用团贷查询标的还款计划错误, returnCode:"+ ret.getReturnCode()+",msg:"+ret.getMsg()+",codeDesc:"+ret.getCodeDesc());
            }
            if (planRepaymentMap.size() == 0) {
                LOGGER.error("@对接合规还款接口@  通过外联平台eip(查询标的还款计划:/repayment/queryRepaymentSchedule)调用团贷查询标的还款计划错误，没有查到本期结果数据. 输入参数 projectPlanId:[{}]  projectId:[{}]  afterId[{}] ", projPlanListId, projPlan.getProjectId(), bizPlanList.getAfterId());
                return Result.error("500", "通过外联平台eip(查询标的还款计划:/repayment/queryRepaymentSchedule)调用团贷查询标的还款计划错误，没有查到本期结果数据.");
            }

            //三, 是否以垫付接口为准,否则以计划接口为准
            boolean byGuaranteePayment = false;
            BigDecimal guaranteeRepaymentRechargeAmount = BigDecimal.ZERO;
            Map<String, BigDecimal> guaranteePaymentMap = Maps.newHashMap();
            //通过外联平台eip调用团贷标的还款计划信息查询接口：/repayment/queryProjectPayment
            com.ht.ussp.core.Result ret2 = eipRemote.queryProjectPayment(params);
            if (Constant.REMOTE_EIP_SUCCESS_CODE.equals(ret2.getReturnCode()) && ret2.getData() != null) {
                Map<String, Map<String, Object>> retMap = (Map<String, Map<String, Object>>) ret2.getData();

                if (retMap.containsKey("projectPayments")) {
                    List<Map<String, Object>> retList = (List<Map<String, Object>>) retMap.get("projectPayments");
                    if (retList != null && retList.size() > 0) {
                        //循环如果没有查询到本期的垫付记录按标还款计划来填值
                        for (Map<String, Object> map : retList) {
                            if (map.containsKey("period") && (projPlanList.getPeriod() != (Integer) map.get("period"))) {
                                continue;
                            }
                            //查到本期的
                            if (map.containsKey("guaranteePayment")) {
                                Map<String, Object> tempMap = (Map<String, Object>) map.get("guaranteePayment");
                                for (Map.Entry<String, Object> ent : tempMap.entrySet()) {
                                    String key = ent.getKey();
                                    BigDecimal value = new BigDecimal(ent.getValue().toString());
                                    guaranteeRepaymentRechargeAmount = guaranteeRepaymentRechargeAmount.add(value);
                                    if (value.compareTo(BigDecimal.ZERO) > 0) {
                                        byGuaranteePayment = true;
                                    }
                                    // 本金利息	principalAndInterest
                                    // 滞纳金	penaltyAmount		逾期费用+罚息
                                    // 实还平台服务费	tuandaiAmount	Decimal	如果有值，返回该字段
                                    // 实还资产端服务费	orgAmount	Decimal	如果有值，返回该字段
                                    // 实还担保公司服务费	guaranteeAmount	Decimal	如果有值，返回该字段
                                    // 实还仲裁服务费	arbitrationAmount	Decimal	如果有值，返回该字段
                                    // 实还中介服务费	agencyAmount	Decimal	如果有值，返回该字段
                                    //转成 费用类型(10:本金,20:利息;30:平台服务费;40:资产端服务费;50:担保公司服务费;60:仲裁服务费;70:逾期费用（罚息）;80: 中介服务费)
                                    if (key.equals("penaltyAmount")) {
                                        guaranteePaymentMap.put("70", value);
                                    }
                                    if (key.equals("tuandaiAmount")) {
                                        guaranteePaymentMap.put("30", value);
                                    }
                                    if (key.equals("orgAmount")) {
                                        guaranteePaymentMap.put("40", value);
                                    }
                                    if (key.equals("guaranteeAmount")) {
                                        guaranteePaymentMap.put("50", value);
                                    }
                                    if (key.equals("arbitrationAmount")) {
                                        guaranteePaymentMap.put("60", value);
                                    }
                                    if (key.equals("agencyAmount")) {
                                        guaranteePaymentMap.put("80", value);
                                    }
                                    //从计划接口中分离出本金和利息
                                    if (key.equals("principalAndInterest")) {
                                        //本金
                                        if (planRepaymentMap.containsKey("10")) {
                                            guaranteePaymentMap.put("10", planRepaymentMap.get("10"));
                                        }
                                        //利息
                                        if (planRepaymentMap.containsKey("20")) {
                                            guaranteePaymentMap.put("20", planRepaymentMap.get("20"));
                                        }
                                    }
                                }
                            }
                        }


                    }
                }
            } else {
                LOGGER.error("@对接合规还款接口@  通过外联平台eip(标的还款信息查询接口:/repayment/queryProjectPayment)调用团贷标的标的还款信息查询接口错误 输入参数 projectPlanId:[{}] projectId:[{}]  afterId[{}] ", projPlanListId, projPlan.getProjectId(), bizPlanList.getAfterId());
                return Result.error("500", "通过外联平台eip调用团贷标的还款信息查询接口错误, returnCode:"+ ret2.getReturnCode()+",msg:"+ret2.getMsg()+",codeDesc:"+ret2.getCodeDesc());
            }
            if (guaranteePaymentMap.size() == 0) {
                /*LOGGER.error("@对接合规还款接口@  通过外联平台eip调用团贷标的还款计划信息查询接口错误，没有查到本期结果数据. 输入参数projectPlanId:[{}] projectId:[{}]  afterId[{}] ", projPlanListId, projPlan.getProjectId(), bizPlanList.getAfterId());
                return Result.error("500", "通过外联平台eip调用团贷标的还款计划信息查询接口错误，没有查到本期结果数据.");*/
                //循环如果没有查询到本期的垫付记录按标还款计划来填值
            }

            /*//流水合计
            BigDecimal resourceAmount = BigDecimal.ZERO;
            //List<RepaymentResource> repaymentResources = repaymentResourceService.selectList(new EntityWrapper<RepaymentResource>().eq("confirm_log_id", confirmLogId));
            List<RepaymentResource> repaymentResources = repaymentResourceService.selectList(new EntityWrapper<RepaymentResource>().eq("business_id", repaymentProjPlanList.getBusinessId()).eq("after_id", afterId));
            if (repaymentResources != null && repaymentResources.size() > 0) {
                for (RepaymentResource repaymentResource : repaymentResources) {
                    //排除用 11:用往期结余还款的流水
                    if (repaymentResource.getRepayAmount() != null && repaymentResource.getIsCancelled() == 0 && !"11".equals(repaymentResource.getRepaySource())) {
                        resourceAmount = resourceAmount.add(repaymentResource.getRepayAmount());
                    }
                }
            }
            vo.setResourceAmount(resourceAmount);*/

            //计算费用: proj_fact_repay中要按project_id分组来进行计算,不要按期数计算
            /*List<RepaymentProjFactRepay> projFactRepays = repaymentProjFactRepayService.selectList(
                    new EntityWrapper<RepaymentProjFactRepay>()
                            .eq("confirm_log_id", confirmLogId)
                            .eq("project_id", projectId)
            );*/
            List<RepaymentProjFactRepay> projFactRepays = repaymentProjFactRepayService.selectList(
                    new EntityWrapper<RepaymentProjFactRepay>().eq("proj_plan_list_id", projPlanList.getProjPlanListId())
            );
            if (projFactRepays != null && projFactRepays.size() > 0) {
                //实收总金额
                BigDecimal factRepayAmount = BigDecimal.ZERO;
                //充值金额
                BigDecimal rechargeAmount = BigDecimal.ZERO;

                List<TdrepayRechargeDetail> detailFeeList = Lists.newArrayList();
                for (RepaymentProjFactRepay r : projFactRepays) {
                    //实还金额应该包含滞纳金
                    factRepayAmount = factRepayAmount.add(r.getFactAmount());
                    //累计费用，线下（资产公司）的滞纳金排除在外，注意用value和uuid去区分.  OVER_DUE_AMONT_UNDERLINE(60,"线下滞纳金","3131c075-5721-11e8-8a00-0242ac110002",5)
                    /*if (RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getValue().equals(r.getPlanItemType())
                            || RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid().equals(r.getFeeId())) {
                        continue;
                    } */
                    if (RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid().equals(r.getFeeId())) {
                        continue;
                    }
                    //resourceAmount = resourceAmount.add(r.getFactAmount());
                    rechargeAmount = rechargeAmount.add(r.getFactAmount());
                    TdrepayRechargeDetail detailFee = new TdrepayRechargeDetail();
                    //要对费类型进行转换：
                    //从 应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收
                    //转成 费用类型(10:本金,20:利息;30:平台服务费;40:资产端服务费;50:担保公司服务费;60:仲裁服务费;70:逾期费用（罚息）;80: 中介服务费)
                    Integer feeType = 10;
                    switch (r.getPlanItemType()) {
                        case 10:
                            feeType = 10;
                            break;
                        case 20:
                            feeType = 20;
                            break;
                        case 30:
                            feeType = 40;
                            break;
                        case 40:
                            feeType = 50;
                            break;
                        case 50:
                            feeType = 30;
                            break;
                        case 60:
                            feeType = 70;
                            break;
                        //case 70: planItemType = ?; break;
                        case 80:
                            feeType = 80;
                            break;
                        //case 90: planItemType = ?; break;
                        //case 100: planItemType = ?; break;
                        default:
                            return Result.error("没有对应的费用类型给接口");
                    }
                    detailFee.setFeeType(feeType);
                    detailFee.setFeeName(FEE_TYPE_MAP.get(feeType));
                    //detailFee.setFeeValue(r.getFactAmount());

                    //费用
                    if (byGuaranteePayment) {
                        if(planRepaymentMap.containsKey(feeType.toString())) {
                            detailFee.setFeeValue(guaranteePaymentMap.get(feeType.toString()));
                        }else{
                            detailFee.setFeeValue(r.getFactAmount());
                            guaranteeRepaymentRechargeAmount = guaranteeRepaymentRechargeAmount.add(r.getFactAmount());
                        }
                    } else {
                        if(planRepaymentMap.containsKey(feeType.toString())) {
                            detailFee.setFeeValue(planRepaymentMap.get(feeType.toString()));
                        }else{
                            detailFee.setFeeValue(r.getFactAmount());
                            planRepaymentRechargeAmount = planRepaymentRechargeAmount.add(r.getFactAmount());
                        }
                    }
                    detailFeeList.add(detailFee);
                }
                vo.setFactRepayAmount(factRepayAmount);
                //vo.setRechargeAmount(rechargeAmount);
                //用从外部接口返回的金额总和做为充值金额
                if (byGuaranteePayment) {
                    vo.setRechargeAmount(guaranteeRepaymentRechargeAmount);
                } else {
                    vo.setRechargeAmount(planRepaymentRechargeAmount);
                }
                //用实还赋值流水金额
                vo.setResourceAmount(factRepayAmount);
                //vo.setResourceAmount(resourceAmount);
                vo.setDetailList(detailFeeList);
            }

            //当期还款状态，目前只有三种，分别为 还款中，逾期，已还款 => 当期结清状态 0：未结清,1：已结清
            //如果 current_status为 还款中，逾期则继续判断repay_status==> 2:线上已还款,3:全部已还款 则为结清
            Integer projPlanListStatus = 0;
            switch (projPlanList.getCurrentStatus()) {
                case "还款中":
                case "逾期":
                    if (projPlanList.getRepayStatus() == 2 || projPlanList.getRepayStatus() == 3) {
                        projPlanListStatus = 1;
                    } else {
                        projPlanListStatus = 0;
                    }
                    break;
                case "已还款":
                    projPlanListStatus = 1;
                    break;
                default:
                    LOGGER.error("@对接合规还款接口@  标的计划本期状态值current_status:[{}]错误 输入参数 projPlanListId:[{}] ", projPlanList.getCurrentStatus(), projPlanListId);
                    return Result.error("500", "标的计划本期状态值错误");
            }
            vo.setIsComplete(projPlanListStatus);
            vo.setProjPlanListId(projPlanList.getProjPlanListId());
            if (StringUtils.isBlank(tuandaiProjectInfo.getTdUserId())) {
                LOGGER.error("@对接合规还款接口@ 团贷用户ID(资金端用户ID)为空 projPlanListId:[{}]", projPlanListId);
                return Result.error("团贷用户ID(资金端用户ID)为空");
            } else {
                vo.setTdUserId(tuandaiProjectInfo.getTdUserId());
            }
            //vo.setConfirmLogId(confirmLogId);
            //vo.setConfirmLogId(repaymentBizPlanList.getPlanListId());
            vo.setConfirmLogId(projPlanList.getProjPlanListId());

            Result result = tdrepayRechargeController.accessTdrepayReCharge(vo);
            if (!"1".equals(result.getCode())) {
                LOGGER.error("@对接合规还款接口@ 调用代充值资金分发参数接入接口失败 vo:[{}]", JSON.toJSONString(vo));
                return Result.error("合规还款失败:" + result.getMsg());
            }
            LOGGER.info("@对接合规还款接口@ 调用代充值资金分发成功 vo:[{}]", JSON.toJSONString(vo));
            return Result.success(JSON.toJSONString(vo));
        } catch (Exception e) {
            LOGGER.error("通过合化还款接口还款失败.", e);
            return Result.error("500", "通过合化还款接口还款失败！" + e.getMessage());
        }
    }
    
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "对接合规还款接口")
	@GetMapping("/retryRepayment")
	@ResponseBody
	public Result retryRepayment(@RequestParam("projPlanListId") String projPlanListId) {
		try {
			List<SysApiCallFailureRecord> sysApiCallFailureRecords = sysApiCallFailureRecordService
					.queryRetryMaxCntFailData(Constant.INTERFACE_CODE_PLATREPAY_REPAYMENT,
							AlmsServiceNameEnums.FINANCE.getName(), projPlanListId);
			if (CollectionUtils.isEmpty(sysApiCallFailureRecords)) {
				return Result.success();
			}
			Map<String, Object> paramMap = new HashMap<>();
			for (SysApiCallFailureRecord sysApiCallFailureRecord : sysApiCallFailureRecords) {
				if (sysApiCallFailureRecord.getRetrySuccess() == 1) {
					continue;
				}
				paramMap.put("refId", sysApiCallFailureRecord.getRefId());
				Result result = repayment(paramMap);
				SysApiCallFailureRecord record = new SysApiCallFailureRecord();
				record.setApiCode(sysApiCallFailureRecord.getApiCode());
				record.setApiName(sysApiCallFailureRecord.getApiName());
				record.setApiParamCiphertext(sysApiCallFailureRecord.getApiParamCiphertext());
				record.setApiParamPlaintext(sysApiCallFailureRecord.getApiParamPlaintext());
				record.setApiReturnInfo(JSONObject.toJSONString(result));
				record.setModuleName(sysApiCallFailureRecord.getModuleName());
				record.setRefId(sysApiCallFailureRecord.getRefId());
				record.setRetryCount(sysApiCallFailureRecord.getRetryCount() + 1);
				record.setRetryTime(new Date());
				record.setTargetUrl(sysApiCallFailureRecord.getTargetUrl());
				record.setUpdateTime(new Date());
				record.setUpdateUser(loginUserInfoHelper.getUserId());
				if (result != null && "1".equals(result.getCode())) {
					record.setRetrySuccess(1);
				} else {
					record.setRetrySuccess(0);
				}
				sysApiCallFailureRecordService.insert(record);
			}
			return Result.success();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Result.error("-99", e.getMessage());
		}
	}
}
