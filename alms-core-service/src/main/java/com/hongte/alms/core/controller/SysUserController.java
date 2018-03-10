package com.hongte.alms.core.controller;

import com.hongte.alms.base.service.SysUserPermissionService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zengkun
 * @since 2018/3/8
 */
@RestController
@RequestMapping("/SysUser")
public class SysUserController {

   private  static     Logger logger = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    @Qualifier("SysUserService")
    SysUserService sysUserService;

    @Autowired
    @Qualifier("SysUserPermissionService")
    SysUserPermissionService sysUserPermissionService;

    @GetMapping("/setUserPermissions")
    @ApiOperation(value = "设置指定用户可访问的业务对应关系")
    public Result setUserPermissions(
            @Param("userId") String userId){

        try{
            sysUserPermissionService.setUserPermissons(userId);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return Result.success("设置用户可访问订单列表失败："+e.getMessage());
        }

        return Result.success();
    }

}
