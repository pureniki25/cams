package com.hongte.alms.core.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.entity.CollectionLog;
import com.hongte.alms.base.collection.entity.CollectionStatus;
import com.hongte.alms.base.collection.service.CollectionLogService;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.Collection;
import com.hongte.alms.base.entity.CollectionLogXd;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.TransferFailLog;
import com.hongte.alms.base.service.CollectionLogXdService;
import com.hongte.alms.base.service.CollectionService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.TransferFailLogService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author:喻尊龙
 * @date: 2018/3/29
 */
@RestController
@RequestMapping("/alms")
public class CollectionTransferController {

    private Logger logger = LoggerFactory.getLogger(CollectionTransferController.class);

    @Autowired
    @Qualifier("CollectionService")
    private CollectionService collectionService;

    @Autowired
    @Qualifier("CollectionLogXdService")
    private CollectionLogXdService collectionLogXdService;

    @Autowired
    private LoginUserInfoHelper loginUserInfoHelper;

    @Autowired
    @Qualifier("CollectionStatusService")
    private CollectionStatusService collectionStatusService;

    @Autowired
    @Qualifier("CollectionLogService")
    private CollectionLogService collectionLogService;

    @Autowired
    @Qualifier("TransferFailLogService")
    private TransferFailLogService transferFailLogService;



    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    private RepaymentBizPlanListService repaymentBizPlanListService;

    @GetMapping("/transfer")
    @ResponseBody
    public Result transferCollection(){

        List<Collection> collectionList = new ArrayList<>();
        List<CollectionLogXd> xdList = new ArrayList<>();

        int count = collectionService.selectCount(null);
        for(int i = 1;i <= count/100+1;i++){

            List<CollectionStatus> statusList = new ArrayList<>();
            List<CollectionLog> logList = new ArrayList<>();

            Page<Collection> pages = new Page<>();
            pages.setCurrent(i);
            pages.setSize(100);

            pages = collectionService.selectPage(pages);
            collectionList = pages.getRecords();
            for (Collection collection:collectionList) {
                Map<String,Object> map = getStatus(collection);
                if(map == null){
                    continue;
                }
                CollectionStatus collectionStatus = (CollectionStatus)map.get("status");
                CollectionLog collectionLog = (CollectionLog)map.get("log");

                statusList.add(collectionStatus);
                logList.add(collectionLog);

                String businessId = collection.getBusinessId();
                String afterId = collection.getAfterId();
                CollectionLogXd collectionLogXd = collectionLogXdService.selectOne(new EntityWrapper<CollectionLogXd>().eq("business_id",businessId).eq("after_id",afterId));
                if(collectionLogXd != null){
                    CollectionLog collectionLog1 = new CollectionLog();
                    collectionLog1.setBusinessId(collection.getBusinessId());
                    collectionLog1.setCrpId(collectionLog.getCrpId());
                    collectionLog1.setAfterStatus(1);
                    LoginInfoDto dto =loginUserInfoHelper.getUserInfoByUserId("",collectionLogXd.getCollectionUser());
                    if(dto == null){
                        TransferFailLog failLog = new TransferFailLog();
                        failLog.setBusinessId(collection.getBusinessId());
                        failLog.setAfterId(collection.getAfterId());
                        failLog.setFailReason(2);

                            transferFailLogService.insert(failLog);


                        continue;
                    }
                    collectionLog1.setCollectionUser(collectionLogXd.getCollectionUser());
                    collectionLog1.setCreateTime(new Date());
                    collectionLog1.setCreateUser("admin");
                    collectionLog1.setUpdateTime(new Date());
                    collectionLog1.setUpdateUser("admin");
                    collectionLog1.setDescribe("信贷历史数据导入");
                    collectionLog1.setSetWay(0);
                    collectionLog1.setBeforeStatus(null);
                    collectionLog1.setSetTypeStatus(1);
                    logList.add(collectionLog1);

                }
            }
            if(statusList.size()>0){
                collectionStatusService.insertOrUpdateBatch(statusList);
            }
            if(logList.size()>0){
                collectionLogService.insertOrUpdateBatch(logList);
            }


        }

        count = collectionLogXdService.selectCount(null);
        for(int i=1;i<=count/100+1;i++){

            List<CollectionStatus> statusList = new ArrayList<>();
            List<CollectionLog> logList = new ArrayList<>();

            Page<CollectionLogXd> pages = new Page<>();
            pages.setCurrent(i);
            pages.setSize(100);
            pages = collectionLogXdService.selectPage(pages,new EntityWrapper<CollectionLogXd>().and("business_id not in (SELECT business_id from tb_collection)"));

            xdList = pages.getRecords();
            for (CollectionLogXd collectionLogXd:xdList) {
                Map<String,Object> map = getStatus(collectionLogXd);
                if(map == null){
                    continue;
                }
                CollectionStatus collectionStatus = (CollectionStatus)map.get("status");
                CollectionLog collectionLog = (CollectionLog)map.get("log");
                statusList.add(collectionStatus);
                logList.add(collectionLog);
            }
            if(statusList.size()>0){
                collectionStatusService.insertOrUpdateBatch(statusList);
            }
            if(logList.size()>0){
                collectionLogService.insertOrUpdateBatch(logList);
            }

        }

        return Result.success();
    }

