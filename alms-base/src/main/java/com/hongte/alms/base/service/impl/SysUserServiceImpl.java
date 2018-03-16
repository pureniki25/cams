package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysOrg;
import com.hongte.alms.base.entity.SysRole;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.enums.SysRoleAreaTypeEnums;
import com.hongte.alms.base.mapper.SysUserMapper;
import com.hongte.alms.base.service.SysOrgService;
import com.hongte.alms.base.service.SysRoleService;
import com.hongte.alms.base.service.SysUserAreaService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-02
 */
@Service("SysUserService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    @Qualifier("SysRoleService")
    SysRoleService sysRoleService;

    @Autowired
    @Qualifier("SysOrgService")
    SysOrgService sysOrgService;

    @Autowired
    @Qualifier("SysUserAreaService")
    SysUserAreaService sysUserAreaService;

    /**
     * 根据角色和区域列表获取用户
     * @param roleCode
     * @param areaCodes
     * @return
     */
    @Override
    public List<String> selectUsersByRoleAndEare(String roleCode, List<String> areaCodes){
        return  sysUserMapper.selectUsersByRoleAndEare(roleCode,areaCodes);

    }

    @Override
    public List<String> selectUserByRoleAndComm(String commId,String roleCode){
        return  sysUserMapper.selectUserByRoleAndComm(commId,roleCode);
    }

    @Override
    public List<SysUser> selectUsersByRole(String roleCode){
        return  sysUserMapper.selectUsersByRole(roleCode);
    }

    @Override
    public List<String> selectUseIdsByName(String userName) {
        List<SysUser> users =  selectList(new EntityWrapper<SysUser>().like("user_name",userName));
        if(users.size()>0){
            List<String> uIds = new LinkedList<>();
            for(SysUser u: users){
                uIds.add(u.getUserId());
            }
            return uIds;
        }
        return null;
    }


    @Override
    public Map<String,SysOrg> selectCompanyByUserId(String userId){

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

        Map<String,SysOrg> companys = new HashMap<>();
        switch (userAreaTypeEnums){
            case OVERALL:
                List<SysOrg> comOrgs = sysOrgService.selectList(new EntityWrapper<SysOrg>().eq("SEQUENCE", AreaLevel.COMPANY_LEVEL.getKey()));
                for(SysOrg org:comOrgs){
                    companys.put(org.getOrgCode(),org);
                }
                break;
            case AREA:
                List<String> userAreas = sysUserAreaService.selectUserAreas(userId);
                //根据用户区域信息 整理出排重的公司列表
                companys  = sysOrgService.getCompanysByAreaList(userAreas);

                String orgCode = selectById(userId).getOrgCode();
                SysOrg orgCompany = sysOrgService.getCompanyByOrgCode(orgCode);
                if(orgCompany!=null && companys.get(orgCompany)==null){
                    companys.put(orgCompany.getOrgCode(),orgCompany);
                }
                break;
        }

        return companys;
    }



}
