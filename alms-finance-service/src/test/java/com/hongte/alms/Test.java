/**
 * 
 */
package com.hongte.alms;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hongte.alms.base.mapper.MoneyPoolRepaymentMapper;
import com.hongte.alms.finance.FinanceServiceApplication;

/**
 * @author 王继光
 * 2018年5月17日 上午11:31:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceApplication.class)
public class Test {

	@Autowired
	MoneyPoolRepaymentMapper MoneyPoolRepaymentMapper ;
	@org.junit.Test
	public void test() {
		List<String> strings = Arrays.asList("69","70","71","72","73","74");
		BigDecimal result = MoneyPoolRepaymentMapper.sumMoneyPoolRepaymentAmountByMprIds(strings);
		System.out.println(result);
	}

}
