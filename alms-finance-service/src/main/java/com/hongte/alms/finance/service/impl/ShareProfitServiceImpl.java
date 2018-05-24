package com.hongte.alms.finance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hongte.alms.base.RepayPlan.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.entity.AccountantOverRepayLog;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.entity.RepaymentResource;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.AccountantOverRepayLogMapper;
import com.hongte.alms.base.mapper.ApplyDerateProcessMapper;
import com.hongte.alms.base.mapper.ApplyDerateTypeMapper;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.mapper.MoneyPoolMapper;
import com.hongte.alms.base.mapper.MoneyPoolRepaymentMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanMapper;
import com.hongte.alms.base.mapper.RepaymentProjFactRepayMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanMapper;
import com.hongte.alms.base.mapper.RepaymentResourceMapper;
import com.hongte.alms.base.mapper.TuandaiProjectInfoMapper;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.service.AccountantOverRepayLogService;
import com.hongte.alms.base.service.RepaymentProjFactRepayService;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;

import com.hongte.alms.finance.service.ShareProfitService;
import com.ht.ussp.bean.LoginUserInfoHelper;

/**
 * @author 王继光 2018年5月24日 下午2:46:52
 */
@Service("ShareProfitService")
public class ShareProfitServiceImpl implements ShareProfitService {
	private static Logger logger = LoggerFactory.getLogger(ShareProfitServiceImpl.class);
	@Autowired
	BasicBusinessMapper basicBusinessMapper;
	@Autowired
	RepaymentBizPlanListMapper repaymentBizPlanListMapper;
	@Autowired
	RepaymentBizPlanMapper repaymentBizPlanMapper;
	@Autowired
	RepaymentResourceMapper repaymentResourceMapper;
	@Autowired
	RepaymentProjFactRepayMapper repaymentProjFactRepayMapper;
	@Autowired
	RepaymentBizPlanListDetailMapper repaymentBizPlanListDetailMapper;
	@Autowired
	RepaymentProjPlanListDetailMapper repaymentProjPlanListDetailMapper;
	@Autowired
	RepaymentProjPlanListMapper repaymentProjPlanListMapper;
	@Autowired
	TuandaiProjectInfoMapper tuandaiProjectInfoMapper;
	@Autowired
	RepaymentProjPlanMapper repaymentProjPlanMapper;
	@Autowired
	MoneyPoolMapper moneyPoolMapper;
	@Autowired
	ApplyDerateProcessMapper applyDerateProcessMapper;
	@Autowired
	ApplyDerateTypeMapper applyDerateTypeMapper;
	@Autowired
	ProcessMapper processMapper;
	@Autowired
	AccountantOverRepayLogMapper accountantOverRepayLogMapper;
	@Autowired
	MoneyPoolRepaymentMapper moneyPoolRepaymentMapper;
	@Autowired
	LoginUserInfoHelper loginUserInfoHelper;

	@Autowired
	@Qualifier("AccountantOverRepayLogService")
	AccountantOverRepayLogService accountantOverRepayLogService;
	@Autowired
	@Qualifier("RepaymentProjFactRepayService")
	RepaymentProjFactRepayService repaymentProjFactRepayService;

	private List<CurrPeriodProjDetailVO> projListDetails = new ArrayList<>();
	private List<RepaymentResource> repaymentResources = new ArrayList<>();
	// private RepaymentBizPlanDto repaymentBizPlanDto ;
	private String businessId;
	private String afterId;

	private RepaymentBizPlanDto planDto;
	/**
	 * 总应还金额
	 */
	private BigDecimal repayPlanAmount = new BigDecimal(0);
	/**
	 * 总实还金额
	 */
	private BigDecimal repayFactAmount = new BigDecimal(0);
	/**
	 * 总银行流水金额
	 */
	private BigDecimal moneyPoolAmount = new BigDecimal(0);
	/**
	 * 本次还款后,结余金额
	 */
	private BigDecimal surplusAmount;
	/**
	 * 本次还款后,缺多少金额
	 */
	private BigDecimal lackAmount;
	private Boolean save;

