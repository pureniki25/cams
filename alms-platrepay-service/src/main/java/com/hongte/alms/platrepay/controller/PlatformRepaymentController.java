package com.hongte.alms.platrepay.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentConfirmLog;
import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.TdrepayRechargeDetail;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanPayedTypeEnum;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.DepartmentBankService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.service.RepaymentProjFactRepayService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.base.service.TdrepayRechargeService;
import com.hongte.alms.base.service.TuandaiProjectInfoService;
import com.hongte.alms.base.vo.compliance.TdrepayRechargeInfoVO;
import com.hongte.alms.common.result.Result;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
    private LoginUserInfoHelper loginUserInfoHelper;

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
     * @param projectId    上标项目编号
     * @param afterId      总批次期数，唯一，对应信贷系统的还款计划编号
     * @param confirmLogId 还款确认日志记录
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
    	String projectId = (String) paramMap.get("projectId");
    	String afterId = (String) paramMap.get("afterId");
    	String confirmLogId = (String) paramMap.get("confirmLogId");
    	
    	LOGGER.info("@对接合规还款接口 开始 @输入参数 projectId:[{}]  afterId[{}]", projectId, afterId);
        //参数验证
        if (StringUtils.isBlank(projectId)) {
            return Result.error("上标项目编号不能为空");
        }
        if (StringUtils.isBlank(afterId)) {
            return Result.error("总批次期数不能为空");
        }
        if (StringUtils.isBlank(confirmLogId)) {
            return Result.error("还款确认日志记录Id不能为空");
        }
        try {
            //****************************************************
            //验证这块以后要移除,通用功能不能只针对某一个平台，目前只针对团贷平台. zgh 20180614
            TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("project_id", projectId));
            if (tuandaiProjectInfo == null) {
                LOGGER.error("@对接合规还款接口@  查不到平台的上标信息 输入参数 projectId:[{}]  ", projectId);
                return Result.error("查不到平台的上标信息");
            }
            //****************************************************

            //查业务信息
            BasicBusiness basicBusiness = basicBusinessService.selectById(tuandaiProjectInfo.getBusinessId());

            //查标的还款计划
            RepaymentProjPlan repaymentProjPlan = repaymentProjPlanService.selectOne(
                    new EntityWrapper<RepaymentProjPlan>()
                            .eq("project_id", projectId)
            );
            if (repaymentProjPlan == null) {
                LOGGER.error("@对接合规还款接口@  查不到标的还款计划信息 输入参数 projectId:[{}]  ", projectId);
                return Result.error("查不到标的还款计划信息");
            }

            //查标的还款期数信息并验证
            List<RepaymentProjPlanList> repaymentProjPlanLists = repaymentProjPlanListService.selectList(
                    new EntityWrapper<RepaymentProjPlanList>()
                            .eq("proj_plan_id", repaymentProjPlan.getProjPlanId())
                            .eq("after_id", afterId)
            );

            if (repaymentProjPlanLists == null || repaymentProjPlanLists.size() == 0) {
                LOGGER.error("@对接合规还款接口@  查不到标的还款计划列表信息 输入参数 projectId:[{}]  afterId[{}] ", projectId, afterId);
                return Result.error("500", "查不到标的还款计划列表信息");
            }
            if (repaymentProjPlanLists.size() > 1) {
                LOGGER.error("@对接合规还款接口@  查到两条以上标的还款计划列表信息 输入参数 projectId:[{}]  afterId[{}] ", projectId, afterId);
                return Result.error("500", "查到两条以上标的还款计划列表信息");
            }
            RepaymentProjPlanList repaymentProjPlanList = repaymentProjPlanLists.get(0);

            //判断是否已经到款，到款后再还款到平台
