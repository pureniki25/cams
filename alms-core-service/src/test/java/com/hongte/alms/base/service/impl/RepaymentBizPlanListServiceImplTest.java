package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.core.CoreServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author:喻尊龙
 * @date: 2018/3/20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreServiceApplication.class)
public class RepaymentBizPlanListServiceImplTest {

    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    private RepaymentBizPlanListService repaymentBizPlanListService;

    @Test
    public void selectNeedPhoneUrgNorBiz() throws Exception {
        repaymentBizPlanListService.selectNeedPhoneUrgNorBiz("123",30);
    }

    @Test
    public void selectNeedVisitNorBiz() throws Exception {
    }

    @Test
    public void selectNeedLawNorBiz() throws Exception {
    }

}