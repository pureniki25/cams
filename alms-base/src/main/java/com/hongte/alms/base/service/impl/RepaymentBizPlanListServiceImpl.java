package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.dto.FinanceManagerListReq;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.vo.module.FinanceManagerListVO;
import com.hongte.alms.base.dto.FinanceManagerListReq;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.vo.PageResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业务还款计划列表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-06
 */
@Service("RepaymentBizPlanListService")
public class RepaymentBizPlanListServiceImpl extends BaseServiceImpl<RepaymentBizPlanListMapper, RepaymentBizPlanList> implements RepaymentBizPlanListService {

    @Autowired
    RepaymentBizPlanListMapper repaymentBizPlanListMapper;


    @Override
    public List<RepaymentBizPlanList> selectNeedPhoneUrgNorBiz(String companyId,Integer overDueDays,Integer businessType) {
//        if(overDueDays.equals(0)){
//            overDueDays = 1000;
//        }
       return repaymentBizPlanListMapper.selectNeedSetColInfoNormalBizPlansBycomId(
                companyId,
                overDueDays,
                CollectionStatusEnum.PHONE_STAFF.getKey(),businessType);
    }

    @Override
    public List<RepaymentBizPlanList> selectNeedVisitNorBiz(String companyId,Integer overDueDays,Integer businessType ) {
        return repaymentBizPlanListMapper.selectNeedSetColInfoNormalBizPlansBycomId(
                companyId,
                overDueDays,
                CollectionStatusEnum.COLLECTING.getKey(),businessType);

    }

    @Override
    public List<RepaymentBizPlanList> selectNeedLawNorBiz(Integer overDueDays,Integer businessType ) {
        return repaymentBizPlanListMapper.selectNeedSetColInfoNormalBizPlansBycomId(
                null,
                overDueDays,
                CollectionStatusEnum.TO_LAW_WORK.getKey(),businessType);
    }
    
    @Override
    public String queryRepaymentBizPlanListByConditions(String businessId, String afterId) {
    	return repaymentBizPlanListMapper.queryRepaymentBizPlanListByConditions(businessId, afterId);
    }

	@Override
	public PageResult selectByFinanceManagerListReq(FinanceManagerListReq req) {
		int count = repaymentBizPlanListMapper.conutFinanceManagerList(req);
		List<FinanceManagerListVO> list = repaymentBizPlanListMapper.selectFinanceMangeList(req);
		return PageResult.success(list, count);
	}

    @Override
    public RepaymentBizPlanList findCurrentPeriod(Date settleDate, List<RepaymentBizPlanList> planLists) {
        RepaymentBizPlanList finalPeriod = planLists.get(planLists.size() - 1);
        int diff = DateUtil.getDiffDays(settleDate, finalPeriod.getDueDate());
        RepaymentBizPlanList currentPeriod = null;

        // 提前还款结清
        if (diff > 0) {
            RepaymentBizPlanList temp = new RepaymentBizPlanList();
            temp.setDueDate(settleDate);
            temp.setBusinessId("temp");
            // 把提前结清的日期放进PlanLists一起比较
            planLists.add(temp);
            planLists.sort(
                    (RepaymentBizPlanList a1, RepaymentBizPlanList a2) -> a1.getDueDate().compareTo(a2.getDueDate()));
            for (int i = 0; i < planLists.size(); i++) {
                if (planLists.get(i).getBusinessId().equals("temp")) {
                    currentPeriod = planLists.get(i + 1);
                }
            }

            // 筛选temp记录
            for (Iterator<RepaymentBizPlanList> it = planLists.iterator(); it.hasNext();) {
                RepaymentBizPlanList pList = it.next();
                if (pList.getBusinessId().equals("temp")) {
                    it.remove();
                }
            }

        } else {
            // 期外
            currentPeriod = finalPeriod;
        }



        return currentPeriod;
    }


//    @Override
//    public List<RepaymentBizPlanList> selectNeedPhoneUrgRenewBiz(String companyId,Integer beforeDueDays) {
//        if(beforeDueDays.equals(0)){
//            beforeDueDays = 1000;
//        }
//       return repaymentBizPlanListMapper.selectNeedSetColInfoRenewBizPlansBycomId(
//               companyId,
//               0-beforeDueDays,
//               CollectionStatusEnum.PHONE_STAFF.getKey()
//       );
//    }
//
//    @Override
//    public List<RepaymentBizPlanList> selectNeedVisitRenewBiz(String companyId,Integer overDueDays) {
//        return repaymentBizPlanListMapper.selectNeedSetColInfoRenewBizPlansBycomId(
//                companyId,
//                overDueDays,
//                CollectionStatusEnum.COLLECTING.getKey()
//        );
//    }
//
//    @Override
//    public List<RepaymentBizPlanList> selectNeedLawRenewBiz(Integer overDueDays) {
//        return repaymentBizPlanListMapper.selectNeedSetColInfoRenewBizPlansBycomId(
//                null,
//                overDueDays,
//                CollectionStatusEnum.TO_LAW_WORK.getKey()
//        );
//    }

    @Override
    public Integer queryFirstPeriodOverdueByBusinessId(String businessId) {
    	return repaymentBizPlanListMapper.queryFirstPeriodOverdueByBusinessId(businessId);
    }

    @Override
    public Integer queryInterestOverdueByBusinessId(String businessId) {
    	return repaymentBizPlanListMapper.queryInterestOverdueByBusinessId(businessId);
    }

    @Override
    public Integer queryPrincipalOverdueByBusinessId(String businessId) {
    	return repaymentBizPlanListMapper.queryPrincipalOverdueByBusinessId(businessId);
    }
}