    private Map<String,Object> getStatus(Collection collection){

        Map<String,Object> map = new HashMap<>();

        RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id",collection.getBusinessId()).eq("after_id",collection.getAfterId()));
        if(repaymentBizPlanList == null){
            TransferFailLog failLog = new TransferFailLog();
            failLog.setBusinessId(collection.getBusinessId());
            failLog.setAfterId(collection.getAfterId());
            failLog.setFailReason(1);
                transferFailLogService.insert(failLog);

            return null;
        }

        //根据信贷userId 获取贷后userId
        LoginInfoDto dto =loginUserInfoHelper.getUserInfoByUserId("",collection.getCollectionUser());
        if(dto == null){
            dto = new LoginInfoDto();
        }
        CollectionStatus status = new CollectionStatus();
        CollectionLog log = new CollectionLog();

        status.setBusinessId(collection.getBusinessId());
        status.setCrpId(repaymentBizPlanList.getPlanListId());
        //判断状态
        int collectionStatus = 0;
        if("催收中".equals(collection.getStatus())){
            collectionStatus = 50;
            status.setVisitStaff(dto.getUserId());
            log.setCollectionUser(dto.getUserId());
            if(StringUtil.isEmpty(dto.getUserId())){
                TransferFailLog failLog = new TransferFailLog();
                failLog.setBusinessId(collection.getBusinessId());
                failLog.setAfterId(collection.getAfterId());
                failLog.setFailReason(2);
                    transferFailLogService.insert(failLog);

                return null;
            }
        }else if("已移交法务".equals(collection.getStatus())){
            log.setCollectionUser("admin");
            collectionStatus = 100;
        }else if("已完成".equals(collection.getStatus())){
            collectionStatus = 200;
            log.setCollectionUser("admin");
        }else if("二押已赎回".equals(collection.getStatus())) {
            log.setCollectionUser("admin");
            collectionStatus = 250;
        }else if("已委外催收".equals(collection.getStatus())){
            log.setCollectionUser("admin");
            collectionStatus = 300;
        }else if("待分配".equals(collection.getStatus())){
            log.setCollectionUser("admin");
            collectionStatus = 350;
        }else if("推迟移交法务".equals(collection.getStatus())){
            log.setCollectionUser("admin");
            collectionStatus = 400;
        }else {
            log.setCollectionUser("admin");
        }
        status.setCollectionStatus(collectionStatus);
        status.setCreateTime(new Date());
        status.setCreateUser("admin");
        status.setUpdateTime(new Date());
        status.setUpdateUser("admin");
        status.setDescribe("信贷历史数据导入");
        status.setSetWay(0);
        status.setCrpType(ifPlanListIsLast(collection.getBusinessId(),repaymentBizPlanList.getPlanListId())?2:1);


        log.setBusinessId(collection.getBusinessId());
        log.setCrpId(repaymentBizPlanList.getPlanListId());
        log.setAfterStatus(collectionStatus);
        log.setCreateTime(new Date());
        log.setCreateUser("admin");
        log.setUpdateTime(new Date());
        log.setUpdateUser("admin");
        log.setDescribe("信贷历史数据导入");
        log.setSetWay(0);
        log.setBeforeStatus(null);
        log.setSetTypeStatus(collectionStatus);

        map.put("status",status);
        map.put("log",log);

        return  map;
    }

