package com.hongte.alms.base.service.impl;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.SysRole;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.entity.SysUserArea;
import com.hongte.alms.base.entity.SysUserPermission;
import com.hongte.alms.base.entity.SysUserRole;
import com.hongte.alms.base.enums.RoleAreaMethodEnum;
import com.hongte.alms.base.enums.SysRoleAreaTypeEnums;
import com.hongte.alms.base.feignClient.UcAppRemote;
import com.hongte.alms.base.mapper.SysUserPermissionMapper;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.SysFinancialOrderService;
import com.hongte.alms.base.service.SysOrgService;
import com.hongte.alms.base.service.SysRoleService;
import com.hongte.alms.base.service.SysUserAreaService;
import com.hongte.alms.base.service.SysUserPermissionService;
import com.hongte.alms.base.service.SysUserRoleService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.vo.finance.SysFinancialOrderVO;
import com.hongte.alms.base.vo.user.BoaInRoleInfoDto;
import com.hongte.alms.base.vo.user.SelfBoaInUserInfo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;

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
    
    @Autowired
    LoginUserInfoHelper loginUserInfoHelper;
    
	@Autowired
	private UcAppRemote ucAppRemote;
    
    /**
     * 根据用户ID设置用户可访问的区域信息
     * @param userId
     */
    @Transient
    public void setUserPermissons(String userId){
        //查出用户权限列表
        List<SysRole> roles = sysRoleService.getUserRoles(userId);

        //确认用户拥有的权限访问数据的区域类型
        SysRoleAreaTypeEnums userAreaTypeEnums = SysRoleAreaTypeEnums.ONLY_SELF;

        RoleAreaMethodEnum roleAreaMethodEnum = RoleAreaMethodEnum.NULL_Area;
        for(SysRole role: roles){
            if(role.getRoleAreaType().equals(SysRoleAreaTypeEnums.OVERALL.getKey())){
                userAreaTypeEnums = SysRoleAreaTypeEnums.OVERALL;
            }
            if(role.getRoleAreaType().equals(SysRoleAreaTypeEnums.AREA.getKey())){
                if(userAreaTypeEnums == SysRoleAreaTypeEnums.ONLY_SELF){
                    userAreaTypeEnums = SysRoleAreaTypeEnums.AREA;
                }
            }
            roleAreaMethodEnum = RoleAreaMethodEnum.valueOf(role.getRoleAreaMethod());
        }

        //根据统一用户平台的树来找
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
            if (financialOrderVOPage != null && financialOrderVOPage.getRecords() != null && !financialOrderVOPage.getRecords().isEmpty()) {
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
        if(businessIds!=null&& !businessIds.isEmpty()){
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
        if(!permissions.isEmpty()){
            sysUserPermissionService.insertBatch(permissions,10000);
        }
    }
    
    
	/**
	 * @Title: updateUserPermision  
	 * @Description: 更新用户信息
	 * @param @param userInfo    参数  
	 * @return void    返回类型  
	 * @throws  
	 */
    @Override
	public void updateUserPermision(SelfBoaInUserInfo userInfo) {
		boolean fresh = false;//判断是否刷新任务设置
		String userId = userInfo.getUserId();
        SysUser sysUser = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("user_id",userId));
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setUserId(userInfo.getUserId());
            sysUser.setOrgCode(userInfo.getOrgCode());
            sysUser.setUserName(userInfo.getUserName());
            sysUser.setXdOrgCode("");
            sysUserService.insert(sysUser);
            sysUserPermissionService.setUserPermissons(userInfo.getUserId());
            fresh = true;
        }else {
        	if(!userInfo.getUserName().equals(sysUser.getUserName())
        			|| !userInfo.getOrgCode().equals(sysUser.getOrgCode())) {
        		sysUser.setUserName(userInfo.getUserName());
        		sysUser.setOrgCode(userInfo.getOrgCode());
        		sysUserService.updateById(sysUser);
        	}
        }

        //角色信息
        List<BoaInRoleInfoDto> boaInRoleInfoDtoList = ucAppRemote.getUserRole(userId);
        if(null == boaInRoleInfoDtoList) {
        	boaInRoleInfoDtoList = new ArrayList<>();
        }
        List<SysUserRole> userRoles =  sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("user_id",userId));
        
        List<String> newRole = new ArrayList<>();
        List<String> oldRole = new ArrayList<>();

        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        List<SysRole> sysRoleList = new ArrayList<>();
        if(boaInRoleInfoDtoList.isEmpty()){
            sysUserRoleService.delete(new EntityWrapper<SysUserRole>().eq("user_id",userId));
        }
        for (BoaInRoleInfoDto ucRoleCode:boaInRoleInfoDtoList) {
            newRole.add(ucRoleCode.getRoleCode());
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleCode(ucRoleCode.getRoleCode());
            sysUserRole.setUserId(userId);
            sysUserRoleList.add(sysUserRole);

            SysRole role = new SysRole();
            if(StringUtils.isBlank(ucRoleCode.getRoleName())) {
            	if(StringUtils.isBlank(ucRoleCode.getRoleNameCn())) {
            		ucRoleCode.setRoleName(ucRoleCode.getRoleCode());
            	}else {
            		ucRoleCode.setRoleName(ucRoleCode.getRoleNameCn());
            	}
            }
            role.setRoleName(ucRoleCode.getRoleName());
            role.setRoleCode(ucRoleCode.getRoleCode());
            role.setRoleAreaType(2);//将角色初始设置为区域性的
            if(sysRoleService.selectOne(new EntityWrapper<SysRole>().eq("role_code",ucRoleCode)) == null){
                sysRoleList.add(role);
            }
            sysUserRoleService.delete(new EntityWrapper<SysUserRole>().eq("user_id",userId));
        }
        if(!sysUserRoleList.isEmpty()){
             sysUserRoleService.insertOrUpdateBatch(sysUserRoleList);
        }
        if(!sysRoleList.isEmpty()){
            sysRoleService.insertOrUpdateBatch(sysRoleList);
        }

        for (SysUserRole role:userRoles) {
            oldRole.add(role.getRoleCode());
        }

        if(newRole.size() != oldRole.size() && !fresh){
            sysUserPermissionService.setUserPermissons(userId);
            return;
        }
        for (String str:newRole) {
            if(!oldRole.contains(str)){
                sysUserPermissionService.setUserPermissons(userId);
                return;
            }
        }
	}


	@Override
	public void delAppUser(String userId) {
		//删除用户信息
		sysUserService.deleteById(userId);
		
		//删除用户区域信息
		Wrapper<SysUserArea> delSysUserAreaWrapper = new EntityWrapper<>();
		delSysUserAreaWrapper.eq("user_id", userId);
		sysUserAreaService.delete(delSysUserAreaWrapper);
		
		//删除用户角色
		Wrapper<SysUserRole> delSysUserRoleWrapper = new EntityWrapper<>();
		delSysUserRoleWrapper.eq("user_id", userId);
		sysUserRoleService.delete(delSysUserRoleWrapper);
		
		//删除用户权限
		Wrapper<SysUserPermission> delSysUserPermissionWrapper = new EntityWrapper<>();
		delSysUserPermissionWrapper.eq("user_id", userId);
		sysUserPermissionService.delete(delSysUserPermissionWrapper);
	}


	@Override
	public void updateUserOrg(String userId) {
		SysUser sysUser = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("user_id",userId));
		LoginInfoDto loginInfoDto = loginUserInfoHelper.getUserInfoByUserId(userId, "");
		sysUser.setOrgCode(loginInfoDto.getOrgCode());
		sysUserService.updateById(sysUser);
	}


	@Override
	public void addAppUser(String userId) {
		SysUser sysUser = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("user_id",userId));
		if(null == sysUser) {
			LoginInfoDto loginInfoDto = loginUserInfoHelper.getUserInfoByUserId(userId, "");
            sysUser = new SysUser();
            sysUser.setUserId(loginInfoDto.getUserId());
            sysUser.setOrgCode(loginInfoDto.getOrgCode());
            sysUser.setUserName(loginInfoDto.getUserName());
            sysUser.setXdOrgCode("");
            sysUserService.insert(sysUser);
		}
	}


	@Override
	public void addUserRole(String userId) {
		SysUser sysUser = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("user_id",userId));
		SelfBoaInUserInfo userInfo = new SelfBoaInUserInfo();
		userInfo.setUserId(userId);
		userInfo.setUserName(sysUser.getUserName());
		userInfo.setOrgCode(sysUser.getOrgCode());
		updateUserPermision(userInfo);
	}


	@Override
	public void delUserRole(String userId) {
		SysUser sysUser = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("user_id",userId));
		SelfBoaInUserInfo userInfo = new SelfBoaInUserInfo();
		userInfo.setUserId(userId);
		userInfo.setUserName(sysUser.getUserName());
		userInfo.setOrgCode(sysUser.getOrgCode());
		updateUserPermision(userInfo);
	}

}
