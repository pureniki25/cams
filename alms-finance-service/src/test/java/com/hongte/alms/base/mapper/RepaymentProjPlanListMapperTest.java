/**
 * 
 */
package com.hongte.alms.base.mapper;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.finance.FinanceServiceApplication;

/**
 * @author 王继光
 * 2018年5月17日 上午10:45:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceApplication.class)
public class RepaymentProjPlanListMapperTest {

	@Autowired
	private RepaymentProjPlanListMapper RepaymentProjPlanListMapper ;
	/**
	 * Test method for {@link com.hongte.alms.base.mapper.RepaymentProjPlanListMapper#selectByProjectIDAndAfterId(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSelectByProjectIDAndAfterId() {
		RepaymentProjPlanList list =  RepaymentProjPlanListMapper.selectByProjectIDAndAfterId("137e8a4a-0727-4551-b20a-48b0d6679cfa", "1-01");
		System.out.println(JSON.toJSONString(list));
	}

}
