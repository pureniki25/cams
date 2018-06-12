package com.hongte.alms.platrepay.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.hongte.alms.platrepay.PlatRepayServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlatRepayServiceApplication.class)
public class AgencyRechargeLogServiceTest {
	
	@Autowired
	@Qualifier("AgencyRechargeLogService")
	private AgencyRechargeLogService agencyRechargeLogService;
	
	@Test
	public void testCallBackAgencyRecharge() {
		agencyRechargeLogService.callBackAgencyRecharge("3719e765-abb3-411f-9901-84acdebb1b2c", "2");
	}
	
	@Test
	public void testQueryRechargeOrder() {
		agencyRechargeLogService.queryRechargeOrder("171014", "213211212121aswq121a121s", "test");
	}
}