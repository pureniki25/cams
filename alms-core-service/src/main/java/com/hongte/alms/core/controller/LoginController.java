package com.hongte.alms.core.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysRole;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.entity.SysUserRole;
import com.hongte.alms.base.service.SysRoleService;
import com.hongte.alms.base.service.SysUserRoleService;
import com.hongte.alms.base.service.SysUserService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.BoaInRoleInfoDto;
import com.ht.ussp.client.dto.LoginInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/13
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginUserInfoHelper loginUserInfoHelper;

    @Autowired
    @Qualifier("SysUserService")
    private SysUserService sysUserService;

    @Autowired
    @Qualifier("SysRoleService")
    private SysRoleService sysRoleService;

    @Autowired
    @Qualifier("SysUserRoleService")
    private SysUserRoleService sysUserRoleService;

    @RequestMapping("/saveloginInfo")
    public void saveloginInfo(){

        //用户信息
        LoginInfoDto dto = loginUserInfoHelper.getLoginInfo();
        if(dto!=null){
            SysUser sysUser = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("user_id",dto.getUserId()));
            if(sysUser == null){
                System.out.println(dto.getOrgCode()+":"+dto.getUserId()+":"+dto.getUserName());
                sysUser = new SysUser();
                sysUser.setUserId(dto.getUserId());
                sysUser.setOrgCode(dto.getOrgCode());
                sysUser.setUserName(dto.getUserName());
                sysUserService.insert(sysUser);
            }
        }

        //角色信息
        List<BoaInRoleInfoDto> boaInRoleInfoDtoList = loginUserInfoHelper.getUserRole();
        List<SysRole> sysRoleList = new ArrayList<>();
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        if(boaInRoleInfoDtoList != null && boaInRoleInfoDtoList.size()>0){
            for (BoaInRoleInfoDto boa:boaInRoleInfoDtoList) {
                SysRole sysRole = new SysRole();
                sysRole.setRoleAreaType(1);
                sysRole.setRoleCode(boa.getRoleCode());
                sysRole.setRoleName(boa.getRoleNameCn());
                sysRoleList.add(sysRole);

                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleCode(boa.getRoleCode());
                sysUserRole.setUserId(dto.getUserId());
                sysUserRoleList.add(sysUserRole);
            }
            sysRoleService.insertOrUpdateBatch(sysRoleList);
            sysUserRoleService.insertOrUpdateBatch(sysUserRoleList);
        }

    }
}
