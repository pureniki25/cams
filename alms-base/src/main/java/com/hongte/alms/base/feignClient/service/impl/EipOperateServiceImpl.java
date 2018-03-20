package com.hongte.alms.base.feignClient.service.impl;

import com.hongte.alms.base.collection.entity.CollectionTrackLog;
import com.hongte.alms.base.collection.service.CollectionTrackLogService;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.feignClient.dto.AddProjectTrackReqDto;
import com.hongte.alms.base.feignClient.dto.ProjectTrackList;
import com.hongte.alms.base.feignClient.service.EipOperateService;
import com.hongte.alms.common.util.DateUtil;
import com.ht.ussp.core.Result;
import com.ht.ussp.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/13
 */
@Service
public class EipOperateServiceImpl implements EipOperateService {

    private Logger logger = LoggerFactory.getLogger(EipOperateServiceImpl.class);

    @Autowired
    @Qualifier("CollectionTrackLogService")
    CollectionTrackLogService collectionTrackLogService;

    @Autowired
    private EipRemote eipRemote;

    @Override
    public Result addProjectTract(CollectionTrackLog log) {

        List<String> projectIds = collectionTrackLogService.selectProjectIdByRbpId(log.getRbpId());

        List<String> successProjectIds = new ArrayList<>();
        List<String> failProjectIds = new ArrayList<>();

        Result result = new Result();

        if(log.getIsSend() == 1){
            for (String projectId : projectIds) {

                ProjectTrackList projectTrackList = new ProjectTrackList();
                projectTrackList.setContent(log.getContent());
                projectTrackList.setAddDate(DateUtil.formatDate("yyyy-MM-dd HH:mm:ss",new Date()));

                List<ProjectTrackList> list = new ArrayList<>();
                list.add(projectTrackList);

                AddProjectTrackReqDto dto = new AddProjectTrackReqDto();
                dto.setProjectId(projectId);
                dto.setProjectTrackList(list);

                try{
                    result =  eipRemote.addProjectTrack(dto);
                }catch (Exception e){
                    logger.error(e.getMessage());
                }

                if("0000".equals(result.getReturnCode())){
                    successProjectIds.add(projectId);
                }else {
                    failProjectIds.add(projectId);
                }
            }

            log.setFailProjectId(failProjectIds.size()>0?JsonUtil.obj2Str(failProjectIds):null);

            log.setSuccessProjectId(successProjectIds.size()>0?JsonUtil.obj2Str(successProjectIds):null);

            if(successProjectIds.size()>0 && failProjectIds.size()>0){
                log.setSendStatus(2);//都不为空  部分失败
                result = Result.buildFail();
            }else if(failProjectIds.size()>0){
                log.setSendStatus(0);// 一个不为空  全部失败
                result = Result.buildFail();
            }else {
                log.setSendStatus(1);// 成功
                result = Result.buildSuccess();
            }
        }else {
            result = Result.buildSuccess();
        }

        return result;
    }

    /**
     * 定时任务  重新发送失败的推送消息
     * @param trackLogList
     * @return
     */
    @Override
    public Result reAddProjectTract(List<CollectionTrackLog> trackLogList) {

        Result result = Result.buildSuccess();

        for (CollectionTrackLog log:trackLogList) {
            List<String> failProjectIds = Arrays.asList(log.getFailProjectId());
            for (String projectId:failProjectIds) {
                AddProjectTrackReqDto dto = new AddProjectTrackReqDto();
                ProjectTrackList projectTrackList = new ProjectTrackList();
                projectTrackList.setAddDate(DateUtil.formatDate("",log.getCreateTime()));
                projectTrackList.setContent(log.getContent());
                List<ProjectTrackList> lists = new ArrayList<>();
                lists.add(projectTrackList);
                dto.setProjectId(projectId);
                dto.setProjectTrackList(lists);
                try{
                    result =  eipRemote.addProjectTrack(dto);
                }catch (Exception e){
                    logger.error(e.getMessage());
                }
            }
            log.setIsSend(1);
        }

        collectionTrackLogService.updateBatchById(trackLogList);

        return result;
    }

}
