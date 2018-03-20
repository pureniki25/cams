package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.UserPermissionBusinessDto;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.SysRoleAreaTypeEnums;
import com.hongte.alms.base.mapper.SysUserPermissionMapper;
import com.hongte.alms.base.service.*;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户业务权限表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-05
 */
@Service("SysUserPermissionService")
public class SysUserPermissionServiceImpl extends BaseServiceImpl<SysUserPermissionMapper, SysUserPermission> implements SysUserPermissionService {

    @Autowired
            @Qualifier("SysRoleService")
    SysRoleService sysRoleService;

    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;

    @Autowired
    @Qualifier("SysUserAreaService")
    SysUserAreaService sysUserAreaService;

    @Autowired
    @Qualifier("SysUserService")
    SysUserService sysUserService;

    @Autowired
    @Qualifier("SysOrgService")
    SysOrgService sysOrgService;

    @Autowired
    @Qualifier("SysUserPermissionService")
    SysUserPermissionService sysUserPermissionService;

    @Autowired
    @Qualifier("SysUserRoleService")
    SysUserRoleService sysUserRoleService;

    @Autowired
    @Qualifier("BasicCompanyService")
    BasicCompanyService basicCompanyService;

    /**
     *根据用户ID设置用户可访问的区域信息
     * @param userId
     */
    @Transient
    public void setUserPermissons(String userId){

//        List<SysUserRole> userRoles = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("user_id",userId));
//
//        List<String> roleCodes = new LinkedList<String>();
//
//        for( SysUserRole uRole: userRoles){
//            roleCodes.add(uRole.getRoleCode());
//        }
//
//        List<SysRole> roles = sysRoleService.selectList(new EntityWrapper<SysRole>().in("role_code",roleCodes));

        //查出用户权限列表
        List<SysRole> roles = sysRoleService.getUserRoles(userId);

        //确认用户拥有的权限访问数据的区域类型
        SysRoleAreaTypeEnums  userAreaTypeEnums = SysRoleAreaTypeEnums.AREA;
        for(SysRole role: roles){
            if(role.getRoleAreaType().equals(SysRoleAreaTypeEnums.OVERALL.getKey())){
                userAreaTypeEnums = SysRoleAreaTypeEnums.OVERALL;
                break;
            }
        }


            //根据统一用户平台的树来找
//        Map<String,SysOrg> companyIds = sysUserService.selectCompanyByUserId(userId);
        //根据信贷的树来找
       Map<String,BasicCompany> companyIds  =  basicCompanyService.selectUserCanSeeCompany(userId);
        List<String>  businessIds = basicBusinessService.selectCompanysBusinessIds(new LinkedList<>(companyIds.keySet()));


        //删除原来用户的可看业务信息
        sysUserPermissionService.delete(new EntityWrapper<SysUserPermission>().eq("user_id",userId));

        List<SysUserPermission> permissions = new LinkedList<>();
        if(businessIds!=null&& businessIds.size()>0){
            for(String businessId:businessIds){
                SysUserPermission permission = new SysUserPermission();
                permission.setBusinessId(businessId);
                permission.setUserId(userId);
                permissions.add(permission);
            }
            //新增对应关系
            sysUserPermissionService.insertBatch(permissions);
        }




        //1.查出用户可看公司列表

    }

}
