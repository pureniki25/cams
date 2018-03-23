package com.hongte.alms.core.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysJobConfig;
import com.hongte.alms.base.service.SysJobConfigService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/21
 */

@RestController
@RequestMapping("/timerSet")
public class TimerSetController {

    private  static Logger logger = LoggerFactory.getLogger(TimerSetController.class);

    @Autowired
    @Qualifier("SysJobConfigService")
    private SysJobConfigService jobConfigService;

    @ApiOperation(value="获取定时器任务列表")
    @GetMapping("/getTimetSetList")
    @ResponseBody
    public PageResult<List<SysJobConfig>> getTimetSetList(@RequestParam("jobName") String jobName,
                                                          @RequestParam("page") int page,
                                                          @RequestParam("limit") int limit){
        Page<SysJobConfig> pages = new Page<>();

        Wrapper<SysJobConfig> wrapper = new EntityWrapper<>();
        if(StringUtils.isNotBlank(jobName)){
            wrapper.eq("job_name",jobName);
        }
        pages.setCurrent(page);
        pages.setSize(limit);

        pages = jobConfigService.selectPage(pages,wrapper);

        return PageResult.success(pages.getRecords(),pages.getTotal());
    }

    @ApiOperation(value="获取定时器任务列表")
    @PostMapping("/saveTimetSetList")
    @ResponseBody
    public Result saveTimerSet(@RequestBody SysJobConfig sysJobConfig){

        System.out.println(sysJobConfig);
        try{
            SysJobConfig sys = jobConfigService.selectOne(new EntityWrapper<SysJobConfig>().eq("id",sysJobConfig.getId()));
            if(sys != null){
                jobConfigService.updateById(sysJobConfig);
            }else {
                jobConfigService.insert(sysJobConfig);
            }


        }catch (Exception e){
            logger.error(e.getMessage());
            return Result.error("500",e.getMessage());
        }

        return Result.success();
    }

    @ApiOperation(value="删除定时器")
    @GetMapping("/deleteTimer")
    @ResponseBody
    public Result deleteTimer(@RequestParam("id") String id){
        try{
            jobConfigService.delete(new EntityWrapper<SysJobConfig>().eq("id",id));
        }catch (Exception e){
            logger.error(e.getMessage());
            return Result.error("500",e.getMessage());
        }
        return Result.success();
    }

}
