package com.hongte.alms.finance.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hongte.alms.finance.PlatRepayServiceApplication;
import com.hongte.alms.finance.service.FinanceService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlatRepayServiceApplication.class)
public class FinanceServiceImplTest {

	@Autowired
	@Qualifier("FinanceService")
	private FinanceService financeService;
	
	@Test
	public void testSendLoanBalanceToDataPlatform() {
		String businessId = "TDC1012016122905";
		financeService.sendLoanBalanceToDataPlatform(businessId);
	}
}
