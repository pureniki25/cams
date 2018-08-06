package com.hongte.alms.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.DepartmentBank;
import com.hongte.alms.base.service.DepartmentBankService;
import com.hongte.alms.base.vo.finance.DepartmentBankVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;
import com.ht.ussp.bean.LoginUserInfoHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 还款账号管理 Created by 张贵宏 on 2018/7/6 11:16
 */
@RestController
@RequestMapping("/departmentBank")
@Api(description = "还款账号管理")
public class DepartmentBankController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentBankController.class);

    @Autowired
    private LoginUserInfoHelper loginUserInfoHelper;

    @Autowired
    @Qualifier("DepartmentBankService")
    private DepartmentBankService departmentBankService;

    @ApiOperation(value = "获取所有银行账号")
    @RequestMapping("/findAll")
    public Result findAll() {
        LOGGER.info("@findAll@查看所有银行账号--开始[]");
        Result result = null;
        List<DepartmentBank> res = departmentBankService.listDepartmentBank();
        result = Result.success(res);
        LOGGER.info("@findAll@查看所有银行账号--结束[{}]", result);
        return result;
    }

    @ApiOperation(value = "搜索查询银行账号")
    @RequestMapping("/search")
    public PageResult search(@RequestBody Page<DepartmentBank> page) {
        // Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,
        // "search_");
        // page = new Page<>(layTableQuery.getPage(), layTableQuery.getLimit());
        // departmentBankService.selectPage()
        page.setOrderByField("updateTime").setAsc(false);
        departmentBankService.selectByPage(page);
        return PageResult.success(page.getRecords(), page.getTotal());
    }

    @ApiOperation(value = "编辑/新增")
    @RequestMapping("/edit")
    public Result edit(@RequestBody DepartmentBankVO departmentBankVO) {
        try {
            if (StringUtils.isBlank(departmentBankVO.getAccountId())) {
                // 新增
                /*
                 * DepartmentBank departmentBank = new DepartmentBank();
                 * BeanUtil.copyProperties(departmentBankVO, departmentBank); if
                 * (departmentBankVO.getDeptIds() != null &&
                 * departmentBankVO.getDeptIds().size() > 0) {
                 * departmentBank.setDeptId(StringUtils.join(departmentBankVO.getDeptIds(),","))
                 * ; } departmentBank.setCreateUser(loginUserInfoHelper.getUserId());
                 * departmentBank.setCreateTime(new Date()); departmentBank.insert();
                 */

                // 新增
                if (departmentBankVO.getDeptIds() != null && departmentBankVO.getDeptIds().size() > 0) {
                    departmentBankVO.getDeptIds().forEach(deptId -> {
                        DepartmentBank departmentBank = new DepartmentBank();
                        BeanUtil.copyProperties(departmentBankVO, departmentBank);
                        departmentBank.setDeptId(deptId);
                        departmentBank.setCreateUser(loginUserInfoHelper.getUserId());
                        departmentBank.setCreateTime(new Date());
                        departmentBank.insert();
                    });

                }

            } else {
                // 更新
                /*
                 * DepartmentBank departmentBank =
                 * departmentBankService.selectById(departmentBankVO.getAccountId());
                 * 
                 * BeanUtil.copyProperties(departmentBankVO, departmentBank, new CopyOptions()
                 * {{ setIgnoreNullValue(true); }}); if (departmentBankVO.getDeptIds() != null
                 * && departmentBankVO.getDeptIds().size() > 0) {
                 * departmentBank.setDeptId(StringUtils.join(departmentBankVO.getDeptIds(),","))
                 * ; } departmentBank.setUpdateUser(loginUserInfoHelper.getUserId());
                 * departmentBank.setUpdateTime(new Date()); departmentBank.updateById();
                 */

                DepartmentBank oldDeptBank = departmentBankService.selectById(departmentBankVO.getAccountId());
                if (departmentBankVO.getDeptIds() != null && departmentBankVO.getDeptIds().size() > 0) {
                    //如果删除了原来的并新选择多个，则更新第一个新增第二个
                    boolean delOld = true;
                    for (String deptId : departmentBankVO.getDeptIds()) {
                        if(deptId.equals(oldDeptBank.getDeptId())){
                            delOld = false;
                            break;
                        }
                    }
                    for (String deptId : departmentBankVO.getDeptIds()) {
                        if (deptId.equals(oldDeptBank.getDeptId()) || departmentBankVO.getDeptIds().size() == 1) {
                            // 更新本次
                            BeanUtil.copyProperties(departmentBankVO, oldDeptBank, new CopyOptions() {
                                {
                                    setIgnoreNullValue(true);
                                }
                            });
                            oldDeptBank.setDeptId(deptId);
                            oldDeptBank.setUpdateUser(loginUserInfoHelper.getUserId());
                            oldDeptBank.setUpdateTime(new Date());
                            oldDeptBank.updateById();
                        } else {
                            // 新增另外的
                            DepartmentBank newDeptBank = new DepartmentBank();
                            BeanUtil.copyProperties(oldDeptBank, newDeptBank);
                            newDeptBank.setAccountId(null);
                            newDeptBank.setDeptId(deptId);
                            newDeptBank.setCreateUser(loginUserInfoHelper.getUserId());
                            newDeptBank.setCreateTime(new Date());
                            newDeptBank.insert();
                        }
                    }
                    if(delOld  && departmentBankVO.getDeptIds().size()>1 ){
                        oldDeptBank.deleteById();

                    }
                }
            }
            return Result.success();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Result.error(ex.getMessage());
        }

    }

    @ApiOperation(value = "删除")
    @RequestMapping("/delete")
    public Result delete(String id) {
        try {
            departmentBankService.deleteById(id);
            return Result.success();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

}
