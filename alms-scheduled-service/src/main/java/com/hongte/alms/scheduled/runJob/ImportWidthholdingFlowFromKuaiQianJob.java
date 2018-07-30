package com.hongte.alms.scheduled.runJob;

import com.hongte.alms.base.service.WithholdingFlowRecordService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 导入代扣流水-快钱
 * Created by 张贵宏 on 2018/7/30 9:50
 */
@JobHandler("ImportWidthholdingFlowFromKuaiQianJob")
@Component
public class ImportWidthholdingFlowFromKuaiQianJob extends IJobHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportWidthholdingFlowFromYiBaoJob.class);
    @Autowired
    @Qualifier("WithholdingFlowRecordService")
    private WithholdingFlowRecordService withholdingFlowRecordService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            LOGGER.info("@ImportWidthholdingFlowFromKuaiQianJob@导入代扣流水-快钱 开始.");
            long start = System.currentTimeMillis();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String settleDate = formatter.format(LocalDate.now().minusDays(1));
            //默认取前一天的数据
            LOGGER.info("@ImportWidthholdingFlowFromKuaiQianJob@导入代扣流水-快钱 正在处理[{}]的结清数据.", settleDate);
            withholdingFlowRecordService.importWidthholdingFlowFromKuaiQian(settleDate);
            long end = System.currentTimeMillis();
            LOGGER.info("@ImportWidthholdingFlowFromKuaiQianJob@导入代扣流水-快钱 结束.耗时:[{}]ms", end-start);
            return SUCCESS;
        } catch (Exception e) {
            LOGGER.error("@ImportWidthholdingFlowFromKuaiQianJob@导入代扣流水-快钱 异常.", e);
            return FAIL;
        }
    }
}
