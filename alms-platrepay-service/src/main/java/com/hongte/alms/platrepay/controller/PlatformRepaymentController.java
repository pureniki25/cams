package com.hongte.alms.platrepay.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hongte.alms.base.dto.compliance.TdPlatformPlanRepaymentDTO;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.ProfitFeeSet;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.entity.SysApiCallFailureRecord;
import com.hongte.alms.base.entity.TdrepayRechargeDetail;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.enums.AlmsServiceNameEnums;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanRepaySrcEnum;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.DepartmentBankService;
import com.hongte.alms.base.service.ProfitFeeSetService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.service.RepaymentConfirmPlatRepayLogService;
import com.hongte.alms.base.service.RepaymentProjFactRepayService;
import com.hongte.alms.base.service.RepaymentProjPlanListDetailService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.base.service.RepaymentResourceService;
import com.hongte.alms.base.service.SysApiCallFailureRecordService;
import com.hongte.alms.base.service.TdrepayRechargeService;
import com.hongte.alms.base.service.TuandaiProjectInfoService;
import com.hongte.alms.base.vo.compliance.TdrepayRechargeInfoVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.platrepay.dto.TdGuaranteePaymentDTO;
import com.hongte.alms.platrepay.dto.TdProjectPaymentDTO;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 平台合规化还款服务控制器
 *
 * @author 张贵宏
 * @date 2018/6/14 10:37
 */
