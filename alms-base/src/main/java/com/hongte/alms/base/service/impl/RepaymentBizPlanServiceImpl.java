package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.RepayPlan.vo.PlanVo;
import com.hongte.alms.base.RepayPlan.vo.RepayingPlanDto;
import com.hongte.alms.base.RepayPlan.vo.RepayingPlanVo;
import com.hongte.alms.base.collection.entity.CollectionStatus;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.collection.vo.DeductionVo;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.WithholdingRecordLog;
import com.hongte.alms.base.enums.repayPlan.RepayPlanSettleStatusEnum;
import com.hongte.alms.base.mapper.DeductionMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanMapper;
import com.hongte.alms.base.process.entity.ProcessLog;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.WithholdingRecordLogService;
import com.hongte.alms.base.vo.finance.RepaymentSettleListVO;
import com.hongte.alms.base.vo.module.RepaymentOpenServiceVO;
import com.hongte.alms.base.vo.module.api.RepayDetailResultRespData;
import com.hongte.alms.base.vo.module.api.RepayResultRespData;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.DateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 业务还款计划信息 服务实现类
 * </p>
 *
 * @since 2018-03-06
 */
@Service("RepaymentBizPlanService")
@Transactional
public class RepaymentBizPlanServiceImpl extends BaseServiceImpl<RepaymentBizPlanMapper, RepaymentBizPlan> implements RepaymentBizPlanService {


	@Autowired
	@Qualifier("WithholdingRecordLogService")
	WithholdingRecordLogService WithholdingRecordLogService;


	@Autowired
	RepaymentBizPlanMapper repaymentBizPlanMapper;
//	@Autowired
//	@Qualifier("RepaymentBizPlanService")
//	RepaymentBizPlanService repaymentBizPlanService;


	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService;

	@Autowired
	@Qualifier("RepaymentBizPlanListDetailService")
	RepaymentBizPlanListDetailService repaymentBizPlanDetailListService;

	@Autowired
	@Qualifier("CollectionStatusService")
	CollectionStatusService collectionStatusService;


	/**
	 * 根据主业务编号和afterId查询还款计划详细信息
	 */
	@Override
	public List<RepaymentOpenServiceVO> selectRepaymentOpenServiceList(String originalBusinessId, String afterId) {
		List<RepaymentOpenServiceVO> list = repaymentBizPlanMapper.selectRepaymentOpenServiceList(originalBusinessId, afterId);
		return list;
	}


	/**
	 * 代扣结果更新表记录
	 *
	 * @author chenzs
	 */


	@Transactional(rollbackFor = Exception.class)
	@Override
	public void repayResultUpdateRecord(RepayResultRespData data) {
		//根据主业务编号和afterId查询出3张还款计划表的记录
		List<RepaymentOpenServiceVO> list = selectRepaymentOpenServiceList(data.getOriginalBusinessId(), data.getAfterId());
		RepaymentOpenServiceVO vo = null;
		if (list != null && list.size() > 0) {
			vo = list.get(0);
		}

		//更新Plan表
		RepaymentBizPlan plan = selectOne(new EntityWrapper<RepaymentBizPlan>().eq("plan_id", vo.getPlanId()));
		plan.setPlanStatus(Integer.parseInt(data.getPlanStatus()));
		updateById(plan);

		//更新Plan_list表

		RepaymentBizPlanList planList = repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("plan_list_id", vo.getPlanListId()));
		planList.setDueDate(DateUtil.getDate(data.getDueDate()));
		planList.setTotalBorrowAmount(BigDecimal.valueOf(Double.valueOf(data.getTotalBorrowAmount())));
		planList.setOverdueAmount(BigDecimal.valueOf(Double.valueOf(data.getOverdueAmount())));
		planList.setOverdueDays(BigDecimal.valueOf(Double.valueOf(data.getOverdueDays())));
		planList.setCurrentStatus(data.getCurrentStatus());
		planList.setRepayFlag(Integer.valueOf(data.getRepayFlag()));
		planList.setFactRepayDate(DateUtil.getDate(data.getFactRepayDate()));
		planList.setFinanceComfirmDate(DateUtil.getDate(data.getFinanceComfirmDate()));
		planList.setFinanceConfirmUser(data.getFinanceConfirmUser());
		planList.setFinanceConfirmUserName(data.getFinanceConfirmUserName());
		planList.setConfirmFlag(Integer.valueOf(data.getConfirmFlag()));
		planList.setAutoWithholdingConfirmedDate(DateUtil.getDate(data.getAutoWithholdingConfirmedDate()));
		planList.setAutoWithholdingConfirmedUser(data.getAutoWithholdingConfirmedUser());
		planList.setAccountantConfirmStatus(Integer.valueOf(data.getAccountantConfirmStatus()));
		planList.setAccountantConfirmUser(data.getAccountantConfirmUser());
		planList.setAccountantConfirmUserName(data.getAccountantConfirmUserName());
		planList.setAccountantConfirmDate(DateUtil.getDate(data.getAccountantConfirmDate()));
		repaymentBizPlanListService.updateById(planList);


