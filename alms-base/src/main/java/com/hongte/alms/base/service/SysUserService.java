package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.SysOrg;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-02
 */
public interface SysUserService extends BaseService<SysUser> {

    public List<String> selectUsersByRoleAndEare(String roleCode, List<String> areaCodes);

    public List<String> selectUserByRoleAndComm(String CommId,String roleCode);


    public List<SysUser> selectUsersByRole(String roleCode);

    public List<String> selectUseIdsByName(String userName);

    /**
     * 根据用户ID查找出用户拥有权限的公司列表
     * @param userId
     * @return  SysOrg 为区域信息对象  orgCode 对应为公司ID， orgName 对应为公司名字
     */
    public Map<String,SysOrg> selectCompanyByUserId(String userId);

}
