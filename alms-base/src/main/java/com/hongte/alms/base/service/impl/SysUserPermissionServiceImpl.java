package com.hongte.alms.base.service.impl;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
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
import com.hongte.alms.base.vo.user.BoaInRoleInfo;
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

	private final String appCode = "ALMS";
	private enum pageType{
		managePage,financePage,deratePage,processPage
	};
	private EnumMap<pageType, Integer> pageTypeMap= new EnumMap<>(pageType.class);
	{
		pageTypeMap.put(pageType.managePage, 1);
		pageTypeMap.put(pageType.financePage, 2);
		pageTypeMap.put(pageType.deratePage, 3);
		pageTypeMap.put(pageType.processPage, 4);
	}
	
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
	
    @Autowired
    private Executor msgThreadAsync;
    
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

        //拥有全局性角色标志位
        Boolean hasOverAllRole = false;

        //拥有区域性角色标志位
        Boolean hasAreaRole = false;

        //拥有财务跟单设置区域性角色标志位
        Boolean  hasFinanceOrderSetAreaRole = false;

        //拥有车贷业务查看角色标志位(车贷出纳)
        Boolean hasSeeCarBizRole = false;

        //拥有房贷业务查看角色标志位(房贷出纳)
        Boolean hasSeeHourseBizRole =false;

        //确认用户拥有的权限访问数据的区域类型
        SysRoleAreaTypeEnums userAreaTypeEnums = SysRoleAreaTypeEnums.ONLY_SELF;

        RoleAreaMethodEnum roleAreaMethodEnum = RoleAreaMethodEnum.NULL_Area;
        for(SysRole role: roles){
            //全局性
            if(role.getRoleAreaType().equals(SysRoleAreaTypeEnums.OVERALL.getKey())){
                hasOverAllRole = true;
            }else {
            //区域性
	            if(role.getRoleAreaType().equals(SysRoleAreaTypeEnums.AREA.getKey())){
	                if(role.getRoleAreaMethod().equals(RoleAreaMethodEnum.FINANCIAL_ORDER.value())){
	                    //拥有财务跟单设置区域性角色标
	                    hasFinanceOrderSetAreaRole = true;
	                }else{
	                    //根据用户区域设置区域性角色
	                    hasAreaRole = true;
	                }
	            }
	            
	            //查看车贷业务
	            if(role.getPageType().equals(SysRoleAreaTypeEnums.SEE_CAR_BUSINESS.getKey())){
	                hasSeeCarBizRole = true;
	            }
	
	            //查看房贷业务
	            if(role.getPageType().equals(SysRoleAreaTypeEnums.SEE_HOURSE_BUSINESS.getKey())){
	                hasSeeHourseBizRole = true;
	            }
            }

        }

        //贷后管理页面   可见的业务列表
        List<String>  dhManagerBusinessIds = new LinkedList<>();

        //财务管理页面  可见的业务列表
        List<String> financeManagerBusinessIds = new LinkedList<>();

        //减免管理页面  可见的业务列表
        List<String> derateManagerBusinessIds = new LinkedList<>();

        //审批查询页面  可见的业务列表
        List<String> processManagerBusinessIds = new LinkedList<>();

        if(hasOverAllRole){
            //拥有全局性角色
            dhManagerBusinessIds = basicBusinessService.selectAllBusinessIds();
            financeManagerBusinessIds = dhManagerBusinessIds;
            derateManagerBusinessIds = dhManagerBusinessIds;
            processManagerBusinessIds = dhManagerBusinessIds;
        }else{
            if(hasAreaRole){
                //拥有区域性角色
                Map<String,BasicCompany> companyIds  = basicCompanyService.selectAreaUserCanSeeCompany(userId);
                List<String>  tempBizs = basicBusinessService.selectCompanysBusinessIds(new LinkedList<>(companyIds.keySet()));
                dhManagerBusinessIds.addAll(tempBizs);
                derateManagerBusinessIds.addAll(tempBizs);
            }

            if(hasFinanceOrderSetAreaRole){
                //拥有财务跟单设置区域性角色
                //查找本用户id在财务跟单设置中配置的可访问的business zgh
                //从财务跟单配置中查询本用户可访问的分公司id及业务类型
                Page<SysFinancialOrderVO> financialOrderVOPage = sysFinancialOrderService.search(new Page<>(1, Integer.MAX_VALUE), null, null, null, userId);
                if (financialOrderVOPage != null && financialOrderVOPage.getRecords() != null && financialOrderVOPage.getRecords().size() > 0) {
                    for (SysFinancialOrderVO fo : financialOrderVOPage.getRecords()) {
                        List<String> tempBusinessIds = basicBusinessService.findBusinessIds(fo.getCompanyId(), fo.getBusinessTypeId());
                        financeManagerBusinessIds.addAll(tempBusinessIds);
                    }
                }
            }

            if(hasSeeCarBizRole){
                //拥有车贷业务查看角色(车贷出纳)
                List<String>  tempBizs = basicBusinessService.selectCarBusinessIds();
                derateManagerBusinessIds.addAll(tempBizs);
            }

            if(hasSeeHourseBizRole){
                //拥有房贷业务查看角色(房贷出纳)
                List<String>  tempBizs = basicBusinessService.selectHouseBusinessIds();
                derateManagerBusinessIds.addAll(tempBizs);
            }

            //查找用户跟进的业务ID(根据催收分配表  tb_collection_status 来查找)
            List<String> followBids =  collectionStatusService.selectFollowBusinessIds(userId);
            dhManagerBusinessIds.addAll(followBids);
        }
        //去除重复的业务Id
        dhManagerBusinessIds = removeDuplicateBizIds(dhManagerBusinessIds);
        financeManagerBusinessIds = removeDuplicateBizIds(financeManagerBusinessIds);
        derateManagerBusinessIds = removeDuplicateBizIds(derateManagerBusinessIds);
        processManagerBusinessIds = removeDuplicateBizIds(processManagerBusinessIds);

        //删除原来用户的可看业务信息
        sysUserPermissionService.delete(new EntityWrapper<SysUserPermission>().eq("user_id",userId));

        List<SysUserPermission> permissions = new ArrayList<>();
        if(hasAreaRole) {
        	permissions = fillSysUserPermission(permissions,dhManagerBusinessIds,userId,pageTypeMap.get(pageType.managePage));
        }
		if(hasSeeCarBizRole || hasSeeHourseBizRole || hasAreaRole) {
			permissions = fillSysUserPermission(permissions,derateManagerBusinessIds,userId,pageTypeMap.get(pageType.deratePage));
		}
		if(hasFinanceOrderSetAreaRole) {
			permissions = fillSysUserPermission(permissions,financeManagerBusinessIds,userId,pageTypeMap.get(pageType.financePage));
		}
          
        //新增对应关系
        if(!permissions.isEmpty()){
        	//sysUserPermissionService.insertBatch(permissions,10000);
        	for(int i = 0;i < permissions.size();i=i+10000) {
				int j = i+10000;
				if(j >= permissions.size() ) {
					j = permissions.size();
				}
				List<SysUserPermission> subPermissions =  permissions.subList(i, j);
				msgThreadAsync.execute(new Runnable() {
					@Override
					public void run() {
						sysUserPermissionService.insertBatch(subPermissions,1000);
					}
	        	});
        	}
        }
    }

    private List<SysUserPermission> fillSysUserPermission(List<SysUserPermission> permissions,List<String> derateManagerBusinessIds,String userId,int pageType) {
    	for(String businessId:derateManagerBusinessIds){
	          SysUserPermission permission = new SysUserPermission();
	          permission.setBusinessId(businessId);
	          permission.setUserId(userId);
	          permission.setPageType(pageType);
	          permissions.add(permission);
	         //System.err.println(JSONObject.toJSONString(permissions));
    	}
    	return permissions;
    }

	private ArrayList<String> removeDuplicateBizIds(List<String> businessIds) {
        Set<String> set = new TreeSet<String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                //字符串,则按照asicc码升序排列
                return o1.compareTo(o2);
            }
        });
        set.addAll(businessIds);
        return new ArrayList<String>(set);
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
        List<BoaInRoleInfo> boaInRoleInfoDtoList = userInfo.getRoleCodes();
        if(null == boaInRoleInfoDtoList) {
        	boaInRoleInfoDtoList = new ArrayList<>();
        }
        List<SysUserRole> userRoles = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("user_id",userId));
        
        List<String> newRole = new ArrayList<>();
        List<String> oldRole = new ArrayList<>();

        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        List<SysRole> sysRoleList = new ArrayList<>();
        if(boaInRoleInfoDtoList.isEmpty()){
            sysUserRoleService.delete(new EntityWrapper<SysUserRole>().eq("user_id",userId));
        }
        for (BoaInRoleInfo ucRoleCode:boaInRoleInfoDtoList) {
        	if(!appCode.equals(ucRoleCode.getApp()) ) {
        		continue;
        	}
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
            role.setRoleAreaMethod(0);
            role.setPageType(0);
            if(sysRoleService.selectOne(new EntityWrapper<SysRole>().eq("role_code",ucRoleCode.getRoleCode())) == null){
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