	@Override
	@Transactional(rollbackFor = ServiceRuntimeException.class)
	public void execute(ConfirmRepaymentReq req, boolean save) {
		if (req == null) {
			throw new ServiceRuntimeException("ConfirmRepaymentReq 不能为空");
		}
		if (req.getBusinessId() == null) {
			throw new ServiceRuntimeException("ConfirmRepaymentReq.businessId 不能为空");
		}
		if (req.getAfterId() == null) {
			throw new ServiceRuntimeException("ConfirmRepaymentReq.afterId 不能为空");
		}
		if (req.getMprIds() == null && req.getSurplusFund() == null) {
			throw new ServiceRuntimeException("ConfirmRepaymentReq至少要有一个还款来源");
		}
		businessId = req.getBusinessId();
		afterId = req.getAfterId();
		repayPlanAmount = repaymentProjFactRepayService.caluUnpaid(businessId, afterId);
		planDto = initRepaymentBizPlanDto(req);
		sortRepaymentResource(req);
		if (repayFactAmount.compareTo(repayPlanAmount) >= 0) {
			surplusAmount = repayFactAmount.subtract(repayPlanAmount);
		} else {
			lackAmount = repayPlanAmount.subtract(repayFactAmount);
		}
		caluProportion(planDto);
		divideMoney(repayFactAmount, planDto);
		divideOveryDueMoney(req.getOfflineOverDue(), planDto, false);
		divideOveryDueMoney(req.getOnlineOverDue(), planDto, true);
		fill();
		System.out.println(JSON.toJSONString(projListDetails));
	}

	/**
	 * 根据参数创建还款来源并排序,银行流水,银行代扣,线下代扣3个优先级最高,结余优先级最低
	 * 
	 * @author 王继光 2018年5月24日 下午3:45:07
	 * @param req
	 * @return
	 */
	private void sortRepaymentResource(ConfirmRepaymentReq req) {
		List<String> mprids = req.getMprIds();
		BigDecimal surplus = req.getSurplusFund();

		if (mprids != null && mprids.size() > 0) {
			List<MoneyPoolRepayment> moneyPoolRepayments = moneyPoolRepaymentMapper.selectBatchIds(mprids);
			for (MoneyPoolRepayment moneyPoolRepayment : moneyPoolRepayments) {
				repayFactAmount = repayFactAmount.add(moneyPoolRepayment.getAccountMoney());
				moneyPoolAmount = moneyPoolAmount.add(moneyPoolRepayment.getAccountMoney());
				RepaymentResource repaymentResource = new RepaymentResource();
				repaymentResource.setAfterId(moneyPoolRepayment.getAfterId());
				repaymentResource.setBusinessId(moneyPoolRepayment.getOriginalBusinessId());
				repaymentResource.setCreateDate(new Date());
				repaymentResource.setCreateUser(loginUserInfoHelper.getUserId());
				repaymentResource.setIsCancelled(0);
				repaymentResource.setRepayAmount(moneyPoolRepayment.getAccountMoney());
				repaymentResource.setRepayDate(moneyPoolRepayment.getTradeDate());
				repaymentResource.setRepaySource("10");
				repaymentResource.setRepaySourceRefId(moneyPoolRepayment.getId().toString());
				if (save) {
					repaymentResource.insert();
				}
				repaymentResources.add(repaymentResource);
			}
		}
		// TODO 银行代扣,线下代扣尚待完善
		if (surplus != null && surplus.compareTo(new BigDecimal(0)) > 0) {
			BigDecimal canUseSurplus = accountantOverRepayLogService.caluCanUse(businessId, afterId);
			if (surplus.compareTo(canUseSurplus) > 0) {
				throw new ServiceRuntimeException("往期结余金额不足");
			}

			AccountantOverRepayLog accountantOverRepayLog = new AccountantOverRepayLog();
			accountantOverRepayLog.setBusinessAfterId(req.getAfterId());
			accountantOverRepayLog.setBusinessId(req.getBusinessId());
			accountantOverRepayLog.setCreateTime(new Date());
			accountantOverRepayLog.setCreateUser(loginUserInfoHelper.getUserId());
			accountantOverRepayLog.setFreezeStatus(0);
			accountantOverRepayLog.setIsRefund(0);
			accountantOverRepayLog.setIsTemporary(0);
			accountantOverRepayLog.setMoneyType(0);
			accountantOverRepayLog.setOverRepayMoney(req.getSurplusFund());
			accountantOverRepayLog.setRemark(String.format("支出于%s的%s期线下财务确认", req.getBusinessId(), req.getAfterId()));
			if (save) {
				accountantOverRepayLog.insert();
			}

			RepaymentResource repaymentResource = new RepaymentResource();
			repaymentResource.setAfterId(req.getAfterId());
			repaymentResource.setBusinessId(req.getBusinessId());
			repaymentResource.setCreateDate(new Date());
			repaymentResource.setCreateUser(loginUserInfoHelper.getUserId());
			repaymentResource.setIsCancelled(0);
			repaymentResource.setRepayAmount(req.getSurplusFund());
			repaymentResource.setRepayDate(new Date());
			repaymentResource.setRepaySource("11");
			repaymentResource.setRepaySourceRefId(accountantOverRepayLog.getId().toString());
			if (save) {
				repaymentResource.insert();
			}
			repayFactAmount.add(req.getSurplusFund());
			repaymentResources.add(repaymentResource);
		}

	}

