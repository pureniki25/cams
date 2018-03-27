package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysRole;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.entity.SysUserRole;
import com.hongte.alms.base.service.*;
import com.hongte.alms.common.util.Constant;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.BoaInRoleInfoDto;
import com.ht.ussp.client.dto.LoginInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/19
 */
@Service("LoginService")
public class LoginServiceImpl implements LoginService {

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

    @Autowired
    @Qualifier("SysUserPermissionService")
    private SysUserPermissionService sysUserPermissionService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveloginInfo() {
        //用户信息
        LoginInfoDto dto = loginUserInfoHelper.getLoginInfo();
        if(dto == null){
            return;
        }
        if(dto.getUserId().equals(Constant.ADMIN_ID)) {
            sysUserPermissionService.setUserPermissons(dto.getUserId());
            return;
        }
    
        boolean fresh = false;//判断是否刷新任务设置
        SysUser sysUser = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("user_id",dto.getUserId()));
        if(sysUser == null){
            System.out.println(dto.getOrgCode()+":"+dto.getUserId()+":"+dto.getUserName());
            sysUser = new SysUser();
            sysUser.setUserId(dto.getUserId());
            sysUser.setOrgCode(dto.getOrgCode());
            sysUser.setUserName(dto.getUserName());
            sysUser.setXdOrgCode(dto.getBmOrgCode());
            sysUserService.insert(sysUser);
            sysUserPermissionService.setUserPermissons(dto.getUserId());
            fresh = true;
        }


        //角色信息
        List<BoaInRoleInfoDto> boaInRoleInfoDtoList = new ArrayList<>();
        boaInRoleInfoDtoList = loginUserInfoHelper.getUserRole();
        List<SysUserRole> userRoles =  sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("user_id",dto.getUserId()));

        List<String> newRole = new ArrayList<>();
        List<String> oldRole = new ArrayList<>();

        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        List<SysRole> sysRoleList = new ArrayList<>();
        if(boaInRoleInfoDtoList.size() == 0){
            sysUserRoleService.delete(new EntityWrapper<SysUserRole>().eq("user_id",dto.getUserId()));
        }
        for (BoaInRoleInfoDto boa:boaInRoleInfoDtoList) {
            newRole.add(boa.getRoleCode());
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleCode(boa.getRoleCode());
            sysUserRole.setUserId(dto.getUserId());
            sysUserRoleList.add(sysUserRole);

            SysRole role = new SysRole();
            role.setRoleName(boa.getRoleNameCn());
            role.setRoleCode(boa.getRoleCode());
            role.setRoleAreaType(1);
            if(sysRoleService.selectOne(new EntityWrapper<SysRole>().eq("role_code",boa.getRoleCode())) == null){
                sysRoleList.add(role);
            }

            sysUserRoleService.delete(new EntityWrapper<SysUserRole>().eq("user_id",dto.getUserId()));
        }
        if(sysUserRoleList.size()>0){
             sysUserRoleService.insertOrUpdateBatch(sysUserRoleList);
        }
        if(sysRoleList.size()>0){
            sysRoleService.insertOrUpdateBatch(sysRoleList);
        }

        for (SysUserRole role:userRoles) {
            oldRole.add(role.getRoleCode());
        }

        if(newRole.size() != oldRole.size() && !fresh){
            sysUserPermissionService.setUserPermissons(dto.getUserId());
            return;
        }
        for (String str:newRole) {
            if(!oldRole.contains(str)){
                sysUserPermissionService.setUserPermissons(dto.getUserId());
                return;
            }
        }

    }


}