		List<RepayDetailResultRespData> repayResultDetailList = data.getRepayResultDetailList();

		//先根据FeeId取修改对象的planListDetail记录并从接口返回的repayResultDetailList中筛选出贷后系统planListDetail表中没有FeeId的记录
		for (RepaymentOpenServiceVO repaymentOpenServiceVO : list) {
			updatePlanListDetail(repaymentOpenServiceVO.getPlanDetailId(), repayResultDetailList);
		}
		//插入贷后系统planListDetail表数据，数据来源于repayResultDetailList中剩下的记录
		for (RepayDetailResultRespData respData : repayResultDetailList) {
			RepaymentBizPlanListDetail detail = new RepaymentBizPlanListDetail();
			detail.setPlanDetailId(respData.getPlanDetailId());
			detail.setPlanListId(vo.getPlanListId());
			detail.setBusinessId(planList.getBusinessId());
			detail.setPeriod(Integer.valueOf(respData.getPeriod()));
			detail.setPlanAmount(BigDecimal.valueOf(Double.valueOf(respData.getPlanAmount())));
			detail.setPlanRate(BigDecimal.valueOf(Double.valueOf(respData.getPlanRate())));
			detail.setFeeId(respData.getFeeId());
			detail.setPlanItemName(respData.getPlanItemName());
			detail.setPlanItemType(Integer.valueOf(respData.getPlanItemType()));
			detail.setAccountStatus(Integer.valueOf(respData.getAccountStatus()));
			detail.setFactAmount(BigDecimal.valueOf(Double.valueOf(respData.getFactAmount())));
			detail.setRepaySource(Integer.valueOf(respData.getRepaySource()));
			detail.setFactRepayDate(DateUtil.getDate(respData.getFactRepayDetailDate()));
			detail.setCreateDate(new Date());
			detail.setCreateUser("接口同步数据");

			repaymentBizPlanDetailListService.insert(detail);

			List<WithholdingRecordLog> loglist = WithholdingRecordLogService.selectWithholdingRecordLog(data.getOriginalBusinessId(), data.getAfterId());
			WithholdingRecordLog log = loglist.get(0);
			log.setRepayStatus(1);
			WithholdingRecordLogService.updateById(log);

			CollectionStatus status = collectionStatusService.selectOne(new EntityWrapper<CollectionStatus>().eq("crp_id", vo.getPlanListId()));
			status.setCollectionStatus(200);
			collectionStatusService.updateById(status);


		}


	}

	//更新planListDetail 数据
	private void updatePlanListDetail(String pDetailId, List<RepayDetailResultRespData> repayResultDetailList) {
		RepaymentBizPlanListDetail detail = repaymentBizPlanDetailListService.selectOne(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_detail_id", pDetailId));
		Iterator iter = repayResultDetailList.iterator();
		while (iter.hasNext()) {
			RepayDetailResultRespData repsDetail = (RepayDetailResultRespData) iter.next();
			detail.setPeriod(Integer.valueOf(repsDetail.getPeriod()));
			detail.setPlanAmount(BigDecimal.valueOf(Double.valueOf(repsDetail.getPlanAmount())));
			detail.setPlanRate(BigDecimal.valueOf(Double.valueOf(repsDetail.getPlanRate())));
			detail.setPlanItemName(repsDetail.getPlanItemName());
			detail.setPlanItemType(Integer.valueOf(repsDetail.getPlanItemType()));
			detail.setAccountStatus(Integer.valueOf(repsDetail.getAccountStatus()));
			detail.setFactAmount(BigDecimal.valueOf(Double.valueOf(repsDetail.getFactAmount())));
			detail.setRepaySource(Integer.valueOf(repsDetail.getRepaySource()));
			detail.setFactRepayDate(DateUtil.getDate(repsDetail.getFactRepayDetailDate()));
			repaymentBizPlanDetailListService.updateById(detail);
			iter.remove();
		}

	}





	public Page<RepayingPlanVo> queryCustomeRepayPlanInfo(String identifyCard, Integer pageIndex, Integer pageSize) throws IllegalAccessException, InstantiationException {

		Page<RepayingPlanVo> voPage = new Page<>();

		Page<RepayingPlanDto> dtoPage = queryCustomerRepayPlan(identifyCard, pageIndex, pageSize);


		voPage.setCurrent(dtoPage.getCurrent());
		voPage.setSize(dtoPage.getSize());
		voPage.setTotal(dtoPage.getTotal());
		List<RepayingPlanVo> voList = new LinkedList<>();
		voPage.setRecords(voList);

		List<RepayingPlanDto> dtoList = dtoPage.getRecords();

		for (RepayingPlanDto dto : dtoList) {
			RepayingPlanVo vo = ClassCopyUtil.copyObject(dto, RepayingPlanVo.class);
			voList.add(vo);
			//判断是否已结清
			if (RepayPlanSettleStatusEnum.payed(dto.getPlanStatus())) {
				vo.setIsOver(true);
				vo.setTip("已结清");
			} else {
				vo.setIsOver(false);
			}

			//判断是否已申请展期
			if (RepayPlanSettleStatusEnum.renewed(dto.getPlanStatus())) {
				vo.setTip("已展期");
				vo.setHasDeffer(true);
			} else {
				vo.setHasDeffer(false);
			}

			List<RepaymentBizPlan> bizPlans = selectList(new EntityWrapper<RepaymentBizPlan>().eq("original_business_id", dto.getBusinessId()));
			List<PlanVo> planVos = new LinkedList<>();
			vo.setPlans(planVos);
			for (RepaymentBizPlan bizPlan : bizPlans) {
				List<RepaymentBizPlanList> bizPlanLists = repaymentBizPlanListService.selectList(
						new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", bizPlan.getPlanId())
								.orderBy("due_date"));
				for (RepaymentBizPlanList bizPlanList : bizPlanLists) {
					//展期已结清的那一期不返回
					if(bizPlanList.getRepayFlag()!=null
							&& bizPlanList.getRepayFlag().equals(6)){
						continue;
					}
					PlanVo planVo = new PlanVo();
					planVos.add(planVo);
					planVo.setPeriod(bizPlanList.getPeriod().toString());
					planVo.setAfterId(bizPlanList.getAfterId());
					planVo.setDate(bizPlanList.getDueDate());
					planVo.setTotalAmount(bizPlanList.getTotalBorrowAmount());
					planVo.setStatus(bizPlanList.getCurrentStatus());
					if (RepayPlanSettleStatusEnum.renewed(bizPlan.getPlanStatus())) {
						planVo.setHasDeffer(true);
					} else {
						planVo.setHasDeffer(false);
					}
					if (RepayPlanSettleStatusEnum.payed(dto.getPlanStatus())) {
						planVo.setIsOver(true);
					} else {
						planVo.setIsOver(false);
					}
				}

			}

		}
		return voPage;
	}

	/**
	 * 查询客户还款计划列表
	 *
	 * @param identifyCard
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	private Page<RepayingPlanDto> queryCustomerRepayPlan(String identifyCard, Integer pageIndex, Integer pageSize) {
		Page<RepayingPlanDto> pages = new Page<>();
		pages.setSize(pageSize);
		pages.setCurrent(pageIndex);

		List<RepayingPlanDto> lsit = repaymentBizPlanMapper.queryCustomerRepayPlan(pages, identifyCard);

		pages.setRecords(lsit);

		return pages;
	}


	@Override
	public List<RepaymentSettleListVO> listRepaymentSettleListVOs(String businessId, String planId) {
		List<RepaymentSettleListVO> list = repaymentBizPlanMapper.listRepaymentSettleListVOs(businessId, planId);
		RepaymentSettleListVO finalVO = list.get(list.size()-1) ;
		List<RepaymentSettleListVO> currents = new ArrayList<>();
		RepaymentSettleListVO curr = null ;
		findCurrentPeriod(finalVO, list, currents, curr);
		//TODO
		
		return repaymentBizPlanMapper.listRepaymentSettleListVOs(businessId, planId);
	}
	
	private void findCurrentPeriod(RepaymentSettleListVO finalVO,List<RepaymentSettleListVO> list,List<RepaymentSettleListVO> currents,RepaymentSettleListVO curr) {
		Date now = new Date();
		int outOfContact = DateUtil.getDiffDays(finalVO.getRepayDate(), now) ;
		if (outOfContact>=0) {
			/*期外逾期*/
			for (RepaymentSettleListVO repaymentSettleListVO : list) {
				if (repaymentSettleListVO.samePeriod(finalVO)) {
					currents.add(repaymentSettleListVO);
				}
			}
			curr = finalVO ;
		}else if(outOfContact<0){
			/*提前结清*/
			for (RepaymentSettleListVO repaymentSettleListVO : currents) {
				if (DateUtil.getDiff(now, repaymentSettleListVO.getRepayDate())>0) {
					curr = repaymentSettleListVO ;
					break;
				}
			}
			
			for (RepaymentSettleListVO repaymentSettleListVO : currents) {
				if (repaymentSettleListVO.samePeriod(curr)) {
					currents.add(curr);
				}
			}
		}
	}
}