// @CrossOrigin
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

	// @Autowired
	// @Qualifier("RepaymentProjFactRepayService")
	// RepaymentProjFactRepayService repaymentProjFactRepayService;

	// @Qualifier("TdrepayRechargeController")
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
	RepaymentConfirmPlatRepayLogService repaymentConfirmPlatRepayLogService;

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	static final ConcurrentMap<Integer, String> FEE_TYPE_MAP;

	@Autowired
	@Qualifier("ProfitFeeSetService")
	private ProfitFeeSetService profitFeeSetService;

	@Autowired
	@Qualifier("RepaymentProjPlanListDetailService")
	private RepaymentProjPlanListDetailService repaymentProjPlanListDetailService;

	static {

		// 要对费类型进行转换：
		// 从
		// 应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收
		// 转成 费用类型(10:本金,20:利息;30:平台服务费;40:资产端服务费;50:担保公司服务费;60:仲裁服务费;70:逾期费用（罚息）;80:
		// 中介服务费)
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
	 * @return com.hongte.alms.common.result.Result Rest接口请求结果对象
	 * @author 张贵宏
	 * @date 2018/6/14 10:30
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "对接合规还款接口")
	@PostMapping("/repayment")
	@ResponseBody
	public Result repayment(@RequestBody Map<String, Object> paramMap) { // TdrepayRechargeInfoVO
		if (paramMap == null || paramMap.isEmpty()) {
			return Result.error("参数不能为空");
		}
		String projPlanListId = (String) paramMap.get("projPlanListId");
		LOGGER.info("@对接合规还款接口 开始 @输入参数 projPlanListId:[{}] ", projPlanListId);
		// 参数验证
		if (StringUtils.isBlank(projPlanListId)) {
			return Result.error("标的还款计划列表Id不能为空");
		}
		try {
			/*
			 * 获取当期的标的还款信息列表信息
			 */
			RepaymentProjPlanList projPlanList = repaymentProjPlanListService.selectById(projPlanListId);
			if (projPlanList == null) {
				LOGGER.info("@对接合规还款接口@  查不到标的还款计划列表信息 输入参数 projPlanListId:[{}]  ", projPlanListId);
				return Result.error("查不到标的还款计划列表信息");
			}

			/*
			 * 查询当期业务维度的还款计划列表
			 */
			RepaymentBizPlanList bizPlanList = repaymentBizPlanListService.selectById(projPlanList.getPlanListId());
			if (bizPlanList == null) {
				LOGGER.info("@对接合规还款接口@  查不到业务维度的还款计划列表信息 输入参数 planListId:[{}]  ", projPlanList.getPlanListId());
				return Result.error("查不到业务维度的还款计划列表信息");
			}

			/*
			 * 查标的还款计划
			 */
			RepaymentProjPlan projPlan = repaymentProjPlanService.selectById(projPlanList.getProjPlanId());

			if (projPlan == null) {
				LOGGER.info("@对接合规还款接口@  查不到标的还款计划信息 输入参数 projPlanListId:[{}]  ", projPlanListId);
				return Result.error("查不到标的还款计划信息");
			}

			// ****************************************************
			// 验证这块以后要移除,通用功能不能只针对某一个平台，目前只针对团贷平台. zgh 20180614
			TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService.selectById(projPlan.getProjectId());
			if (tuandaiProjectInfo == null) {
				LOGGER.info("@对接合规还款接口@  查不到平台的上标信息 输入参数 projPlanListId:[{}]  ", projPlanListId);
				return Result.error("查不到平台的上标信息");
			}
			// ****************************************************

			/*
			 * 1）资产端内部在分润后将先还完线上部分之后再进行合规化还款，即部分还款子状态为2或者3时 2）repayStatus
			 * 部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款
			 */
			Integer repayStatus = projPlanList.getRepayStatus();
			if (repayStatus == null || repayStatus.intValue() == 1) {
				LOGGER.info("@对接合规还款接口@  应该在还完线上部分后再调用合规化还款接口 输入参数 projPlanListId:[{}]  ", projPlanListId);
				return Result.error("应该在还完线上部分后再调用合规化还款接口");
			}

			TdrepayRechargeInfoVO vo = new TdrepayRechargeInfoVO();
			vo.setProjectId(projPlan.getProjectId());
			// 业务所属资产端，1、鸿特信息，2、 一点车贷
			vo.setAssetType(1);
			// 原业务编号
			vo.setOrigBusinessId(projPlanList.getOrigBusinessId());

			/*
			 * 查业务信息，判断业务类型
			 * 业务类型(1:车易贷展期,2:房速贷展期,3:金融仓储,4:三农金融,9:车易贷,11:房速贷,12车全垫资代采,13:扶贫贷,14:汽车融资租赁,15:
			 * 二手车商贷,20:一点车贷,25:商贸贷,26:业主贷,27:家装分期,28:商贸共借;29:业主共借)
			 * 规则根据：http://wiki.hongte.info/pages/viewpage.action?pageId=3867818 信用贷业务类型：
			 * 业主信用贷用信 -> 业主贷; 小微企业贷用信 -> 商银贷
			 * tb_tuandai_project_info的project_id与master_issue_id相等则 -> 商贸共借， 业主共借同理
			 */
			BasicBusiness basicBusiness = basicBusinessService.selectById(projPlanList.getOrigBusinessId());
			if (basicBusiness == null) {
				LOGGER.info("@对接合规还款接口@  查不到业务基础信息 输入参数 origBusinessId:[{}]  ", projPlanList.getOrigBusinessId());
				return Result.error("查不到业务基础信息");
			}
			Integer businessType = basicBusiness.getBusinessType();
			if (businessType == null) {
				LOGGER.info("@对接合规还款接口@  没有找到业务类型 输入参数 origBusinessId:[{}]  ", projPlanList.getOrigBusinessId());
				return Result.error("没有找到业务类型");
			}

			// 25 信用贷 特殊处理
			if (businessType.intValue() == 25) {
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
				default:
					break;
				}
			} else if ((businessType.intValue() == 1 || businessType.intValue() == 9)
					&& !tuandaiProjectInfo.getProjectId().equals(tuandaiProjectInfo.getMasterIssueId())) {
				businessType = 31;
			} else if ((businessType.intValue() == 2 || businessType.intValue() == 11 || businessType.intValue() == 35)
					&& !tuandaiProjectInfo.getProjectId().equals(tuandaiProjectInfo.getMasterIssueId())) {
				businessType = 32;
			} else if (businessType.intValue() == 20
					&& !tuandaiProjectInfo.getProjectId().equals(tuandaiProjectInfo.getMasterIssueId())) {
				businessType = 33;
			}
			vo.setBusinessType(businessType);

			// 实还日期
			vo.setFactRepayDate(projPlanList.getFactRepayDate());
			// 借款人,平台只关心标的的每个共借款人姓名
			vo.setCustomerName(tuandaiProjectInfo.getRealName());
			// 分公司
			vo.setCompanyName(basicBusiness.getCompanyName());

			/*
			 * 根据 projPlanListId 获取实还明细 ；
			 */
			List<RepaymentProjFactRepay> projFactRepayList = repaymentProjFactRepayService
					.selectList(new EntityWrapper<RepaymentProjFactRepay>().eq("proj_plan_list_id", projPlanListId)
							.eq("is_cancelled", 0).orderBy("create_date", false));
			if (projFactRepayList == null || projFactRepayList.isEmpty()) {
				LOGGER.info("@对接合规还款接口@ 查询实还流水为空 输入参数projPlanListId:[{}]", projPlanListId);
				return Result.error("查询实还流水为空");
			}

			/*
			 * 1）还款来源取值说明：（资产端内部在分润后将还款状态改为线上已还款之后，再调用合规化还款接口去给资金端还款，
			 * 如果repay_status为2线上已还款时repayFlog是没有值的，所以要从实还流水中查还款来源），取最后一条实还流水记录的还款来源
			 * 2）部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款
			 */
			vo.setRepaySource(
					RepayPlanRepaySrcEnum.getByValue(projFactRepayList.get(0).getRepaySource()).getPlatRepayVal());

			if (bizPlanList.getFinanceComfirmDate() == null) {
				LOGGER.info("@对接合规还款接口@ 财务确认还款操作日期为空 输入参数plan_list_id:[{}]", bizPlanList.getPlanListId());
				return Result.error("财务确认还款操作日期为空");
			} else {
				vo.setConfirmTime(bizPlanList.getFinanceComfirmDate());
			}
			vo.setAfterId(projPlanList.getAfterId());
			vo.setPeriod(projPlanList.getPeriod());

			/*
			 * 结清状态取值说明： （还款计划状态，0:还款中，10:提前结清，20:已结清，30:亏损结清，40:坏账结清, 50:已申请展期
			 * 平台还款接口状态，0：非结清，10：正常结清，11：逾期结清，20：展期原标结清，30：坏账结清）
			 */
			Integer tempProjPlanStatus = null; // 待赋值平台还款状态
			Integer planStatus = projPlan.getPlanStatus(); // 还款计划状态
			if (planStatus == null) {
				LOGGER.info("@对接合规还款接口@ 还款计划状态planStatus为空 输入参数plan_list_id:[{}]", projPlan.getProjPlanId());
				return Result.error("还款计划状态为空，请检查还款计划是否完整");
			}
			switch (planStatus.intValue()) {
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
				LOGGER.info("@对接合规还款接口@  标的计划结清状态值plan_status:[{}]错误 输入参数 projPlanListId:[{}] ", planStatus,
						projPlanListId);
				return Result.error("500", "标的计划结清状态值错误");
			}

			// 标的还款计划结清状态
			vo.setSettleType(tempProjPlanStatus);

			/*
			 * 肖莹环提出充值金额计算规则：利息以平台应还利息为准，其他费用项以资产端数据为准。 说明：有垫付则按垫付传费用项，否则按查询的标的还未计划信息传费用项
			 * 2018-09-14 修改规则：本金以平台本金为准
			 */
			Map<String, Object> params = Maps.newHashMap();
			params.put("projectId", projPlan.getProjectId());

			BigDecimal amount = BigDecimal.ZERO; // 平台本金
			BigDecimal interestAmount = BigDecimal.ZERO; // 平台利息
			// 平台标的还款计划费用map

			/*
			 * 通过外联平台eip调用团贷查询标的还款计划信息
			 */
			com.ht.ussp.core.Result ret = eipRemote.queryRepaymentSchedule(params);
			LOGGER.info("查询平台标的还款计划，标id：{}；接口返回数据：{}", projPlan.getProjectId(), ret);

			if (ret != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(ret.getReturnCode()) && ret.getData() != null) {

				Map map = JSONObject.parseObject(JSONObject.toJSONString(ret.getData()), Map.class);

				if (map != null && map.get("repaymentScheduleList") != null) {
					List<TdPlatformPlanRepaymentDTO> dtos = JSONObject.parseArray(
							JSONObject.toJSONString(map.get("repaymentScheduleList")),
							TdPlatformPlanRepaymentDTO.class);

					if (CollectionUtils.isEmpty(dtos)) {
						LOGGER.info("@对接合规还款接口@  从平台获取标的还款计划为空 平台返回数据 [{}] ", ret);
						return Result.error("从平台获取标的还款计划为空");
					}

					for (TdPlatformPlanRepaymentDTO dto : dtos) {

						// 获取当前期数
						if (projPlanList.getPeriod() != null
								&& projPlanList.getPeriod().intValue() == dto.getPeriod()) {

							/*
							 * 平台还款接口费用类型(10:本金,20:利息;30:平台服务费;40:资产端服务费;50:担保公司服务费;60:仲裁服务费;70:逾期费用（罚息）;80:
							 * 中介服务费)
							 */

							amount = dto.getAmount();
							interestAmount = dto.getInterestAmount();

							if (vo.getSettleType().intValue() != 0) {
								Map<String, Object> paramMap2 = new HashMap<>();
								paramMap2.put("projectId", projPlan.getProjectId());
								paramMap2.put("orgType", 1);
								com.ht.ussp.core.Result queryRepaymentEarlierResult = eipRemote
										.queryRepaymentEarlier(paramMap2);
								LOGGER.info("提前结清平台费用查询，标id：{}；接口返回数据：{}", projPlan.getProjectId(),
										queryRepaymentEarlierResult);

								if (queryRepaymentEarlierResult != null
										&& Constant.REMOTE_EIP_SUCCESS_CODE
												.equals(queryRepaymentEarlierResult.getReturnCode())
										&& queryRepaymentEarlierResult.getData() != null) {

									JSONObject parseObject = (JSONObject) JSONObject
											.toJSON(queryRepaymentEarlierResult.getData());
									if (parseObject != null) {
										amount = BigDecimal.valueOf(parseObject.getDouble("principal"));
										interestAmount = BigDecimal.valueOf(parseObject.getDouble("interest"));
									}
								} else {
									LOGGER.info("@对接合规还款接口@  提前结清平台费用查询出错 平台返回数据 [{}] ", queryRepaymentEarlierResult);
									return Result.error("提前结清平台费用查询出错");
								}
							}

						}
					}
				} else {
					LOGGER.info(
							"@对接合规还款接口@  通过外联平台eip(查询标的还款计划:/repayment/queryRepaymentSchedule)调用团贷查询标的还款计划错误 输入参数 projectPlanId:[{}] projectId:[{}]  afterId[{}] ",
							projPlanListId, projPlan.getProjectId(), bizPlanList.getAfterId());
					return Result
							.error("通过外联平台eip(查询标的还款计划:/repayment/queryRepaymentSchedule)调用团贷查询标的还款计划错误, returnCode:"
									+ ret.getReturnCode() + ",msg:" + ret.getMsg() + ",codeDesc:" + ret.getCodeDesc());
				}

				/*
				 * 获取实还明细
				 */
				List<RepaymentProjFactRepay> projFactRepays = repaymentProjFactRepayService
						.selectList(new EntityWrapper<RepaymentProjFactRepay>()
								.eq("proj_plan_list_id", projPlanList.getProjPlanListId()).eq("is_cancelled", 0));

				// 按费用项大类plan_item_type把重复的费用项进行合并累加

				if (CollectionUtils.isNotEmpty(projFactRepays)) {

					// 实收总金额
					BigDecimal factRepayAmount = BigDecimal.ZERO;
					BigDecimal rechargeAmount = BigDecimal.ZERO;

					List<TdrepayRechargeDetail> detailFeeList = Lists.newArrayList();

					/*
					 * 获取不需要分润的数据
					 */
					Map<String, Integer> notShaPrMap = getNotShareProfitFeeIds(projPlanListId);
					boolean amountFlag = false;
					boolean interestAmountFlag = false;

					Map<Integer, TdrepayRechargeDetail> isUsedMap = new HashMap<>();

					for (RepaymentProjFactRepay r : projFactRepays) {
						// 累计实还金额，包含线下和线上费用
						factRepayAmount = factRepayAmount.add(r.getFactAmount());
						/*
						 * 区分分润与不分润的费用明细（线下费用不用分润）
						 */
						if (notShaPrMap.containsKey(r.getFeeId()) || r.getFactAmount().equals(BigDecimal.ZERO)) {
							continue;
						}
						TdrepayRechargeDetail detailFee = new TdrepayRechargeDetail();
						// 要对费类型进行转换，押金和冲应收不用分润：
						// 从
						// 应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收
						// 转成
						// 费用类型(10:本金,20:利息;30:平台服务费;40:资产端服务费;50:担保公司服务费;60:仲裁服务费;70:逾期费用（罚息）;80:中介服务费)
						Integer feeType = null;
						switch (r.getPlanItemType()) {
						case 10:
							feeType = 10;
							if (!amountFlag) {
								detailFee.setFeeValue(amount);
								amountFlag = true;
							} else {
								detailFee.setFeeValue(BigDecimal.ZERO);
							}
							break;
						case 20:
							feeType = 20;
							if (!interestAmountFlag) {
								detailFee.setFeeValue(interestAmount);
								interestAmountFlag = true;
							} else {
								detailFee.setFeeValue(BigDecimal.ZERO);
							}
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
						case 70:
							if (RepayPlanFeeTypeEnum.SUB_COMPANY_PENALTY.getUuid().equals(r.getFeeId())) {
								feeType = 40;
							} else if (RepayPlanFeeTypeEnum.PLAT_PENALTY.getUuid().equals(r.getFeeId())) {
								feeType = 30;
							}
							break;
						case 80:
							feeType = 80;
							break;
						default:
							break;
						}
						detailFee.setFeeType(feeType);
						detailFee.setFeeName(r.getPlanItemName());
						if (detailFee.getFeeValue() == null) {
							detailFee.setFeeValue(r.getFactAmount());
						}
						if (r.getRepaySource() != null) {
							if ((r.getRepaySource().intValue() != 30) && (r.getRepaySource().intValue() != 31)) {
								rechargeAmount = rechargeAmount.add(detailFee.getFeeValue());
							}
						} else {
							LOGGER.info("@对接合规还款接口@ 还款方式不能为空 projPlanListId:[{}]", projPlanListId);
							return Result.error("还款方式不能为空");
						}

						if (isUsedMap.containsKey(detailFee.getFeeType())) {
							TdrepayRechargeDetail detail = isUsedMap.get(detailFee.getFeeType());
							detail.setFeeValue(detail.getFeeValue()
									.add(detailFee.getFeeValue() == null ? BigDecimal.ZERO : detailFee.getFeeValue()));
							continue;
						} else {
							isUsedMap.put(detailFee.getFeeType(), detailFee);
						}

						detailFeeList.add(detailFee);
					}
					vo.setFactRepayAmount(factRepayAmount);
					vo.setRechargeAmount(rechargeAmount);

					/*
					 * 0：非结清，10：正常结清，11：逾期结清，20：展期原标结清，30：坏账结清 如果坏账结清，则充值金额等于实还金额
					 */
					if (vo.getSettleType().intValue() == 30) {
						vo.setRechargeAmount(vo.getFactRepayAmount());
					}

					// 用实还赋值流水金额
					vo.setResourceAmount(factRepayAmount);
					vo.setDetailList(detailFeeList);

				}

				// 当期还款状态，目前只有三种，分别为 还款中，逾期，已还款 => 当期结清状态 0：未结清,1：已结清
				// 如果 current_status为 还款中，逾期则继续判断repay_status==> 2:线上已还款,3:全部已还款 则为结清
				Integer projPlanListStatus = 0;
				switch (projPlanList.getCurrentStatus()) {
				case "还款中":
				case "逾期":
					if (repayStatus == 2 || repayStatus == 3) {
						projPlanListStatus = 1;
					} else {
						projPlanListStatus = 0;
					}
					break;
				case "已还款":
					projPlanListStatus = 1;
					break;
				default:
					LOGGER.info("@对接合规还款接口@  标的计划本期状态值current_status:[{}]错误 输入参数 projPlanListId:[{}] ",
							projPlanList.getCurrentStatus(), projPlanListId);
					return Result.error("500", "标的计划本期状态值错误");
				}
				vo.setIsComplete(projPlanListStatus);
				vo.setProjPlanListId(projPlanList.getProjPlanListId());
				if (StringUtils.isBlank(tuandaiProjectInfo.getTdUserId())) {
					LOGGER.info("@对接合规还款接口@ 团贷用户ID(资金端用户ID)为空 projPlanListId:[{}]", projPlanListId);
					return Result.error("团贷用户ID(资金端用户ID)为空");
				} else {
					vo.setTdUserId(tuandaiProjectInfo.getTdUserId());
				}
				vo.setConfirmLogId(projPlanList.getProjPlanListId());

				Result result = tdrepayRechargeController.accessTdrepayReCharge(vo);
				if (!"1".equals(result.getCode())) {
					LOGGER.info("@对接合规还款接口@ 调用代充值资金分发参数接入接口失败 vo:[{}]", vo);
					return Result.error("合规还款失败:" + result.getMsg());
				}
			} else {
				return Result.error("查询平台标的还款计划失败");
			}
			LOGGER.info("@对接合规还款接口@ 调用代充值资金分发成功 vo:[{}]", vo);
			return Result.success(JSON.toJSONString(vo));
		} catch (Exception e) {
			LOGGER.error("通过合化还款接口还款失败.", e);
			return Result.error("500", "通过合化还款接口还款失败！" + e.getMessage());
		}
	}

	/**
	 * 获取不需要分润的数据
	 */
	private Map<String, Integer> getNotShareProfitFeeIds(String projPlanListId) {

		Map<String, Integer> feeIdMap = new HashMap<>();

		List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailService
				.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id", projPlanListId));
		if (CollectionUtils.isEmpty(repaymentProjPlanListDetails)) {
			return feeIdMap;
		}

		for (RepaymentProjPlanListDetail detail : repaymentProjPlanListDetails) {
			if (StringUtil.isEmpty(detail.getFeeId()) || detail.getShareProfitIndex() == null) {
				continue;
			}
			if (detail.getShareProfitIndex().intValue() >= 1200) {
				feeIdMap.put(detail.getFeeId(), detail.getShareProfitIndex());
			}
		}

		return feeIdMap;
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
