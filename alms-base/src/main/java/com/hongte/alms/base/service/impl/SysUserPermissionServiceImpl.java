package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.SysRole;
import com.hongte.alms.base.entity.SysUserPermission;
import com.hongte.alms.base.enums.RoleAreaMethodEnum;
import com.hongte.alms.base.enums.SysRoleAreaTypeEnums;
import com.hongte.alms.base.mapper.SysUserPermissionMapper;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.vo.finance.SysFinancialOrderVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.HashMap;
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

    @Autowired
    @Qualifier("CollectionStatusService")
    CollectionStatusService collectionStatusService;

    @Autowired
    @Qualifier("SysFinancialOrderService")
    SysFinancialOrderService sysFinancialOrderService;

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
        SysRoleAreaTypeEnums userAreaTypeEnums = SysRoleAreaTypeEnums.ONLY_SELF;

        RoleAreaMethodEnum roleAreaMethodEnum = RoleAreaMethodEnum.NULL_Area;
        for(SysRole role: roles){
            if(role.getRoleAreaType().equals(SysRoleAreaTypeEnums.OVERALL.getKey())){
                userAreaTypeEnums = SysRoleAreaTypeEnums.OVERALL;
//                break;
            }
            if(role.getRoleAreaType().equals(SysRoleAreaTypeEnums.AREA.getKey())){
                if(userAreaTypeEnums == SysRoleAreaTypeEnums.ONLY_SELF){
                    userAreaTypeEnums = SysRoleAreaTypeEnums.AREA;
                }
            }

            if (RoleAreaMethodEnum.FINANCIAL_ORDER.value().equals(role.getRoleAreaMethod())) {
                roleAreaMethodEnum = RoleAreaMethodEnum.FINANCIAL_ORDER;
            }
            roleAreaMethodEnum = RoleAreaMethodEnum.valueOf(role.getRoleAreaMethod());
        }

            //根据统一用户平台的树来找
//        Map<String,SysOrg> companyIds = sysUserService.selectCompanyByUserId(userId);
        //根据信贷的树来找
       Map<String,BasicCompany> companyIds  =  basicCompanyService.selectUserCanSeeCompany(userId);
        List<String>  businessIds = basicBusinessService.selectCompanysBusinessIds(new LinkedList<>(companyIds.keySet()));

        //角色权限是否为区域下的财务跟单配置权限 zgh
        if (SysRoleAreaTypeEnums.AREA.equals(userAreaTypeEnums) && RoleAreaMethodEnum.FINANCIAL_ORDER.equals(roleAreaMethodEnum)) {
            //如果有设置按跟单方式处理，则清除原区域性的权限
            businessIds.clear();

            //查找本用户id在财务跟单设置中配置的可访问的business zgh
            //从财务跟单配置中查询本用户可访问的分公司id及业务类型
            Page<SysFinancialOrderVO> financialOrderVOPage = sysFinancialOrderService.search(new Page<>(1, Integer.MAX_VALUE), null, null, null, userId);
            if (financialOrderVOPage != null && financialOrderVOPage.getRecords() != null && financialOrderVOPage.getRecords().size() > 0) {
                for (SysFinancialOrderVO fo : financialOrderVOPage.getRecords()) {
                    List<String> tempBusinessIds = basicBusinessService.findBusinessIds(fo.getCompanyId(), fo.getBusinessTypeId());
                    businessIds.addAll(tempBusinessIds);
                }
            }
        }

        //查找用户跟进的业务ID
        List<String> followBids =  collectionStatusService.selectFollowBusinessIds(userId);
        businessIds.addAll(followBids);


        //删除原来用户的可看业务信息
        sysUserPermissionService.delete(new EntityWrapper<SysUserPermission>().eq("user_id",userId));

        List<SysUserPermission> permissions = new LinkedList<>();
        Map<String,String> tempMap = new HashMap<>();
        if(businessIds!=null&& businessIds.size()>0){
            for(String businessId:businessIds){
                if(tempMap.get(businessId)==null){
                    tempMap.put(businessId,userId);
                    SysUserPermission permission = new SysUserPermission();
                    permission.setBusinessId(businessId);
                    permission.setUserId(userId);
                    permissions.add(permission);
                }
            }
        }
        //新增对应关系
        if(permissions.size()>0){
            sysUserPermissionService.insertBatch(permissions);
        }
    }

}