	/**
	 * 查找并关联业务有关的还款计划
	 * 
	 * @author 王继光 2018年5月17日 下午9:37:57
	 * @param req
	 * @return
	 */
	private RepaymentBizPlanDto initRepaymentBizPlanDto(ConfirmRepaymentReq req) {

		RepaymentBizPlanDto repaymentBizPlanDto = new RepaymentBizPlanDto();
		RepaymentBizPlan repaymentBizPlan = new RepaymentBizPlan();
		repaymentBizPlan.setBusinessId(req.getBusinessId());
		repaymentBizPlan = repaymentBizPlanMapper.selectOne(repaymentBizPlan);
		repaymentBizPlanDto.setRepaymentBizPlan(repaymentBizPlan);

		List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper
				.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", repaymentBizPlan.getPlanId())
						.eq("business_id", req.getBusinessId()).eq("after_id", req.getAfterId()).orderBy("after_id"));

		List<RepaymentBizPlanListDto> repaymentBizPlanListDtos = new ArrayList<>();
		for (RepaymentBizPlanList repaymentBizPlanList : repaymentBizPlanLists) {
			RepaymentBizPlanListDto repaymentBizPlanListDto = new RepaymentBizPlanListDto();
			List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailMapper
					.selectList(new EntityWrapper<RepaymentBizPlanListDetail>()
							.eq("plan_list_id", repaymentBizPlanList.getPlanListId()).orderBy("share_profit_index")
							.orderBy("plan_item_type").orderBy("fee_id"));
			repaymentBizPlanListDto.setBizPlanListDetails(repaymentBizPlanListDetails);
			repaymentBizPlanListDto.setRepaymentBizPlanList(repaymentBizPlanList);
			repaymentBizPlanListDtos.add(repaymentBizPlanListDto);

		}
		repaymentBizPlanDto.setBizPlanListDtos(repaymentBizPlanListDtos);

