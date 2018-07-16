package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
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
import java.util.*;

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
            }
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
            if(role.getRoleAreaType().equals(SysRoleAreaTypeEnums.SEE_CAR_BUSINESS.getKey())){
                hasSeeCarBizRole = true;
            }

            //查看房贷业务
            if(role.getRoleAreaType().equals(SysRoleAreaTypeEnums.SEE_HOURSE_BUSINESS.getKey())){
                hasSeeHourseBizRole = true;
            }

        }
//        DH_MANGER(1,"贷后管理页")
//                ,FINANCE_MANAGER(2,"财务管理页面")
//                ,DERATE_MANAGER(3,"减免管理页面")
//        	,PROCESS_MANAGER(4,"审批查询界面")

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

        List<SysUserPermission> permissions = new LinkedList<>();
        Map<String,String> tempMap = new HashMap<>();
//        if(businessIds!=null&& businessIds.size()>0){
//            for(String businessId:businessIds){
//                if(tempMap.get(businessId)==null){
//                    tempMap.put(businessId,userId);
//                    SysUserPermission permission = new SysUserPermission();
//                    permission.setBusinessId(businessId);
//                    permission.setUserId(userId);
//                    permissions.add(permission);
//                }
//            }
//        }
        //新增对应关系
        if(permissions.size()>0){
            sysUserPermissionService.insertBatch(permissions);
        }
    }


    private static ArrayList<String> removeDuplicateBizIds(List<String> businessIds) {
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

}
