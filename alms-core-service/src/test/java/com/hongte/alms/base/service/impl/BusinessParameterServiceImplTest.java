package com.hongte.alms.base.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hongte.alms.base.service.BusinessParameterService;
import com.hongte.alms.base.vo.module.classify.ClassifyConditionVO;
import com.hongte.alms.core.CoreServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreServiceApplication.class)
public class BusinessParameterServiceImplTest {
	
	@Autowired
	private BusinessParameterService businessParameterService;
	
	@Test
	public void testFiveLevelClassifyForBusiness() {
		ClassifyConditionVO vo = new ClassifyConditionVO();
		vo.setBusinessId("TDC5012017101402");
		vo.setOpSourse("1");
		String className = businessParameterService.fiveLevelClassifyForBusiness(vo);
		System.out.println(className);
	}
}