//            if (repaymentProjPlanList.getRepayFlag().equals(RepayPlanPayedTypeEnum.PAYING.getValue())
//                    || repaymentProjPlanList.getRepayFlag().equals(RepayPlanPayedTypeEnum.RENEW_PAY.getValue())) {
            if (repaymentProjPlanList.getRepayFlag().equals(RepayPlanPayedTypeEnum.PAYING.getValue())) {
                LOGGER.error("@对接合规还款接口@  此还款标的计划列表未还款 输入参数 projectId:[{}]  afterId{[]}  ", projectId, afterId);
                return Result.error("500", "此还款标的计划列表未还款");
            }

            RepaymentConfirmLog repaymentConfirmLog = repaymentConfirmLogService.selectById(confirmLogId);
            if (repaymentConfirmLog == null) {
                LOGGER.error("@对接合规还款接口@ 查不到指定的还款日志记录 输入参数confirmLogId:[{}]", confirmLogId);
                return Result.error("查不到指定的还款日志记录");
            }

            RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService.selectOne(
                    new EntityWrapper<RepaymentBizPlanList>()
                            .eq("business_id", repaymentConfirmLog.getBusinessId())
//                            .eq("orig_business_id", repaymentConfirmLog.getOrgBusinessId())
                            .eq("after_id", repaymentConfirmLog.getAfterId())
            );


            TdrepayRechargeInfoVO vo = new TdrepayRechargeInfoVO();
            vo.setProjectId(projectId);
            //业务所属资产端，1、鸿特信息，2、 一点车贷
            vo.setAssetType(1);
            //原业务编号
            vo.setOrigBusinessId(repaymentProjPlan.getOriginalBusinessId());
            //http://wiki.hongte.info/pages/viewpage.action?pageId=3867818
            //业务类型： 业主信用贷用信 -> 业主贷;  小微企业贷用信 -> 商银贷; tb_tuandai_project_info的project_id与master_issue_id相等则 -> 商贸共借， 业主共借同理
            //目标类型： 业务类型(1:车易贷展期,2:房速贷展期,3:金融仓储,4:三农金融,9:车易贷,11:房速贷,12车全垫资代采,13:扶贫贷,14:汽车融资租赁,15:二手车商贷,20:一点车贷,25:商贸贷,26:业主贷,27:家装分期,28:商贸共借;29:业主共借)
            Integer businessType = basicBusiness.getBusinessType();
            // 25 信用贷 特殊处理
            if (businessType == 25 ){
                switch (basicBusiness.getBusinessCtype()){
                    case "小微企业贷用信":
                        if(tuandaiProjectInfo.getProjectId().equals(tuandaiProjectInfo.getMasterIssueId())){
                            businessType = 28;
                        }else{
                            businessType = 30;
                        }
                        break;
                    case "业主信用贷用信":
                        if(tuandaiProjectInfo.getProjectId().equals(tuandaiProjectInfo.getMasterIssueId())){
                            businessType = 29;
                        }else{
                            businessType = 26;
                        }
                        break;
                }
            }
            vo.setBusinessType(businessType);

            //实还日期
            vo.setFactRepayDate(repaymentConfirmLog.getRepayDate());
            //借款人
            vo.setCustomerName(basicBusiness.getCustomerName());
            //分公司
            vo.setCompanyName(basicBusiness.getCompanyName());
            //处理转换 还款来源，10：线下转账，11:用往期结余还款(归类到线下转账吧),20：线下代扣，30：银行代扣
            //到接口 还款来源，1:线下转账,2:第三方代扣,3:银行代扣,4:APP网关充值,5:协议代扣
            vo.setRepaySource(RepayPlanPayedTypeEnum.getByValue(repaymentConfirmLog.getRepaySource()).getClassifyId());
            vo.setConfirmTime(repaymentBizPlanList.getFinanceComfirmDate());
            vo.setAfterId(afterId);
            vo.setPeriod(repaymentConfirmLog.getPeriod());

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
                //case 50: projPlanStatus = 11; break;
                default:
                    //查标的还款期数信息并验证
                    RepaymentProjPlanList lastRepaymentProjPlanList = repaymentProjPlanListService.selectOne(
                            new EntityWrapper<RepaymentProjPlanList>()
                                    .eq("proj_plan_id", repaymentProjPlan.getProjPlanId())
                                    .orderBy("period", false)
                    );
                    if (lastRepaymentProjPlanList != null && lastRepaymentProjPlanList.getDueDate() != null && repaymentConfirmLog.getRepayDate() != null) {
                        if (lastRepaymentProjPlanList.getDueDate().before(repaymentConfirmLog.getRepayDate())) {
                            projPlanStatus = 11;
                        }
                    }
                    break;
            }
            //标的还款计划结清状态
            vo.setSettleType(projPlanStatus);

            //计算费用: proj_fact_repay中要按project_id分组来进行计算,不要按期数计算
            List<RepaymentProjFactRepay> projFactRepays = repaymentProjFactRepayService.selectList(
                    new EntityWrapper<RepaymentProjFactRepay>()
                            .eq("confirm_log_id", confirmLogId)
                            .eq("project_id", projectId)
            );
            if (projFactRepays != null && projFactRepays.size() > 0) {
                //流水合计
                BigDecimal resourceAmount = BigDecimal.ZERO;
                //实收总金额
                BigDecimal factRepayAmount = BigDecimal.ZERO;
                //充值金额
                BigDecimal rechargeAmount = BigDecimal.ZERO;

                List<TdrepayRechargeDetail> detailFeeList = Lists.newArrayList();


                /**
                 * 累计费用，线下（资产公司）的滞纳金排除在外，注意用value和uuid去区分.  OVER_DUE_AMONT_UNDERLINE(60,"线下滞纳金","3131c075-5721-11e8-8a00-0242ac110002",5)
                 * {@link com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum }
                 */
                for (RepaymentProjFactRepay r : projFactRepays) {
                    if (RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getValue().equals(r.getPlanItemType())
                            || RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid().equals(r.getFeeId())) {
                        continue;
                    }
                    resourceAmount = resourceAmount.add(r.getFactAmount());
                    factRepayAmount = factRepayAmount.add(r.getFactAmount());
                    rechargeAmount = rechargeAmount.add(r.getFactAmount());

                    TdrepayRechargeDetail detailFee = new TdrepayRechargeDetail();

                    //要对费类型进行转换：
                    //从 应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收
                    //转成 费用类型(10:本金,20:利息;30:平台服务费;40:资产端服务费;50:担保公司服务费;60:仲裁服务费;70:逾期费用（罚息）;80: 中介服务费)
                    Integer feeType = 10;
                    switch (r.getPlanItemType()){
                        case 10: feeType = 10; break;
                        case 20: feeType = 20; break;
                        case 30: feeType = 40; break;
                        case 40: feeType = 50; break;
                        case 50: feeType = 30; break;
                        case 60: feeType = 70; break;
                        //case 70: planItemType = ?; break;
                        case 80: feeType = 80; break;
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
                vo.setResourceAmount(resourceAmount);
                vo.setFactRepayAmount(factRepayAmount);
                vo.setRechargeAmount(rechargeAmount);
                vo.setDetailList(detailFeeList);
            }


            //当期还款状态，目前只有三种，分别为 还款中，逾期，已还款 => 当期结清状态 0：未结清,1：已结清
            Integer projPlanListStatus = 0;
            switch (repaymentProjPlanList.getCurrentStatus()) {
                case "还款中":
                case "逾期":
                    projPlanListStatus = 0;
                    break;
                case "已还款":
                    projPlanListStatus = 1;
                    break;
            }
            vo.setIsComplete(projPlanListStatus);
            vo.setProjPlanListId(repaymentProjPlanList.getProjPlanListId());
            if (StringUtils.isBlank(tuandaiProjectInfo.getTdUserId())) {
                LOGGER.error("@对接合规还款接口@ 团贷用户ID(资金端用户ID)为空 project_id:[{}]", projectId);
                return Result.error("团贷用户ID(资金端用户ID)为空");
            } else {
                vo.setTdUserId(tuandaiProjectInfo.getTdUserId());
            }
            vo.setConfirmLogId(confirmLogId);

            Result result = tdrepayRechargeController.accessTdrepayReCharge(vo);
            if ("-500".equals(result.getCode())) {
                return Result.error("合规还款失败");
            }
            //return Result.success(departmentBankService.listDepartmentBank());
            return Result.success();
        } catch (Exception e) {
            LOGGER.error("通过合化还款接口还款失败.", e);
            return Result.error("500", "通过合化还款接口还款失败！");
        }
    }
}
