package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysRole;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.entity.SysUserRole;
import com.hongte.alms.base.service.LoginService;
import com.hongte.alms.base.service.SysRoleService;
import com.hongte.alms.base.service.SysUserRoleService;
import com.hongte.alms.base.service.SysUserService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.BoaInRoleInfoDto;
import com.ht.ussp.client.dto.LoginInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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


    @Override
    public void saveloginInfo() {
        //用户信息
        LoginInfoDto dto = loginUserInfoHelper.getLoginInfo();
        if(dto == null){
            return;
        }
        SysUser sysUser = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("user_id",dto.getUserId()));
        if(sysUser == null){
            System.out.println(dto.getOrgCode()+":"+dto.getUserId()+":"+dto.getUserName());
            sysUser = new SysUser();
            sysUser.setUserId(dto.getUserId());
            sysUser.setOrgCode(dto.getOrgCode());
            sysUser.setUserName(dto.getUserName());
            sysUserService.insert(sysUser);
        }


        //角色信息
        List<BoaInRoleInfoDto> boaInRoleInfoDtoList = loginUserInfoHelper.getUserRole();
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        List<SysRole> sysRoleList = new ArrayList<>();
        if(boaInRoleInfoDtoList != null && boaInRoleInfoDtoList.size()>0){
            for (BoaInRoleInfoDto boa:boaInRoleInfoDtoList) {

                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleCode(boa.getRoleCode());
                sysUserRole.setUserId(dto.getUserId());
                sysUserRoleList.add(sysUserRole);

                SysRole role = new SysRole();
                role.setRoleName(boa.getRoleNameCn());
                role.setRoleCode(boa.getRoleCode());
                role.setRoleAreaType(1);
                if(sysRoleService.selectOne(new EntityWrapper<SysRole>().eq("role_code",boa.getRoleCode()).eq("role_name",boa.getRoleNameCn())) == null){
                    sysRoleList.add(role);
                }

                sysUserRoleService.delete(new EntityWrapper<SysUserRole>().eq("user_id",dto.getUserId()));
            }
            sysUserRoleService.insertBatch(sysUserRoleList);
            sysRoleService.insertBatch(sysRoleList);


        }

    }
}
