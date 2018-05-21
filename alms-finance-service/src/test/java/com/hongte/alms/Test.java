/**
 * 
 */
package com.hongte.alms;


import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.finance.FinanceServiceApplication;
import com.hongte.alms.finance.service.FinanceService;

/**
 * @author 王继光
 * 2018年5月17日 上午11:31:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceApplication.class)
public class Test {

	@Autowired
	@Qualifier("FinanceService")
	private FinanceService financeService ;
	
	@org.junit.Test
	public void Test() {
		ConfirmRepaymentReq req = new ConfirmRepaymentReq() ;
		req.setBusinessId("TDF1012018031505-5-216");
		req.setAfterId("1-01");
		req.setOfflineOverDue(new BigDecimal(500));
		req.setOnlineOverDue(new BigDecimal(100));
		req.setMprIds(Arrays.asList("75","76","77"));
		Result result = financeService.confirmRepayment(req);
		
	}
}
