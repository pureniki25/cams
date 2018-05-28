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
import com.hongte.alms.base.feignClient.dto.CustomerInfoDto;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.withhold.service.RechargeService;
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

    
    @Autowired
    @Qualifier("RechargeService")
    RechargeService rechargeService;
	@Override
	public void withholding() {
		List<RepaymentBizPlanList> pLists=repaymentBizPlanListService.selectAutoRepayList();
		for(RepaymentBizPlanList pList:pLists) {
			//不是最后一期才能代扣
			if(!rechargeService.istLastPeriod(pList)) {
				
			}else {
				continue;
			}
		}
	}
	


	
	
	
	
	/*
	 * 每一期自动代扣
	 */
	
	private void autoRepayPerList(RepaymentBizPlanList pList) {
		
		
		
		
	}
	
	
	
	
	
	
	
	
	

	
	
	
	
}
