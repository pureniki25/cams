package com.hongte.alms.platrepay.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanPayedTypeEnum;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.vo.compliance.TdrepayRechargeInfoVO;
import com.hongte.alms.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * 平台合规化还款服务控制器
 *
 * @author 张贵宏
 * @date 2018/6/14 10:37
 */
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
        String projPlanListId = (String)paramMap.get("projPlanListId");
//        String projectId = (String) paramMap.get("projectId");
//        String afterId = (String) paramMap.get("afterId");
        //String confirmLogId = (String) paramMap.get("confirmLogId");

        LOGGER.info("@对接合规还款接口 开始 @输入参数 projPlanListId:[{}] ", projPlanListId);
        //参数验证
        if (StringUtils.isBlank(projPlanListId)) {
            return Result.error("标的还款计划列表Id不能为空");
        }
//        if (StringUtils.isBlank(afterId)) {
//            return Result.error("总批次期数不能为空");
//        }
//        if (StringUtils.isBlank(confirmLogId)) {
//            return Result.error("还款确认日志记录Id不能为空");
//        }
        try {
//            //****************************************************
//            //验证这块以后要移除,通用功能不能只针对某一个平台，目前只针对团贷平台. zgh 20180614
//            TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService.selectById(projectId);
//            if (tuandaiProjectInfo == null) {
//                LOGGER.error("@对接合规还款接口@  查不到平台的上标信息 输入参数 projectId:[{}]  ", projectId);
//                return Result.error("查不到平台的上标信息");
//            }
//            //****************************************************
//
//            //查业务信息
//            BasicBusiness basicBusiness = basicBusinessService.selectById(tuandaiProjectInfo.getBusinessId());
//
//            //查标的还款计划
//            RepaymentProjPlan repaymentProjPlan = repaymentProjPlanService.selectOne(
//                    new EntityWrapper<RepaymentProjPlan>()
//                            .eq("project_id", projectId)
//            );
//            //RepaymentProjPlan projectPlan = repaymentProjPlanService.selectById(repaymentProjPlanList.getProjPlanId());
//            if (repaymentProjPlan == null) {
//                LOGGER.error("@对接合规还款接口@  查不到标的还款计划信息 输入参数 projectId:[{}]  ", projectId);
//                return Result.error("查不到标的还款计划信息");
//            }

            //查标的还款期数信息并验证
            List<RepaymentProjPlanList> repaymentProjPlanLists = repaymentProjPlanListService.selectList(
                    new EntityWrapper<RepaymentProjPlanList>()
                            .eq("proj_plan_list_id", projPlanListId)
            );

            if (repaymentProjPlanLists == null || repaymentProjPlanLists.size() == 0) {
                LOGGER.error("@对接合规还款接口@  查不到标的还款计划列表信息 输入参数 projPlanListId:[{}]  ", projPlanListId);
                return Result.error("500", "查不到标的还款计划列表信息");
            }
            if (repaymentProjPlanLists.size() > 1) {
                LOGGER.error("@对接合规还款接口@  查到两条以上标的还款计划列表信息 输入参数 projPlanListId:[{}]   ", projPlanListId);
                return Result.error("500", "查到两条以上标的还款计划列表信息");
            }
            RepaymentProjPlanList repaymentProjPlanList = repaymentProjPlanLists.get(0);
            //查业务信息
            BasicBusiness basicBusiness = basicBusinessService.selectById(repaymentProjPlanList.getBusinessId());
            //查标的还款计划
            RepaymentProjPlan repaymentProjPlan = repaymentProjPlanService.selectOne(
                    new EntityWrapper<RepaymentProjPlan>()
                            .eq("proj_plan_id", repaymentProjPlanList.getProjPlanId())
            );
            if (repaymentProjPlan == null) {
                LOGGER.error("@对接合规还款接口@  查不到标的还款计划信息 输入参数 projPlanListId:[{}]  ", projPlanListId);
                return Result.error("查不到标的还款计划信息");
            }
            //****************************************************
            //验证这块以后要移除,通用功能不能只针对某一个平台，目前只针对团贷平台. zgh 20180614
            TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService.selectById(repaymentProjPlan.getProjectId());
            if (tuandaiProjectInfo == null) {
                LOGGER.error("@对接合规还款接口@  查不到平台的上标信息 输入参数 projPlanListId:[{}]  ", projPlanListId);
                return Result.error("查不到平台的上标信息");
            }
            //****************************************************

/*            RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService.selectOne(
                    new EntityWrapper<RepaymentBizPlanList>()
                            .eq("business_id", repaymentConfirmLog.getBusinessId())
//                            .eq("orig_business_id", repaymentConfirmLog.getOrgBusinessId())
                            .eq("after_id", repaymentConfirmLog.getAfterId())
            );*/

            RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService.selectOne(
                    new EntityWrapper<RepaymentBizPlanList>()
                            .eq("business_id", repaymentProjPlanList.getBusinessId())
//                            .eq("orig_business_id", repaymentConfirmLog.getOrgBusinessId())
                            .eq("after_id", repaymentProjPlanList.getAfterId())
            );

            //部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款
            //新需求： 资产端内部在分润后将还款状态改为线上已还款之后，再调用合规化还款接口去给资金端还款
            if (repaymentBizPlanList.getRepayStatus() != 2 && repaymentBizPlanList.getRepayStatus() != 3) {
                LOGGER.error("@对接合规还款接口@  应该在还完线上部分后再调用合规化还款接口 输入参数 projPlanListId:[{}]  ", projPlanListId);
                return Result.error("500", "应该在还完线上部分后再调用合规化还款接口");
            }

            //判断是否已经到款，到款后再还款到平台
//            if (repaymentProjPlanList.getRepayFlag().equals(RepayPlanPayedTypeEnum.PAYING.getValue())
//                    || repaymentProjPlanList.getRepayFlag().equals(RepayPlanPayedTypeEnum.RENEW_PAY.getValue())) {
//            if (repaymentProjPlanList.getRepayFlag() == null || repaymentProjPlanList.getRepayFlag().equals(RepayPlanPayedTypeEnum.PAYING.getValue())) {
//                LOGGER.error("@对接合规还款接口@  此还款标的计划列表未还款 输入参数 projectId:[{}]  afterId{[]}  ", projectId, afterId);
//                return Result.error("500", "此还款标的计划列表未还款");
//            }

            //临时取消已还款状态验证

//            if (!"已还款".equals(repaymentProjPlanList.getCurrentStatus())) {
//                LOGGER.error("@对接合规还款接口@  此还款标的计划列表未还款 输入参数 projectId:[{}]  afterId{[]}  ", projectId, afterId);
//                return Result.error("500", "此还款标的计划列表未还款");
//            }

          /*  RepaymentConfirmLog repaymentConfirmLog = repaymentConfirmLogService.selectById(confirmLogId);
            if (repaymentConfirmLog == null) {
                LOGGER.error("@对接合规还款接口@ 查不到指定的还款日志记录 输入参数confirmLogId:[{}]", confirmLogId);
                return Result.error("查不到指定的还款日志记录");
            }*/


            TdrepayRechargeInfoVO vo = new TdrepayRechargeInfoVO();
            vo.setProjectId(repaymentProjPlanList.getProjPlanListId());
            //业务所属资产端，1、鸿特信息，2、 一点车贷
            vo.setAssetType(1);
            //原业务编号
            vo.setOrigBusinessId(repaymentProjPlanList.getOrigBusinessId());
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
            //vo.setFactRepayDate(repaymentConfirmLog.getRepayDate());
            vo.setFactRepayDate(repaymentBizPlanList.getFactRepayDate());
            //借款人,平台只关心标的的每个共借款人姓名
            //vo.setCustomerName(basicBusiness.getCustomerName());
            vo.setCustomerName(tuandaiProjectInfo.getRealName());
            //分公司
            vo.setCompanyName(basicBusiness.getCompanyName());
            //还款来源
            vo.setRepaySource(RepayPlanPayedTypeEnum.getByValue(repaymentProjPlanList.getRepayFlag()).getClassifyId());

            //取还款确认日志的最后一次的来源做为整个业务的还款来源
            //处理转换 还款来源，10：线下转账，11:用往期结余还款(归类到线下转账吧),20：线下代扣，30：银行代扣
            //到接口 还款来源，1:线下转账,2:第三方代扣,3:银行代扣,4:APP网关充值,5:协议代扣
//            RepaymentConfirmLog lastRepaymentConfirmLog = repaymentConfirmLogService.selectOne(
//                    new EntityWrapper<RepaymentConfirmLog>().eq("business_id", repaymentProjPlanList.getBusinessId()).eq("after_id", afterId).orderBy("repay_date", false)
//            );
            /*if (repaymentConfirmLog.getRepaySource() == null) {
                LOGGER.error("@对接合规还款接口@ 指定的还款日志记录的还款来源为空 输入参数confirmLogId:[{}]", confirmLogId);
                return Result.error("指定的还款日志记录的还款来源为空");
            } else {
                vo.setRepaySource(RepayPlanPayedTypeEnum.getByValue(repaymentConfirmLog.getRepaySource()).getClassifyId());
            }*/

//            if (lastRepaymentConfirmLog != null && lastRepaymentConfirmLog.getRepaySource() == null) {
//                LOGGER.error("@对接合规还款接口@ 获取最后一次还款日志为空");
//                return Result.error("获取最后一次还款日志为空");
//            } else {
//                vo.setRepaySource(RepayPlanPayedTypeEnum.getByValue(repaymentProjPlanList.getRepayFlag()).getClassifyId());
//            }


            if (repaymentBizPlanList.getFinanceComfirmDate() == null) {
                LOGGER.error("@对接合规还款接口@ 财务确认还款操作日期为空 输入参数plan_list_id:[{}]", repaymentBizPlanList.getPlanListId());
                return Result.error("财务确认还款操作日期为空");
            } else {
                vo.setConfirmTime(repaymentBizPlanList.getFinanceComfirmDate());
            }
            vo.setAfterId(repaymentBizPlanList.getAfterId());
//            vo.setPeriod(repaymentConfirmLog.getPeriod());
            vo.setPeriod(repaymentBizPlanList.getPeriod());

            //处理标的计划结清状态
            //还款计划状态，0:还款中，10:提前结清，20:已结清，30:亏损结清，50:已申请展期  => 0：非结清，10：正常结清，11：逾期结清，20：展期原标结清，30：坏账结清
            Integer projPlanStatus = 0;
            switch (repaymentProjPlan.getPlanStatus()) {
                case 0:
                    projPlanStatus = 0;
                    break;
                case 10:
                case 20:
                    projPlanStatus = 10;
                    break;
                case 30:
                    projPlanStatus = 30;
                    break;
                case 50:
                    projPlanStatus = 20;
                    break;
                default:
                    LOGGER.error("@对接合规还款接口@  标的计划结清状态值plan_status:[{}]错误 输入参数 projPlanListId:[{}] ", repaymentProjPlan.getPlanStatus(), projPlanListId);
                    return Result.error("500", "标的计划结清状态值错误");
//                default:
//                    //查标的还款期数信息并验证
//                    RepaymentProjPlanList lastRepaymentProjPlanList = repaymentProjPlanListService.selectOne(
//                            new EntityWrapper<RepaymentProjPlanList>()
//                                    .eq("proj_plan_id", repaymentProjPlan.getProjPlanId())
//                                    .orderBy("period", false)
//                    );
//                    if (lastRepaymentProjPlanList != null && lastRepaymentProjPlanList.getDueDate() != null && repaymentConfirmLog.getRepayDate() != null) {
//                        if (lastRepaymentProjPlanList.getDueDate().before(repaymentConfirmLog.getRepayDate())) {
//                            projPlanStatus = 11;
//                        }
//                    }*//*
//                    if (repaymentProjPlanList.getDueDate().before(repaymentBizPlanList.getFactRepayDate())) {
//                        projPlanStatus = 11;
//                    } else {
//                        LOGGER.error("@对接合规还款接口@  标的计划结清状态值错误 输入参数 projectId:[{}]  afterId[{}] ", projectId, afterId);
//                        return Result.error("500", "标的计划结清状态值错误");
//                    }
//                    break;
            }
            //标的还款计划结清状态
            vo.setSettleType(projPlanStatus);

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
                    new EntityWrapper<RepaymentProjFactRepay>().eq("proj_plan_list_id", repaymentProjPlanList.getProjPlanListId())
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
                    detailFee.setFeeValue(r.getFactAmount());
                    detailFeeList.add(detailFee);
                }
                vo.setFactRepayAmount(factRepayAmount);
                vo.setRechargeAmount(rechargeAmount);
                //用实还赋值流水金额
                vo.setResourceAmount(factRepayAmount);

                //vo.setResourceAmount(resourceAmount);
                vo.setDetailList(detailFeeList);
            }


            //当期还款状态，目前只有三种，分别为 还款中，逾期，已还款 => 当期结清状态 0：未结清,1：已结清
            //如果 current_status为 还款中，逾期则继续判断repay_status==> 2:线上已还款,3:全部已还款 则为结清
            Integer projPlanListStatus = 0;
            switch (repaymentProjPlanList.getCurrentStatus()) {
                case "还款中":
                case "逾期":
                    if (repaymentBizPlanList.getRepayStatus() == 2 || repaymentBizPlanList.getRepayStatus() == 3) {
                        projPlanListStatus = 1;
                    } else {
                        projPlanListStatus = 0;
                    }
                    break;
                case "已还款":
                    projPlanListStatus = 1;
                    break;
                default:
                    LOGGER.error("@对接合规还款接口@  标的计划本期状态值current_status:[{}]错误 输入参数 projPlanListId:[{}] ", repaymentProjPlanList.getCurrentStatus(), projPlanListId);
                    return Result.error("500", "标的计划本期状态值错误");
            }
            vo.setIsComplete(projPlanListStatus);
            vo.setProjPlanListId(repaymentProjPlanList.getProjPlanListId());
            if (StringUtils.isBlank(tuandaiProjectInfo.getTdUserId())) {
                LOGGER.error("@对接合规还款接口@ 团贷用户ID(资金端用户ID)为空 projPlanListId:[{}]", projPlanListId);
                return Result.error("团贷用户ID(资金端用户ID)为空");
            } else {
                vo.setTdUserId(tuandaiProjectInfo.getTdUserId());
            }
            //vo.setConfirmLogId(confirmLogId);
            vo.setConfirmLogId(repaymentBizPlanList.getPlanListId());

            Result result = tdrepayRechargeController.accessTdrepayReCharge(vo);
            if (!"1".equals(result.getCode())) {
                LOGGER.error("@对接合规还款接口@ 调用代充值资金分发参数接入接口失败 vo:[{}]", JSON.toJSONString(vo));
                return Result.error("合规还款失败:" + result.getMsg());
            }
            //return Result.success(departmentBankService.listDepartmentBank());
            return Result.success();
        } catch (Exception e) {
            LOGGER.error("通过合化还款接口还款失败.", e);
            return Result.error("500", "通过合化还款接口还款失败！" + e.getMessage());
        }
    }
}
