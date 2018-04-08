package com.hongte.alms.core.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.entity.CollectionTrackLog;
import com.hongte.alms.base.collection.service.CollectionTrackLogService;
import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.base.collection.vo.CollectionTrckLogReq;
import com.hongte.alms.base.feignClient.service.EipOperateService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;
import com.ht.ussp.bean.LoginUserInfoHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 贷后跟踪记录表 前端控制器
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-01
 */
@RestController
@RequestMapping("/collectionTrackLog")
@Api(tags = "CollectionTrackLogController", description = "贷后跟踪记录相关接口")
public class CollectionTrackLogController {

    private Logger logger = LoggerFactory.getLogger(CollectionTrackLogController.class);

    @Autowired
    @Qualifier("CollectionTrackLogService")
    CollectionTrackLogService collectionTrackLogService;

    @Autowired
    LoginUserInfoHelper loginUserInfoHelper;

    @Autowired
    @Qualifier("eipOperateServiceImpl")
    private EipOperateService eipOperateService;


    @ApiOperation(value = "获取分页贷后跟踪记录 分页")
    @GetMapping("/selectCollectionTrackLogPage")
    @ResponseBody
    public PageResult<List<CollectionTrackLogVo>> selectCollectionTrackLogPage(@ModelAttribute CollectionTrckLogReq req){

        try{
            Page<CollectionTrackLogVo> pages = collectionTrackLogService.selectCollectionTrackLogByRbpId(req);

            List<CollectionTrackLogVo> list = pages.getRecords();

            for(int i=1;i<list.size()+1;i++){
                list.get(i-1).setSortId(i);
            }

            return PageResult.success(pages.getRecords(),pages.getTotal());
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }

    }




    @ApiOperation(value = "根据ID查找")
    @GetMapping("/selectById")
    @ResponseBody
    public Result<CollectionTrackLog> selectById(@RequestParam("trackLogId") String trackLogId){

        try{
            CollectionTrackLog log =  collectionTrackLogService.selectById(trackLogId);
            if(log==null){

                return Result.error("500","查询失败");
            }else{
                return Result.success(log);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }

    }

    @ApiOperation(value = "根据ID删除历史记录")
    @DeleteMapping("/deleteLog")
    public Result<Integer> deleteLog(@RequestParam("id") Integer trackLogId){

        try{
            boolean ret =  collectionTrackLogService.deleteById(trackLogId);
            if(ret){
                return Result.success(1);
            }else{
                return Result.error("500","删除失败");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }

    }

    @ApiOperation(value = "新增/修改历史记录")
    @PostMapping("/addOrUpdateLog")
    public Result<Integer> addOrUpdateLog(@RequestBody CollectionTrackLog log){

        com.ht.ussp.core.Result result = new com.ht.ussp.core.Result();

        result = eipOperateService.addProjectTract(log);

        try{
            if(log.getTrackLogId() != null){
                CollectionTrackLog logOld =  collectionTrackLogService.selectById(log.getTrackLogId());
                logOld.setContent(log.getContent());
                logOld.setIsSend(log.getIsSend());
                logOld.setTrackStatusId(log.getTrackStatusId());
                logOld.setTrackStatusName(log.getTrackStatusName());
                log = logOld;
            }
            CollectionTrackLogVo.setDefaultVal(log,loginUserInfoHelper.getUserId());
//            vo.setDefaultVal();
//            CollectionTrackLog log = (CollectionTrackLog)vo;
//            vo.s;
            collectionTrackLogService.insertOrUpdate(log);
//            collectionTrackLogService.insertOrUpdateAllColumn(vo);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }

        if(!"0000".equals(result.getReturnCode())){
            return Result.error("400","推送平台未成功");
        }

        return Result.success(1);
    }

}

