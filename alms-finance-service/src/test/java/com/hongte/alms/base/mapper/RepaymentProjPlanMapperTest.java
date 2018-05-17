/**
 * 
 */
package com.hongte.alms.base.mapper;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.finance.FinanceServiceApplication;

/**
 * @author 王继光
 * 2018年5月17日 上午9:54:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceApplication.class)
public class RepaymentProjPlanMapperTest {

	@Autowired
	private RepaymentProjPlanMapper repaymentProjPlanMapper ;
	/**
	 * Test method for {@link com.hongte.alms.base.mapper.RepaymentProjPlanMapper#selectProjPlanProjectInfo(java.lang.String)}.
	 */
	@Test
	public void testSelectProjPlanProjectInfo() {
		List<Map<String, Object>> list = repaymentProjPlanMapper.selectProjPlanProjectInfo("TDF1012018031505");
		System.out.println(JSON.toJSONString(list));
	}

}
