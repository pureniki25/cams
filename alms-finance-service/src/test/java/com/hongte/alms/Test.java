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

import com.alibaba.fastjson.JSON;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.vo.finance.CurrPeriodRepaymentInfoVO;
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
	
	@Autowired
	@Qualifier("RepaymentConfirmLogService")
	private RepaymentConfirmLogService confirmLogService ;
	
	@org.junit.Test
	public void Test() {
//		Result result = confirmLogService.revokeConfirm("TDF1012018032101", "1-01");
	}
}
