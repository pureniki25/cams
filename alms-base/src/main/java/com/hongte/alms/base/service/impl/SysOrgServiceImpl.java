package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysOrg;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.mapper.SysOrgMapper;
import com.hongte.alms.base.service.SysOrgService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 组织机构表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-03
 */
@Service("SysOrgService")
public class SysOrgServiceImpl extends BaseServiceImpl<SysOrgMapper, SysOrg> implements SysOrgService {

    //根据传入的组织机构编码获取到父节点，到区域级别
    @Override
    public List<String> getParentsOrgs(String orgCode){
        List<String> parentOrgCodes = new LinkedList<>();
        int count = 0;
        //限制10层
        while (orgCode!=null&& count<10){
            List<SysOrg> orgs = selectList(new EntityWrapper<SysOrg>().eq("ORG_CODE",orgCode));

            if(orgs.size()==0){
                return parentOrgCodes;
            }
            SysOrg parentOrg = orgs.get(0);

            parentOrgCodes.add(parentOrg.getParentOrgCode());


            orgCode = parentOrg.getParentOrgCode();
//            if(parentOrg.getSequence().equals(AreaLevel.AREA_LEVEL.getKey())){
//                return parentOrgCodes;
//            }
            count++;
        }



        return null;

    }

    @Override
    public SysOrg getCompanyByOrgCode(String orgCode) {
//        String companyCode = null;
        int count = 0;
        while (orgCode!=null&& count<10){
            List<SysOrg> orgs = selectList(new EntityWrapper<SysOrg>().eq("ORG_CODE",orgCode));

            if(orgs.size()==0){
                return null;
            }
            SysOrg parentOrg = orgs.get(0);
            orgCode = parentOrg.getParentOrgCode();

            if(parentOrg.getSequence().equals(AreaLevel.COMPANY_LEVEL.getKey())){
                return parentOrg;
            }
            count++;
        }

        return null;
    }

    @Override
    public Map<String,SysOrg> getCompanysByAreaList(List<String> areas) {
        Integer count= 0;
        Map<String,SysOrg> comMap = new HashMap<String,SysOrg>();

        getCompanysByAreaList(areas,count,comMap);

        return comMap;
    }

    public Map<String,SysOrg> getCompanysByAreaList(List<String> areas,Integer count,Map<String,SysOrg> comMap){
        if (count++>10){
            return comMap;
        }
        List<String> subOrgs = new LinkedList<String>();
        for(String area:areas){
            List<SysOrg> orgs = selectList(new EntityWrapper<SysOrg>().eq("PARENT_ORG_CODE",area));

            for(SysOrg org:orgs){
                if(org.getSequence().equals(AreaLevel.COMPANY_LEVEL.getKey())){
                   if(comMap.get(org.getOrgCode())==null){
                       comMap.put(org.getOrgCode(),org);
                   }
                }else{
                    subOrgs.add(org.getOrgCode());
                }
            }
        }
        if(subOrgs.size()>0){
            getCompanysByAreaList(subOrgs,count,comMap);
        }
        return comMap;
    }

}