		List<RepaymentProjPlanDto> repaymentProjPlanDtos = new ArrayList<>();
		List<RepaymentProjPlan> repaymentProjPlans = repaymentProjPlanMapper
				.selectList(new EntityWrapper<RepaymentProjPlan>().eq("business_id", req.getBusinessId()));
		for (RepaymentProjPlan repaymentProjPlan : repaymentProjPlans) {
			RepaymentProjPlanDto repaymentProjPlanDto = new RepaymentProjPlanDto();
			List<RepaymentProjPlanList> repaymentProjPlanLists = repaymentProjPlanListMapper.selectList(
					new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", repaymentProjPlan.getProjPlanId())
							.eq("business_id", req.getBusinessId()).eq("after_id", req.getAfterId())
							.orderBy("total_borrow_amount", false));
			List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = new ArrayList<>();
			for (RepaymentProjPlanList repaymentProjPlanList : repaymentProjPlanLists) {
				RepaymentProjPlanListDto repaymentProjPlanListDto = new RepaymentProjPlanListDto();
				repaymentProjPlanListDto.setRepaymentProjPlanList(repaymentProjPlanList);
				List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailMapper
						.selectList(new EntityWrapper<RepaymentProjPlanListDetail>()
								.eq("proj_plan_list_id", repaymentProjPlanList.getProjPlanListId())
								.orderBy("share_profit_index").orderBy("plan_item_type").orderBy("fee_id"));

				List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos = new ArrayList<>();
				BigDecimal unpaid = new BigDecimal(0);
				for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : repaymentProjPlanListDetails) {
					
					if (repaymentProjPlanListDetail.getProjFactAmount()==null) {
						repaymentProjPlanListDetail.setProjFactAmount(new BigDecimal(0));
					}
					unpaid = unpaid.add(repaymentProjPlanListDetail.getProjPlanAmount().subtract(repaymentProjPlanListDetail.getProjFactAmount()));
					
					RepaymentProjPlanListDetailDto repaymentProjPlanListDetailDto = new RepaymentProjPlanListDetailDto();
					repaymentProjPlanListDetailDto.setRepaymentProjPlanListDetail(repaymentProjPlanListDetail);
					List<RepaymentProjFactRepay> repaymentProjFactRepays = repaymentProjFactRepayMapper
							.selectList(new EntityWrapper<RepaymentProjFactRepay>()
									.eq("proj_plan_detail_id", repaymentProjPlanListDetail.getProjPlanDetailId())
									.orderBy("plan_item_type").orderBy("fee_id"));
					repaymentProjPlanListDetailDto.setRepaymentProjFactRepays(repaymentProjFactRepays);
					repaymentProjPlanListDetailDtos.add(repaymentProjPlanListDetailDto);

				}
				repaymentProjPlanListDto.setUnpaid(unpaid);
				repaymentProjPlanListDto.setRepaymentProjPlanListDetailDtos(repaymentProjPlanListDetailDtos);
				repaymentProjPlanListDto.setProjPlanListDetails(repaymentProjPlanListDetails);
				repaymentProjPlanListDtos.add(repaymentProjPlanListDto);
			}
			TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoMapper
					.selectById(repaymentProjPlan.getProjectId());
			CurrPeriodProjDetailVO currPeriodProjDetailVO = new CurrPeriodProjDetailVO();
			currPeriodProjDetailVO
					.setMaster(tuandaiProjectInfo.getMasterIssueId().equals(tuandaiProjectInfo.getProjectId()));
			currPeriodProjDetailVO.setProjAmount(tuandaiProjectInfo.getBorrowAmount());
			currPeriodProjDetailVO.setProject(tuandaiProjectInfo.getProjectId());
			currPeriodProjDetailVO.setUserName(tuandaiProjectInfo.getRealName());
			projListDetails.add(currPeriodProjDetailVO);
			repaymentProjPlanDto.setTuandaiProjectInfo(tuandaiProjectInfo);
			repaymentProjPlanDto.setRepaymentProjPlan(repaymentProjPlan);
			repaymentProjPlanDto.setProjPlanListDtos(repaymentProjPlanListDtos);
			repaymentProjPlanDtos.add(repaymentProjPlanDto);
		}

		repaymentBizPlanDto.setProjPlanDtos(repaymentProjPlanDtos);
		return repaymentBizPlanDto;

	}

	/**
	 * 分配逾期滞纳金到某还款来源中
	 * 
	 * @author 王继光 2018年5月24日 下午9:10:04
	 * @param req
	 */
	@Deprecated
	private void divideOverDueToRepayResource(ConfirmRepaymentReq req) {
		BigDecimal offLineOverDue = req.getOfflineOverDue();
		BigDecimal onLineOverDue = req.getOnlineOverDue();
		for (RepaymentResource resource : repaymentResources) {
			if (offLineOverDue == null && onLineOverDue == null) {
				break;
			}
			BigDecimal surplus = resource.getRepayAmount();
			if (resource.getdOfflineOverDue() == null) {
				if (offLineOverDue != null) {
					if (surplus.compareTo(offLineOverDue) >= 0) {
						resource.setdOfflineOverDue(offLineOverDue);
						surplus = surplus.subtract(offLineOverDue);
						offLineOverDue = null;
					} else {
						offLineOverDue = offLineOverDue.subtract(surplus);
						surplus = new BigDecimal(0);
					}
				}
			}
			if (resource.getdOnlineOverDue() == null) {
				if (onLineOverDue != null) {
					if (surplus.compareTo(offLineOverDue) >= 0) {
						resource.setdOfflineOverDue(offLineOverDue);
						surplus = surplus.subtract(offLineOverDue);
						onLineOverDue = null;
					} else {
						onLineOverDue = onLineOverDue.subtract(surplus);
					}
				}
			}
		}

		if (offLineOverDue != null || onLineOverDue != null) {
			logger.info("本次所有的还款来源都不够钱还线上滞纳金 或 线下滞纳金");
			// TODO 前期开发假设所有的还款来源足够填充,后期需要优化此段逻辑
		}

	}

	/**
	 * 计算每个标的占比
	 * 
	 * @author 王继光 2018年5月18日 上午10:43:18
	 * @param dto
	 * @return
	 */
	private void caluProportion(RepaymentBizPlanDto dto) {
		BigDecimal count = new BigDecimal(0);
		for (RepaymentProjPlanDto projPlanDto : dto.getProjPlanDtos()) {
			count = count.add(projPlanDto.getRepaymentProjPlan().getBorrowMoney());
		}
		for (RepaymentProjPlanDto projPlanDto : dto.getProjPlanDtos()) {
			BigDecimal proportion = projPlanDto.getRepaymentProjPlan().getBorrowMoney().divide(count).setScale(10,
					BigDecimal.ROUND_HALF_UP);
			projPlanDto.setProportion(proportion);
		}
	}

	/**
	 * 计算每个标的分配下来的金额
	 * 
	 * @author 王继光 2018年5月18日 上午10:43:18
	 * @param dto
	 * @return
	 */
	private void divideMoney(BigDecimal money, RepaymentBizPlanDto dto) {
		BigDecimal moneyCopy = money;
		for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
			RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
			if (i == dto.getProjPlanDtos().size() - 1) {
				repaymentProjPlanDto.setDivideAmount(moneyCopy);
			} else {
				BigDecimal dmoney = money.multiply(repaymentProjPlanDto.getProportion());
				repaymentProjPlanDto.setDivideAmount(dmoney);
				moneyCopy = moneyCopy.subtract(dmoney);
			}
		}
	}

	/**
	 * 计算每个标的分配下来的滞纳金金额
	 * 
	 * @author 王继光 2018年5月24日 下午9:27:27
	 * @param money
	 * @param dto
	 * @param online
	 *            true=分配线上滞纳金到标的,false=分配线下滞纳金到标的
	 */
	private void divideOveryDueMoney(BigDecimal money, RepaymentBizPlanDto dto, boolean online) {
		BigDecimal moneyCopy = money;
		for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
			RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
			if (i == dto.getProjPlanDtos().size() - 1) {
				if (online) {
					repaymentProjPlanDto.setOnlineOverDue(moneyCopy);
				} else {
					repaymentProjPlanDto.setOfflineOverDue(moneyCopy);
				}
			} else {
				BigDecimal dmoney = money.multiply(repaymentProjPlanDto.getProportion());
				if (online) {
					repaymentProjPlanDto.setOnlineOverDue(moneyCopy);
				} else {
					repaymentProjPlanDto.setOfflineOverDue(moneyCopy);
				}
				moneyCopy = moneyCopy.subtract(dmoney);
			}
		}
	}

	private CurrPeriodProjDetailVO getCurrPeriodProjDetailVO(String projectId) {
		for (CurrPeriodProjDetailVO currPeriodProjDetailVO : projListDetails) {
			if (currPeriodProjDetailVO.getProject().equals(projectId)) {
				return currPeriodProjDetailVO;
			}
		}
		return null;
	}

	// 填充detail
	private void fill() {
		for (RepaymentResource resource : repaymentResources) {
			BigDecimal repayAmount = resource.getRepayAmount();
			
			for (RepaymentProjPlanDto projPlanDto : planDto.getProjPlanDtos()) {

				if (repayAmount==null) {
					break ;
				}
				
				BigDecimal divideAmount = projPlanDto.getDivideAmount();
				
				int c = repayAmount.compareTo(divideAmount) ;
				if (c>0) {
					repayAmount = repayAmount.subtract(divideAmount);
				}else if(c==0) {
					repayAmount = null ;
				}else {
					repayAmount = null ;
				}
				BigDecimal offLineOverDue = projPlanDto.getOfflineOverDue()==null?new BigDecimal(0):projPlanDto.getOfflineOverDue();
				BigDecimal onLineOverDue = projPlanDto.getOnlineOverDue()==null?new BigDecimal(0):projPlanDto.getOnlineOverDue();
				divideAmount = divideAmount.subtract(offLineOverDue).subtract(onLineOverDue);
				
				List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = projPlanDto.getProjPlanListDtos();
				String projectId = projPlanDto.getTuandaiProjectInfo().getProjectId();
				CurrPeriodProjDetailVO currPeriodProjDetailVO = getCurrPeriodProjDetailVO(projectId);
				for (RepaymentProjPlanListDto repaymentProjPlanListDto : repaymentProjPlanListDtos) {
					if (divideAmount==null&&offLineOverDue==null&&onLineOverDue==null) {
						break;
					}
					
					RepaymentProjPlanList projPlanList = repaymentProjPlanListDto.getRepaymentProjPlanList();
					BigDecimal unpaid = repaymentProjPlanListDto.getUnpaid();
					if (unpaid==null) {
						continue ;
					}
					
					List<RepaymentProjPlanListDetail> details = repaymentProjPlanListDto.getProjPlanListDetails();
					for (RepaymentProjPlanListDetail detail : details) {
						switch (detail.getPlanItemType()) {
						case 10:
						case 20:
						case 30:
						case 50:
							createProjFactRepay(divideAmount,detail, currPeriodProjDetailVO);
							break;
						case 60:
							if (detail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
								createProjFactRepay(onLineOverDue, detail, currPeriodProjDetailVO);
							}
							if (detail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
								createProjFactRepay(offLineOverDue, detail, currPeriodProjDetailVO);
							}
							break;
						default:
							createProjFactRepay(divideAmount, detail, currPeriodProjDetailVO);
							break;
						}
					}
				}
			}
		}
		
	}
	/**
	 * 将填充到实还的资金拷贝一份填充到CurrPeriodProjDetailVO
	 * @author 王继光
	 * 2018年5月24日 下午11:44:50
	 * @param amount
	 * @param detail
	 * @param vo
	 */
	private void rendCurrPeriodProjDetailVO (BigDecimal amount,RepaymentProjPlanListDetail detail,CurrPeriodProjDetailVO vo) {
		switch (detail.getPlanItemType()) {
		case 10:
			vo.setItem10(vo.getItem10().add(amount));
		case 20:
			vo.setItem20(vo.getItem20().add(amount));
		case 30:
			vo.setItem30(vo.getItem30().add(amount));
		case 50:
			vo.setItem50(vo.getItem50().add(amount));
			break;
		case 60:
			if (detail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
				vo.setOnlineOverDue(vo.getOnlineOverDue().add(amount));
			}
			if (detail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
				vo.setOfflineOverDue(vo.getOfflineOverDue().add(amount));
			}
			break;
		default:
			break;
		}
	} 
	/**
	 * 根据RepaymentProjPlanListDetail和实还金额创建RepaymentProjFactRepay
	 * @author 王继光
	 * 2018年5月24日 下午11:45:26
	 * @param divideAmount
	 * @param detail
	 * @param vo
	 * @return
	 */
	private RepaymentProjFactRepay createProjFactRepay(BigDecimal divideAmount,RepaymentProjPlanListDetail detail,CurrPeriodProjDetailVO vo) {
		if (divideAmount!=null) {
			RepaymentProjFactRepay fact = new RepaymentProjFactRepay() ;
			fact.setAfterId(afterId);
			fact.setBusinessId(businessId);
			fact.setCreateDate(new Date());
			fact.setCreateUser(loginUserInfoHelper.getUserId());
			fact.setOrigBusinessId(detail.getOrigBusinessId());
			fact.setPeriod(detail.getPeriod());
			fact.setPlanItemName(detail.getPlanItemName());
			fact.setPlanItemType(detail.getPlanItemType());
			fact.setPlanListId(detail.getPlanListId());
			fact.setProjPlanDetailId(detail.getProjPlanDetailId());
			fact.setProjPlanListId(detail.getProjPlanListId());
			fact.setFactRepayDate(null);//还款来源日期
			fact.setRepayRefId(null);//还款来源id
			fact.setRepaySource(null);//还款来源类别
			if (detail.getProjFactAmount()==null) {
				detail.setProjFactAmount(new BigDecimal(0));
			}
			BigDecimal unpaid = detail.getProjPlanAmount().subtract(detail.getProjFactAmount());
			int c = divideAmount.compareTo(unpaid);
			if (c>0) {
				divideAmount = divideAmount.subtract(unpaid);
				detail.setProjFactAmount(detail.getProjFactAmount().add(unpaid));
				fact.setFactAmount(unpaid);
				rendCurrPeriodProjDetailVO(unpaid, detail, vo);
			}else if (c==0) {
				divideAmount = null ;
				detail.setProjFactAmount(detail.getProjFactAmount().add(unpaid));
				fact.setFactAmount(unpaid);
				rendCurrPeriodProjDetailVO(unpaid, detail, vo);
			}else {
				detail.setProjFactAmount(detail.getProjFactAmount().add(divideAmount));
				fact.setFactAmount(divideAmount);
				rendCurrPeriodProjDetailVO(divideAmount, detail, vo);
				divideAmount = null ;
			}
		}
		return null;
	}
}
