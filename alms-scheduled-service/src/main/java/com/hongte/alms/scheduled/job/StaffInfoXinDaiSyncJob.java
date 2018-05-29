package com.hongte.alms.scheduled.job;

import com.hongte.alms.base.entity.SysJobConfig;
import com.hongte.alms.base.enums.JobConfigEnums;
import com.hongte.alms.base.service.SysJobConfigService;
import com.hongte.alms.scheduled.client.TransLogCleint;
import com.hongte.alms.scheduled.service.IXDSyncDataService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时同步信贷电催/催收信息的Job
 * 曾坤  2018-5-21
 */
@Component
public class StaffInfoXinDaiSyncJob  {
    private static Logger _log = LoggerFactory.getLogger(StaffInfoXinDaiSyncJob.class);

    @Autowired
    TransLogCleint transLogCleint;


    @Autowired
    @Qualifier("SysJobConfigService")
    private SysJobConfigService sysJobConfigService;

    @Scheduled(cron = "0 0 4 * * ?")
//    @Scheduled(cron = "*/5 * * * * ?")
    public void TransferCollectionTransfer() throws JobExecutionException {

        SysJobConfig config = null;
        try {
            //判断定时任务是否可执行
            config =  sysJobConfigService.getCanExecuteConfig(JobConfigEnums.JobConfigType.TRANS_COL_TRACK_LOG.getValue());
            if(config ==null)
                return;
            try {
                transLogCleint.transferCollectionTransfer();
                _log.info("完成一次历史贷后跟踪记录同步");
            }catch (Exception e){
                e.printStackTrace();
                _log.error("历史贷后跟踪记录同步 异常："+e.getMessage());
            }
            sysJobConfigService.updateById(config);
        }catch (Exception e){
            e.printStackTrace();
            _log.error("历史贷后跟踪记录同步 异常："+e.getMessage());
        }
        _log.info("贷后跟踪记录同步完毕: " + new Date());
    }


    @Scheduled(cron = "0 0 4 * * ?")
//    @Scheduled(cron = "*/5 * * * * ?")
    public void execute() throws JobExecutionException {

        SysJobConfig config = null;
        try {
            //判断定时任务是否可执行
            config =  sysJobConfigService.getCanExecuteConfig(JobConfigEnums.JobConfigType.TRANS_STAFF_SET.getValue());
            if(config ==null)
                return;
            try {
                transLogCleint.transferCollection();
                _log.info("完成一次历史催收人员设置同步");
            }catch (Exception e){
                e.printStackTrace();
                _log.error("历史催收人员设置同步 异常："+e.getMessage());
            }
            sysJobConfigService.updateById(config);
        }catch (Exception e){
            e.printStackTrace();
            _log.error("历史催收人员设置同步 异常："+e.getMessage());
        }
        _log.info("历史催收人员设置同步完毕: " + new Date());
    }

}
