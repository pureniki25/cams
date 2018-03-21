package com.hongte.alms.core.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.entity.CollectionTimeSet;
import com.hongte.alms.base.entity.SysJobConfig;
import com.hongte.alms.base.service.SysJobConfigService;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingReq;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingVo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
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
    @PostMapping("/getTimetSetList")
    @ResponseBody
    public PageResult<List<SysJobConfig>> getTimetSetList( ){
        Page<SysJobConfig> page = new Page<>();

        page = jobConfigService.selectPage(page);

        return PageResult.success(page.getRecords(),page.getTotal());
    }

}