    /**
     * 判断还款计划是否是最后一个
     * @param
     * @return
     */
    public boolean ifPlanListIsLast( String businessId,String planListId){
        RepaymentBizPlanList pList =  repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id",businessId).orderBy("due_date desc"));
        if(pList.getPlanListId().equals(planListId)){
            return true;
        }
        return false;
    }

    private Map<String,Object> getStatus(CollectionLogXd collectionLogXd){

        Map<String,Object> map = new HashMap<>();

        RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id",collectionLogXd.getBusinessId()).eq("after_id",collectionLogXd.getAfterId()));

        if(repaymentBizPlanList == null){
            if(repaymentBizPlanList == null){
                TransferFailLog failLog = new TransferFailLog();
                failLog.setBusinessId(collectionLogXd.getBusinessId());
                failLog.setAfterId(collectionLogXd.getAfterId());
                failLog.setFailReason(1);
                    transferFailLogService.insert(failLog);

                return null;
            }
        }

        if(repaymentBizPlanList == null){
            repaymentBizPlanList = new RepaymentBizPlanList();
        }

        //根据信贷userId 获取贷后userId
        LoginInfoDto dto =loginUserInfoHelper.getUserInfoByUserId("",collectionLogXd.getCollectionUser());
        if(dto == null){
            dto = new LoginInfoDto();
        }
        CollectionStatus status = new CollectionStatus();
        CollectionLog log = new CollectionLog();

        status.setBusinessId(collectionLogXd.getBusinessId());
        status.setCrpId(repaymentBizPlanList.getPlanListId());

        status.setCollectionStatus(1);
        if(StringUtil.isEmpty(dto.getUserId())){
            TransferFailLog failLog = new TransferFailLog();
            failLog.setBusinessId(collectionLogXd.getBusinessId());
            failLog.setAfterId(collectionLogXd.getAfterId());
            failLog.setFailReason(2);
                transferFailLogService.insert(failLog);

            return null;
        }
        status.setPhoneStaff(dto.getUserId());
        status.setCreateTime(new Date());
        status.setCreateUser("admin");
        status.setUpdateTime(new Date());
        status.setUpdateUser("admin");
        status.setDescribe("信贷历史数据导入");
        status.setSetWay(0);
        status.setCrpType(ifPlanListIsLast(collectionLogXd.getBusinessId(),repaymentBizPlanList.getPlanListId())?2:1);


        log.setBusinessId(collectionLogXd.getBusinessId());
        log.setCrpId(repaymentBizPlanList.getPlanListId());
        log.setAfterStatus(1);
        log.setCollectionUser(dto.getUserId());
        log.setCreateTime(new Date());
        log.setCreateUser("admin");
        log.setUpdateTime(new Date());
        log.setUpdateUser("admin");
        log.setDescribe("信贷历史数据导入");
        log.setSetWay(0);
        log.setBeforeStatus(null);
        log.setSetTypeStatus(1);

        map.put("status",status);
        map.put("log",log);

        return  map;

    }

}
