package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.SysOrg;
import com.hongte.alms.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组织机构表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-03
 */
public interface SysOrgService extends BaseService<SysOrg> {
    /**
     * 根据传入的组织机构编码查找所有的父节点
     * @param orgCode
     * @return
     */
    public List<String> getParentsOrgs(String orgCode);


    /**
     * 根据组织机构编码查找出所属公司编码(往上查)
     * @param orgCode
     * @return
     */
    public SysOrg getCompanyByOrgCode(String orgCode);


    /**
     * 根据区域列表查找出排重的公司列表（查自己和往下查）
     * @param areas
     * @return  SysOrg 为区域信息对象  orgCode 对应为公司ID， orgName 对应为公司名字
     */
    public Map<String,SysOrg> getCompanysByAreaList(List<String> areas);


}
