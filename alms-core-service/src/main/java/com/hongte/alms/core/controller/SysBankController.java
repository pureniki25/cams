package com.hongte.alms.core.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.hongte.alms.base.entity.SysBank;
import com.hongte.alms.base.service.SysBankService;
import com.hongte.alms.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * [银行信息表] 前端控制器
 * </p>
 *
 * @author 张贵宏
 * @since 2018-7-10
 */
@RestController
@RequestMapping("/sysBank")
@Api(description = "银行管理控制器")
public class SysBankController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysBankController.class);

    @Autowired
    @Qualifier("SysBankService")
    private SysBankService sysBankService;

    /**
     * 查询所有银行
     * @return
     */
    @ApiOperation(value = "查询所有银行")
    @RequestMapping("/findAll")
    public Result findAll() {
        List<SysBank> banks = Lists.newArrayList();
        try {
            banks =  sysBankService.selectList(new EntityWrapper<SysBank>().orderBy("bank_name",true));
            return Result.success(banks);
        }catch (Exception ex){
            LOGGER.error(ex.getMessage(), ex);
            return Result.error(ex.getMessage());
        }
    }

}

