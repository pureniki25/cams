package com.hongte.alms.base.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.UserPermissionBusinessDto;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.Doc;
import com.hongte.alms.base.entity.DocType;
import com.hongte.alms.base.entity.RenewalBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.enums.RepayTypeEnum;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.mapper.BizOutputRecordMapper;
import com.hongte.alms.base.mapper.RenewalBusinessMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanMapper;
import com.hongte.alms.base.mapper.TransferOfLitigationMapper;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.ExpenseSettleService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.vo.billing.CarLoanBilVO;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.base.vo.module.ExpenseSettleRepaymentPlanVO;
import com.hongte.alms.base.vo.module.ExpenseSettleVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;

/**
 * <p>
 * 基础业务信息表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-02
 */
@Service("BasicBusinessService")
public class BasicBusinessServiceImpl extends BaseServiceImpl<BasicBusinessMapper, BasicBusiness>
		implements BasicBusinessService {
	private Logger logger = LoggerFactory.getLogger(BasicBusinessServiceImpl.class);
	

	@Autowired
	private TransferOfLitigationMapper transferOfLitigationMapper;

	@Autowired
	BasicBusinessMapper basicBusinessMapper;

	@Autowired
	@Qualifier("SysParameterService")
	SysParameterService sysParameterService;

	@Autowired
	@Qualifier("BizOutputRecordService")
	BizOutputRecordService bizOutputRecordService;

	@Autowired
	@Qualifier("ExpenseSettleService")
	ExpenseSettleService expenseSettleService;
	@Autowired
	RenewalBusinessMapper renewalBusinessMapper;

	@Autowired
	BizOutputRecordMapper bizOutputRecordMapper;

	@Autowired
	RepaymentBizPlanMapper repaymentBizPlanMapper;
	@Autowired
	RepaymentBizPlanListMapper repaymentBizPlanListMapper;
	@Autowired
	RepaymentBizPlanListDetailMapper repaymentBizPlanListDetailMapper;
	

	public List<BusinessInfoForApplyDerateVo> selectBusinessInfoForApplyDerateVo(String crpId, Integer isDefer,
			String originalBusinessId) {
		List<BusinessInfoForApplyDerateVo> List = basicBusinessMapper.selectBusinessInfoForApplyDerateVo(crpId, isDefer,
				originalBusinessId);

		// 利息类型列表
		List<SysParameter> borrowRateUnitlist = sysParameterService.selectList(new EntityWrapper<SysParameter>()
				.eq("param_type", SysParameterTypeEnums.BORROW_RATE_UNIT.getKey()).orderBy("row_Index"));

		Map<String, SysParameter> sysParameterMap = sysParameterService
				.selectParameterMap(SysParameterTypeEnums.BORROW_RATE_UNIT);
		for (BusinessInfoForApplyDerateVo vo : List) {
			Double principal = basicBusinessMapper.queryPayedPrincipal(vo.getBusinessId());
			vo.setPayedPrincipal(BigDecimal.valueOf(principal == null ? 0 : principal));

			SysParameter parameter = sysParameterMap.get(vo.getRepaymentTypeId());
			if (parameter != null) {
				vo.setRepaymentTypeName(parameter.getParamValue());
			} else {
				vo.setRepaymentTypeName(vo.getRepaymentTypeId().toString());
			}

			if (vo.getBorrowRate() != null) {
				vo.setBorrowRateStr(String.format("%.2f", vo.getBorrowRate()) + "%");
			}
			for (SysParameter rateUnit : borrowRateUnitlist) {
				if (vo.getBorrowRateUnit().toString().equals(rateUnit.getParamValue())) {
					vo.setBorrowRateName(rateUnit.getParamName());
				}
			}
			// 剩余本金
			if (vo.getPayedPrincipal() == null) {
				if (vo.getGetMoney() != null) {
					vo.setRemianderPrincipal(vo.getGetMoney());
				} else {
					vo.setRemianderPrincipal(new BigDecimal(0.00));
				}
			} else {
				if (vo.getGetMoney() != null) {
					vo.setRemianderPrincipal(vo.getGetMoney().subtract(vo.getPayedPrincipal()));
				} else {
					vo.setRemianderPrincipal(new BigDecimal(0.00));
				}
			}

		}

		return List;

	}

	public BusinessInfoForApplyDerateVo selectBusinessInfoForApplyDerateVoOne(String crpId, Integer isDefer,
			String originalBusinessId) {
		List<BusinessInfoForApplyDerateVo> List = selectBusinessInfoForApplyDerateVo(crpId, isDefer,
				originalBusinessId);

		if (List.size() > 0) {
			return List.get(0);
		}
		return new BusinessInfoForApplyDerateVo();
	}

	@Override
	public List<UserPermissionBusinessDto> selectUserPermissionBusinessDtos(List<String> companyIds) {
		return basicBusinessMapper.selectUserPermissionBusinessDtos(companyIds);
	}

	@Override
	public List<String> selectCompanysBusinessIds(List<String> companyIds) {
		List<UserPermissionBusinessDto> bList = new LinkedList<>();
		if (companyIds.size() > 0) {
			bList = selectUserPermissionBusinessDtos(companyIds);
		}
		List<String> sList = new LinkedList<String>();

		for (UserPermissionBusinessDto dto : bList) {
			sList.add(dto.getBusinessId());
		}
		return sList;
	}

	/**
	 * 结清最终缴纳的金额
	 * 
	 * @param original_business_id
	 * @return
	 */
	@Override
	public Double getSettleTotalFactSum(@Param("original_business_id") String original_business_id) {

		return basicBusinessMapper.getSettleTotalFactSum(original_business_id);
	}

	/**
	 * 一次性收取的分公司费用+期初收取的月收分公司服务费+平台费+担保费
	 * 
	 * @param original_business_id
	 * @return
	 */
	@Override
	public BigDecimal getPreChargeAndPreFees(String original_business_id) {
		BigDecimal preCharge = BigDecimal.valueOf(basicBusinessMapper.getPreCharge(original_business_id) == null ? 0
				: basicBusinessMapper.getPreCharge(original_business_id));
		// BigDecimal
		// preFees=BigDecimal.valueOf(basicBusinessMapper.getPreFees(original_business_id)==null?0:basicBusinessMapper.getPreFees(original_business_id));
		BigDecimal sum = preCharge;
		return sum;
	}
	//
	// Public BusinessInfoForApplyDerateVo selectBusinessInfoForApplyDerateVo(String
	// crpId){
	//
	// return null;
	// }

	@Override
	public Double getMonthSumFactAmount(String original_business_id) {
		return basicBusinessMapper.getMonthSumFactAmount(original_business_id);
	}

	@Override
	public Map<String, Object> getNeedPay(String original_business_id) {
		return basicBusinessMapper.getNeedPay(original_business_id);
	}

	/**
	 * 月收平台服务费的业务
	 * 
	 * @param original_business_id
	 * @return
	 */
	@Override
	public Double getMonthPlatformAmount(@Param("crpId") String crpId) {
		return basicBusinessMapper.getMonthPlatformAmount(crpId);
	}

	/**
	 * 月收公司服务费的业务
	 * 
	 * @param original_business_id
	 * @returnfmn
	 */
	@Override
	public Double getMonthCompanyAmount(@Param("crpId") String crpId) {
		return basicBusinessMapper.getMonthCompanyAmount(crpId);
	}

	@Override
	public ExpenseSettleVO getPreLateFees(String crpId, String original_business_id, String repayType, String businessTypeId,
			Date preSettleDate, Date ContractDate, Date firstRepayDate,String restPeriods) throws Exception {
		Integer settleMonth = getSettleMonths(preSettleDate, firstRepayDate);// 结清阶段
		Double preLateFees = Double.valueOf(0);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Integer platformCount=getMonthPlatformAmountCount(crpId);//有无月收平台费
		Double monthPlatformAmount = getMonthPlatformAmount(crpId);// 月收平台费
		Double monthCompanyAmount = getMonthCompanyAmount(crpId);// 月收公司服务费
		BizOutputRecord bizOutputRecord = bizOutputRecordService
				.selectOne(new EntityWrapper<BizOutputRecord>().eq("business_id", original_business_id));
		Date outputDate = null;// 出款日期
		ExpenseSettleVO vo = cal(original_business_id, preSettleDate);
		if (bizOutputRecord != null) {
			outputDate = bizOutputRecord.getFactOutputDate();
		}else {
			throw new ServiceRuntimeException("找不到出款记录");
		}
		if(ContractDate==null) {
			ContractDate=outputDate;
		}
		try {

			if (businessTypeId.equals(BusinessTypeEnum.FSD_TYPE.getValue().toString())) {// 房贷
				// 无月收平台服务费
				if (platformCount ==0) {
					Date date1 = dateFormat.parse("2017-03-01");
					Date date2 = dateFormat.parse("2017-12-04");
					Date date3 = dateFormat.parse("2017-12-05");
					Date date4 = dateFormat.parse("2017-06-05");
					if (repayType.equals(RepayTypeEnum.EQUAL_AMOUNT_INTEREST.getValue().toString())) {
						// 出款日期在2017年3月之前并且分公司服务费是一次性收取的，提前还款违约金为0
						if (outputDate.compareTo(date1) < 0 && monthCompanyAmount == 0) {
							preLateFees=Double.valueOf(0);
						}
						// 出款日期在2017年3月和2017年12月4日之间并且分公司服务费是按月收取的，提前还款违约金为:分公司服务费剩余的还款期数综合，但是不超过剩余本金的6%；超过的按6%收取
						if (outputDate.compareTo(date1) > 0 && outputDate.compareTo(date2) <= 0
								&& monthCompanyAmount > 0) {
							preLateFees = vo.getPenalty().doubleValue();
						}
						// 出款日期在2017年12月5日至今
						// ,并且分公司服务费是按月收取的，提前还款违约金为:借款日起半年内结清，收取剩余本金的4%，超过半年不足1年的收取2%，超过一年结清的不收取
						if (outputDate.compareTo(date3) > 0 && monthCompanyAmount > 0) {
							Calendar c = Calendar.getInstance();
							c.setTime(outputDate);
							c.add(Calendar.MONTH, 6);
							Date halfYeahDate = c.getTime();
							c.add(Calendar.MONTH, 6);
							Date oneYeahDate = c.getTime();
							if (preSettleDate.before(halfYeahDate)) {// 半年内
								preLateFees = vo.getPrincipal().doubleValue() * 0.04;
							} else if (preSettleDate.after(halfYeahDate) && preSettleDate.before(oneYeahDate)) {// 超过半年不足1年
								preLateFees = vo.getPrincipal().doubleValue() * 0.04;
							} else if (preSettleDate.after(preSettleDate)) {// 超过一年结清的不收取
								preLateFees = Double.valueOf(0);
							}

						}

					} else if (repayType.equals(String.valueOf(RepayTypeEnum.FIRST_INTEREST.getValue().toString()))) {// 先息后本
						// 出款日期在2017年6月5日之前 ,并且分公司服务费是按月收取的，提前还款违约金为:0
						if (outputDate.compareTo(date4) < 0 && monthCompanyAmount > 0) {
							preLateFees = Double.valueOf(0);
						}
						// 出款日期在2017年6月5日至今 ,并且分公司服务费是按月收取的，提前还款违约金为:剩余本金*0.5%*剩余还款期数
						if (outputDate.compareTo(date4) > 0 && monthCompanyAmount > 0) {
							preLateFees = vo.getPrincipal().doubleValue() * 0.005 * Integer.valueOf(restPeriods);
						}
					} else {
						preLateFees = Double.valueOf(0);
					}

				} else {// 有月收平台服务费

					// 2018年4月2日以后的数据

					if (ContractDate.after(DateUtil.getDate("2018-04-02", "yyyy-MM-dd"))) {
						// 等额本息,还本付息5年
						if (repayType.equals(RepayTypeEnum.EQUAL_AMOUNT_INTEREST.getValue().toString())
								|| repayType.equals(RepayTypeEnum.DIVIDE_INTEREST_FIVE.getValue().toString())) {
							if (settleMonth <= 6) {
								// 本金违约金
								double bjwyj = vo.getPrincipal().doubleValue() * 0.04 - monthPlatformAmount * 2
										- monthCompanyAmount * 2;
								preLateFees = bjwyj + monthPlatformAmount * 2 + monthCompanyAmount * 2;
							} else if (settleMonth >= 7 && settleMonth <= 12) {
								// 本金违约金
								double bjwyj = vo.getPrincipal().doubleValue() * 0.02 - monthPlatformAmount * 2
										- monthCompanyAmount * 2;
								if (bjwyj < 0) {// 如果是负数，只收取服务费违约金
									bjwyj = 0;
								} 
								preLateFees = bjwyj + monthPlatformAmount * 2 + monthCompanyAmount * 2;

							} else if (settleMonth >= 13 && settleMonth <= 48) {
								preLateFees = monthPlatformAmount * 2 + monthCompanyAmount * 2;
							}

							// 先息后本
						} else if (repayType.equals(RepayTypeEnum.FIRST_INTEREST.getValue().toString())) {
							if (settleMonth <= 12) {
								preLateFees = vo.getPrincipal().doubleValue() * 0.005 * Integer.valueOf(restPeriods);
								if (preLateFees > vo.getPrincipal().doubleValue() * 0.06) {
									preLateFees = vo.getPrincipal().doubleValue() * 0.06;
								}
								preLateFees = preLateFees + monthPlatformAmount*2  + monthCompanyAmount*2 ;
							}

							// 还本付息10年
						} else if (repayType.equals(RepayTypeEnum.DIVIDE_INTEREST_TEN.getValue().toString())) {
							if (settleMonth <= 6) {
								preLateFees = monthPlatformAmount * 2 + monthCompanyAmount * 2;
							} else if (settleMonth <= 12 && settleMonth >= 7) {
								preLateFees = monthPlatformAmount * 2 + monthCompanyAmount * 2;
							} else if (settleMonth >= 13 && settleMonth <= 36) {
								preLateFees = monthPlatformAmount * 2 + monthCompanyAmount * 2;
							}
						}

						// 2017年12月30日和2018年4月2日之间的数据
					} else if (ContractDate.before(DateUtil.getDate("2018-04-02", "yyyy-MM-dd"))
							&& ContractDate.after(DateUtil.getDate("2017-12-30", "yyyy-MM-dd"))) {
						// 等额本息,还本付息5年
						if (repayType.equals(RepayTypeEnum.EQUAL_AMOUNT_INTEREST.getValue().toString())
								|| repayType.equals(RepayTypeEnum.DIVIDE_INTEREST_FIVE.getValue().toString())) {
							if (settleMonth <= 6) {
								// 本金违约金
								double bjwyj = vo.getPrincipal().doubleValue() * 0.04 - monthPlatformAmount * 2
										- monthCompanyAmount * 2;
								if (bjwyj < 0) {// 如果是负数，只收取服务费违约金
									bjwyj = 0;
								}
								preLateFees = bjwyj + monthPlatformAmount * 2 + monthCompanyAmount * 2;
							} else if (settleMonth >= 7 && settleMonth <= 12) {
								// 本金违约金
								double bjwyj = vo.getPrincipal().doubleValue() * 0.02 - monthPlatformAmount
										- monthCompanyAmount;
								if (bjwyj < 0) {// 如果是负数，只收取服务费违约金
									bjwyj = 0;
								}
								preLateFees = bjwyj + monthPlatformAmount + monthCompanyAmount;

							} else if (repayType.equals(RepayTypeEnum.EQUAL_AMOUNT_INTEREST.getValue().toString())
									&& settleMonth >= 13 && settleMonth <= 48) {
								preLateFees = Double.valueOf(0);
							}

							// 先息后本
						} else if (repayType.equals(RepayTypeEnum.FIRST_INTEREST.getValue().toString())) {
							if (settleMonth <= 12) {
								preLateFees = vo.getPrincipal().doubleValue() * 0.005 * Integer.valueOf(restPeriods);
								if (preLateFees > vo.getPrincipal().doubleValue() * 0.06) {
									preLateFees = vo.getPrincipal().doubleValue() * 0.06;
								}
								preLateFees = preLateFees + monthPlatformAmount + monthCompanyAmount;
							} else {
								preLateFees = Double.valueOf(0);
							}

							// 还本付息10年
						} else if (repayType.equals(RepayTypeEnum.DIVIDE_INTEREST_TEN.getValue().toString())) {
							if (settleMonth <= 6) {
								// 本金违约金
								double bjwyj = vo.getPrincipal().doubleValue() * 0.06 - monthPlatformAmount * 2
										- monthCompanyAmount * 2;
								if (bjwyj < 0) {// 如果是负数，只收取服务费违约金
									bjwyj = 0;
								}
								preLateFees = bjwyj+monthPlatformAmount * 2 + monthCompanyAmount * 2;
							} else if (settleMonth <= 12 && settleMonth >= 7) {
								// 本金违约金
								double bjwyj = vo.getPrincipal().doubleValue() * 0.02 - monthPlatformAmount
										- monthCompanyAmount;
								if (bjwyj < 0) {// 如果是负数，只收取服务费违约金
									bjwyj = 0;
								}
								preLateFees =bjwyj+monthPlatformAmount + monthCompanyAmount;
							} else if (settleMonth >= 13 && settleMonth <= 120) {
								preLateFees = Double.valueOf(0);
							}
						}
					}

				}
			} else {
                       
				preLateFees = Double.valueOf(0);

			}
		} catch (

		ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vo.setPenalty(BigDecimal.valueOf(preLateFees));
		return vo;
	}

	private ExpenseSettleVO cal(String businessId, Date settleDate) {
		final BasicBusiness basicBusiness = basicBusinessMapper.selectById(businessId);
		RepaymentBizPlan repaymentBizPlan = new RepaymentBizPlan();
		repaymentBizPlan.setBusinessId(businessId);
		repaymentBizPlan = repaymentBizPlanMapper.selectOne(repaymentBizPlan);
		List<Object> businessIds = renewalBusinessMapper.selectObjs(new EntityWrapper<RenewalBusiness>()
				.eq("original_business_id", businessId).setSqlSelect("renewal_business_id"));
		if (businessIds == null) {
			businessIds = new ArrayList<>();
		}
		businessIds.add(businessId);
		List<RepaymentBizPlanList> planLists=null;
		if(businessId.contains("ZQ")) {
			 planLists = repaymentBizPlanListMapper.selectList(
					new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).orderBy("due_date"));
		}else {
		planLists = repaymentBizPlanListMapper.selectList(
				new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", businessId).orderBy("due_date"));
		}
		final List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailMapper.selectList(
				new EntityWrapper<RepaymentBizPlanListDetail>().in("business_id", businessIds).orderBy("period"));
		final ExpenseSettleRepaymentPlanVO plan = new ExpenseSettleRepaymentPlanVO(repaymentBizPlan, planLists,
				details);
//		plan.findCurrentPeriods(settleDate);
		final List<BizOutputRecord> bizOutputRecord = bizOutputRecordMapper.selectList(
				new EntityWrapper<BizOutputRecord>().eq("business_id", businessId).orderBy("fact_output_date", true));
		ExpenseSettleVO expenseSettleVO = new ExpenseSettleVO();
//		expenseSettleVO.setRestPeriod(plan.getFinalPeriod().getRepaymentBizPlanList().getPeriod()
//				- plan.getCurrentPeriod().getRepaymentBizPlanList().getPeriod());
		if (!basicBusiness.getRepaymentTypeId().equals(RepayTypeEnum.EQUAL_AMOUNT_INTEREST.getValue())
				&& !basicBusiness.getRepaymentTypeId().equals(RepayTypeEnum.FIRST_INTEREST.getValue())
				&& !basicBusiness.getRepaymentTypeId().equals(RepayTypeEnum.DIVIDE_INTEREST_TEN.getValue())
				&& !basicBusiness.getRepaymentTypeId()
						.equals(RepayTypeEnum.DIVIDE_INTEREST_FIVE.getValue())) {
			throw new ServiceRuntimeException("暂时不支持这种还款方式的减免申请");
		}
		calPrincipal(settleDate, expenseSettleVO, basicBusiness, plan, bizOutputRecord);
		calPenalty(settleDate, expenseSettleVO, basicBusiness, plan);
		expenseSettleService.calLackFee(settleDate, expenseSettleVO, basicBusiness, plan);
		expenseSettleService.calDemurrage(settleDate, expenseSettleVO, basicBusiness, plan);
		return expenseSettleVO;

	}

	/**
	 * 计算剩余未还本金
	 * 
	 * @author 2018年3月30日 下午2:35:29
	 * @param expenseSettleVO
	 */
	private void calPrincipal(Date settleDate, ExpenseSettleVO expenseSettleVO, BasicBusiness basicBusiness,
			ExpenseSettleRepaymentPlanVO plan, List<BizOutputRecord> bizOutputRecords) {
		BigDecimal outPutMoney = new BigDecimal(0);
		for (BizOutputRecord bizOutputRecord : bizOutputRecords) {
			outPutMoney = outPutMoney.add(bizOutputRecord.getFactOutputMoney());
		}
		switch (basicBusiness.getRepaymentTypeId()) {
		case 2:
			expenseSettleVO.setPrincipal(outPutMoney);
			break;
		case 5:
			BigDecimal paid = new BigDecimal(0);
			for (RepaymentBizPlanListDetail detail : plan.allDetails()) {
				if (detail.getPlanItemType().equals(new Integer(10))) {
					paid = paid.add(detail.getFactAmount() == null ? new BigDecimal(0) : detail.getFactAmount());
				}
			}
			expenseSettleVO.setPrincipal(outPutMoney.subtract(paid));
			break;
		case 500:
			BigDecimal paid1 = new BigDecimal(0);
			for (RepaymentBizPlanListDetail detail : plan.allDetails()) {
				if (detail.getPlanItemType().equals(new Integer(10))) {
					paid1 = paid1.add(detail.getFactAmount() == null ? new BigDecimal(0) : detail.getFactAmount());
				}
			}
			expenseSettleVO.setPrincipal(outPutMoney.subtract(paid1));
			break;
		case 1000:
			BigDecimal paid2 = new BigDecimal(0);
			for (RepaymentBizPlanListDetail detail : plan.allDetails()) {
				if (detail.getPlanItemType().equals(new Integer(10))) {
					paid2 = paid2.add(detail.getFactAmount() == null ? new BigDecimal(0) : detail.getFactAmount());
				}
			}
			expenseSettleVO.setPrincipal(outPutMoney.subtract(paid2));
		}
	}

	/**
	 * 计算提前还款违约金 2018年5月8日 下午5:58:40
	 * 
	 * @param settleDate
	 * @param expenseSettleVO
	 * @param basicBusiness
	 * @param plan
	 */
	private void calPenalty(Date settleDate, ExpenseSettleVO expenseSettleVO, BasicBusiness basicBusiness,
			ExpenseSettleRepaymentPlanVO plan) {
		BigDecimal penalty = new BigDecimal(0);
		RepaymentBizPlanList lastCurrentPeriod = plan.findCurrentPeriods(settleDate)
				.get(plan.findCurrentPeriods(settleDate).size() - 1).getRepaymentBizPlanList();
		for (RepaymentBizPlanListDetail detail : plan.allDetails()) {
			if (detail.getPeriod() > lastCurrentPeriod.getPeriod() && detail.getPlanItemType() == 30) {
				penalty = penalty.add(detail.getPlanAmount());
			}

		}

		switch (basicBusiness.getRepaymentTypeId()) {
		case 2:
			// 先息后本

			break;
		case 5:
			// 等额本息

			BigDecimal p6 = expenseSettleVO.getPrincipal().multiply(new BigDecimal(0.06));
			if (penalty.compareTo(p6) >= 0) {
				penalty = p6;
			}
			break;
		default:
			/* 找不到还款方式233333333333 */
			break;
		}

		expenseSettleVO.setPenalty(penalty);

	}

	// 获取结清阶段
	private Integer getSettleMonths(Date settleDate, Date firstRepayDate) {
		Integer days = DateUtil.getDiffDays(firstRepayDate, settleDate);
		Integer months=0;
		if(days<0) {
			months=0;
		}else {
			months=days/30;
		}
		return months;
	}
	
	 /**
		 * 	 获取展期的借款期数
		 * @param crpId
		 * @return
		 */
	@Override
	 public Integer   getBorrowLlimitZQ(@Param("crpId") String crpId) {
		return basicBusinessMapper.getBorrowLlimitZQ(crpId);
		 
	 }

	@Override
	public Integer getMonthPlatformAmountCount(String crpId) {
		return basicBusinessMapper.getMonthPlatformAmountCount(crpId);
	}
	

		@Override
		public Map<String, Object> carLoanBilling(CarLoanBilVO carLoanBilVO,Integer overdueDays) {

			if (carLoanBilVO == null || StringUtil.isEmpty(carLoanBilVO.getBusinessId())
					|| carLoanBilVO.getBillDate() == null) {
				return null;
			}
			double outsideInterest = 0; // 期外逾期利息
			double outside = carLoanBilVO.getOutsideInterest(); // 期外逾期利息计算费率
			// 获取车贷基础信息
			String businessId = carLoanBilVO.getBusinessId();
			
			Date billDate = carLoanBilVO.getBillDate(); // 预计结清日期
			Map<String, Object> resultMap = queryCarLoanData(businessId);
			
			Map<String, Object> maxPeriodMap = transferOfLitigationMapper.queryMaxDueDateByBusinessId(businessId); // 合同到期日
			Date maxDueDate = (Date) maxPeriodMap.get("maxDueDate");
			double surplusPrincipal = ((BigDecimal) resultMap.get("surplusPrincipal")).doubleValue(); // 剩余本金
			String repaymentTypeId = (String) resultMap.get("repaymentTypeId"); // 还款类型
			
			int outputPlatformId = (int) resultMap.get("outputPlatformId"); // 出款平台
			
			
			double borrowMoney = ((BigDecimal) resultMap.get("borrowMoney")).doubleValue(); // 借款金额
			
			// 判断预计结算日期是否超过合同日期
			if (billDate.after(maxDueDate)) {


				// 判断是否上标业务： outputPlatformId == 1 是， outputPlatformId == 0 否
				if ("到期还本息".equals(repaymentTypeId) || "每月付息到期还本".equals(repaymentTypeId)) {
					//非上标
					if (outputPlatformId == 0) {
						if (overdueDays < 15) {
							outsideInterest = surplusPrincipal * 0.035 / 30 * overdueDays;
						} else if (15 <= overdueDays && overdueDays < 30) {
							outsideInterest = surplusPrincipal * 0.035;
						} else {
							int i = (overdueDays / 30) <= 1 ? 1 : (overdueDays / 30);
							int j = overdueDays % 30;
							if (i >= 1) {
							
								if(j<15) {
									outsideInterest = surplusPrincipal * 0.035 * i;
									outsideInterest += surplusPrincipal * 0.035 / 30 * j;
								}else {
									outsideInterest = surplusPrincipal * 0.035 * (i+1);
									
								}
							}
						}
						
					 //上标
					}else {
						if (overdueDays < 15) {
							outsideInterest = surplusPrincipal * outside / 30 * overdueDays;
						} else if (15 <= overdueDays && overdueDays < 30) {
							outsideInterest = surplusPrincipal * outside;
						} else {
							int i = (overdueDays / 30) <= 1 ? 1 : (overdueDays / 30);
							int j = overdueDays % 30;
							if (i >=1) { 
								if(j<15) {
									outsideInterest = surplusPrincipal * outside * i;
									outsideInterest += surplusPrincipal * outside / 30 * j;
								}else {
									outsideInterest = surplusPrincipal * outside * (i+1);
									
								}
							}
						}
						
						
					}
				} else if ("等额本息".equals(repaymentTypeId)) {
					//非上标
					if (outputPlatformId == 0) {
						if (overdueDays < 15) {
							outsideInterest = borrowMoney * 0.02 / 30 * overdueDays;
						} else if (15 <= overdueDays && overdueDays < 30) {
							outsideInterest = borrowMoney * 0.02;
						} else {
							int i = (overdueDays / 30) <= 1 ? 1 : (overdueDays / 30);
							int j = overdueDays % 30;
							if (i >= 1) {
								if(j<15) {
									outsideInterest = borrowMoney * 0.02  * i;
									outsideInterest += borrowMoney * 0.02  / 30 * j;
								}else {
									outsideInterest = borrowMoney * 0.02  * (i+1);
									
								}
							}
						}
                     
					//上标
					}else {
						if (overdueDays < 15) {
							outsideInterest = surplusPrincipal * outside / 30 * overdueDays;
						} else if (15 <= overdueDays && overdueDays < 30) {
							outsideInterest = surplusPrincipal * outside;
						} else {
							int i = (overdueDays / 30) <= 1 ? 1 : (overdueDays / 30);
							int j = overdueDays % 30;
							if (i >= 1) { 
								if(j<15) {
									outsideInterest = surplusPrincipal * outside * i;
									outsideInterest += surplusPrincipal * outside / 30 * j;
								}else {
									outsideInterest = surplusPrincipal * outside * (i+1);
									
								}
							
							}
						}
						
						
					}
				} else {
					return null;
				}

			}
			resultMap.put("outsideInterest", BigDecimal.valueOf(outsideInterest).setScale(2, RoundingMode.HALF_UP).doubleValue());
			return resultMap;
		}
		
		
		public Map<String, Object> queryCarLoanData(String businessId) {
			if (StringUtil.isEmpty(businessId)) {
				return null;
			}


			Map<String, Object> resultMap = transferOfLitigationMapper.queryCarLoanData(businessId);
			if (resultMap == null) {
				return resultMap;
			}

			Date factRepayDate = (Date) resultMap.get("factRepayDate");
			Date dueDate = (Date) resultMap.get("_dueDate"); // 第一期应还日期
			int overdueDays = DateUtil.getDiffDays(factRepayDate == null ? dueDate : factRepayDate, new Date());
			resultMap.put("overdueDays", overdueDays < 0 ? 0 : overdueDays);

			Object repaymentTypeId = resultMap.get("repaymentTypeId");
			String repaymentType = "";
			if (repaymentTypeId != null) {
				switch ((int) repaymentTypeId) {
				case 1:
					repaymentType = "到期还本息";
					break;
				case 2:
					repaymentType = "每月付息到期还本";
					break;
				case 4:
					repaymentType = "等本等息";
					break;
				case 5:
					repaymentType = "等额本息";
					break;
				default:
					repaymentType = "分期还本付息";
					break;
				}
				resultMap.put("repaymentTypeId", repaymentType);
			}


			return resultMap;
		}
		public static void main(String[] args) {
			int i=16%15;
			System.out.println(i);
		}
}
