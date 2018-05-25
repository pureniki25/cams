package com.hongte.alms.withhold.service.impl;


import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.withhold.service.WithholdingService;

/**
 * @author czs
 * @since 2018/5/24
 * 自动代扣的服务
 */
@Service("WithholdingService")
public class WithholdingServiceimpl  implements WithholdingService {


    private  static Logger logger = LoggerFactory.getLogger(WithholdingServiceimpl.class);
    @Autowired
    Executor executor;    
    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    RepaymentBizPlanListService repaymentBizPlanListService;

	@Override
	public void withholding() {
		List<RepaymentBizPlanList> pLists=repaymentBizPlanListService.selectAutoRepayList();
		for(RepaymentBizPlanList pList:pLists) {
			//不是最后一期才能代扣
			if(!istLastPeriod(pList)) {
				
			}else {
				continue;
			}
		}
	}
	


	
	
	
	
	
	private void autoRepayPerList(RepaymentBizPlanList pList) {
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
    /**
     * 
     * 判断每期还款计划是否为最后一期
     * @param projPlanList
     * @return
     */
	private boolean istLastPeriod(RepaymentBizPlanList pList) {
		boolean isLast=false;
		List<RepaymentBizPlanList> pLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", pList.getPlanId()));
		RepaymentBizPlanList lastpList=pLists.stream().max(new Comparator<RepaymentBizPlanList>() {
			@Override
			public int compare(RepaymentBizPlanList o1, RepaymentBizPlanList o2) {
				return o1.getDueDate().compareTo(o2.getDueDate());
			}
		}).get();
		
		if(pList.getPlanListId().equals(lastpList.getPlanListId())) {
			isLast=true;
		}
		return isLast;
	}	
	
	
	
}
