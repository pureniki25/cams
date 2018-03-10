package com.hongte.alms.scheduled.job;

import com.hongte.alms.base.process.service.ProcessTypeService;
import com.hongte.alms.base.process.service.ProcessTypeStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zengkun
 * @since 2018/2/7
 */
@Component
public class TestJobClass {

    @Autowired
    @Qualifier("ProcessTypeService")
    ProcessTypeService processTypeService;

    @Autowired
    @Qualifier("ProcessTypeStepService")
    ProcessTypeStepService processTypeStepService;

    @Scheduled(cron = "0/1 * * * * ? ")
    public  void jobTest(){
//        List<ProcessTypeStep> stepList = processTypeStepService.getProcessTypeStep(Constant.APPLY_DERATE_PROCEEE_TYPE_ID);

    }

}
