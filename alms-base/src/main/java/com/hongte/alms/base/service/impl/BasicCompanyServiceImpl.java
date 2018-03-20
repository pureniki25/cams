package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.SysOrg;
import com.hongte.alms.base.entity.SysRole;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.enums.SysRoleAreaTypeEnums;
import com.hongte.alms.base.mapper.BasicCompanyMapper;
import com.hongte.alms.base.mapper.SysModuleMapper;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.SysRoleService;
import com.hongte.alms.base.service.SysUserAreaService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 资产端区域列表，树状结构 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-24
 */
@Service("BasicCompanyService")
public class BasicCompanyServiceImpl extends BaseServiceImpl<BasicCompanyMapper, BasicCompany> implements BasicCompanyService {
    @Autowired
    BasicCompanyMapper basicCompanyMapper;

    @Autowired
    @Qualifier("SysRoleService")
    SysRoleService sysRoleService;

    @Autowired
    @Qualifier("SysUserAreaService")
    SysUserAreaService sysUserAreaService;

    @Override
    public List<BasicCompany> selectCompanysByAreaId(List<String> areas) {

        Map<String,BasicCompany> comMap = selectCompanysMapByAreaId(areas);

/*        Integer count= 0;
        Map<String,BasicCompany> comMap = new HashMap<String,BasicCompany>();

        getCompanysByAreaList(areas,count,comMap);*/

        List<BasicCompany> list = new LinkedList<BasicCompany>();
        for(BasicCompany c: comMap.values()){
            list.add(c);
        }

        return list;
    }
    public Map<String,BasicCompany>  selectCompanysMapByAreaId(List<String> areas){
        Integer count= 0;
        Map<String,BasicCompany> comMap = new HashMap<String,BasicCompany>();
        if(areas.size()>0){
            getCompanysByAreaList(areas,count,comMap);
        }

        return comMap;

    }



    @Override
    public List<String> getCIdsByAreaId(List<String> areas){

        List<BasicCompany> companies =selectCompanysByAreaId (areas);

        List<String>  l = new LinkedList<>();

        for(BasicCompany c: companies){
            l.add(c.getAreaId());
        }
        return l;
    }



    public Map<String,BasicCompany> getCompanysByAreaList(List<String> areas,Integer count,Map<String,BasicCompany> comMap){
        if (count++>10){
            return comMap;
        }
        List<String> subOrgs = new LinkedList<String>();
        for(String area:areas){
            List<BasicCompany> orgs = selectList(new EntityWrapper<BasicCompany>().eq("area_pid",area));

            for(BasicCompany company:orgs){
                if(company.getAreaLevel().equals(AreaLevel.COMPANY_LEVEL.getKey())){
                    if(comMap.get(company.getAreaId())==null){
                        comMap.put(company.getAreaId(),company);
                    }
                }else{
                    subOrgs.add(company.getAreaId());
                }
            }
        }
        if(subOrgs.size()>0){
            getCompanysByAreaList(subOrgs,count,comMap);
        }
        return comMap;
    }


    @Override
    public List<String> selectUserSearchComIds(String userId, List<String> areas, List<String> comIds) {

        //用户可访问的基础公司map
        Map<String, BasicCompany> baseMap = selectUserCanSeeCompany(userId);

        //搜索选择的公司Map
        Map<String, BasicCompany>  searchMap = null;
        if(areas!=null && areas.size()>0){
            searchMap = selectCompanysMapByAreaId(areas);
        }
        if(comIds!=null&&comIds.size()>0){
            if(searchMap == null){
                searchMap = new HashMap<>();
            }
            for(String comId:comIds){
                BasicCompany c =  searchMap.get(comId);
                if(c==null){
                    searchMap.put(comId,null);
                }
            }
        }



        List<String> comRetIds = new LinkedList<>();


        if(searchMap!=null){
            //如果有筛选的内容，则只取用户能访问的
            for(String  Id : searchMap.keySet()){
                BasicCompany c = baseMap.get(Id);
                if(c!=null){
                    comRetIds.add(Id);
                }
            }
        }else{
            //如果没有筛选的内容，则取用户可访问的公司ID列表
            for(String comId:baseMap.keySet()){
                comRetIds.add(comId);
            }
        }

        return comRetIds;
    }

//    @Override
    public Map<String, BasicCompany> selectUserCanSeeCompany(String userId) {


        //查出用户权限列表
        List<SysRole> roles = sysRoleService.getUserRoles(userId);

        //确认用户拥有的权限访问数据的区域类型
        SysRoleAreaTypeEnums userAreaTypeEnums = SysRoleAreaTypeEnums.AREA;
        for(SysRole role: roles){
            if(role.getRoleAreaType().equals(SysRoleAreaTypeEnums.OVERALL.getKey())){
                userAreaTypeEnums = SysRoleAreaTypeEnums.OVERALL;
                break;
            }
        }

        Map<String,BasicCompany> companys = new HashMap<>();

        switch (userAreaTypeEnums){
            case OVERALL:
                List<BasicCompany> comOrgs = selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.COMPANY_LEVEL.getKey()));
                for(BasicCompany company:comOrgs){
                    companys.put(company.getAreaId(),company);
                }
                break;
            case AREA:
                List<String> userAreas = sysUserAreaService.selectUserAreas(userId);
                //根据用户区域信息 整理出排重的公司列表
                companys  = selectCompanysMapByAreaId(userAreas);

                //根据用户的组织结构取用户可访问的公司
//                String orgCode = selectById(userId).getOrgCode();
//                SysOrg orgCompany = sysOrgService.getCompanyByOrgCode(orgCode);
//                if(orgCompany!=null && companys.get(orgCompany)==null){
//                    companys.put(orgCompany.getOrgCode(),orgCompany);
//                }
                break;
        }

        return companys;
    }


}
