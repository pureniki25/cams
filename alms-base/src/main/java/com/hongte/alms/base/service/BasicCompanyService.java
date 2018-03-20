package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资产端区域列表，树状结构 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-24
 */
public interface BasicCompanyService extends BaseService<BasicCompany> {


    /**
     * 根据区域ID获取公司列表
     * @return
     */
    public List<BasicCompany> selectCompanysByAreaId(List<String> areas);

    /**
     * 根据区域ID获取公司ID列表
     * @param areas
     * @return
     */
    public List<String> getCIdsByAreaId(List<String> areas);


    /**
     * 根据用户ID，选择的区域列表，公司列表 查找出用户此次查询需要访问的公司ID列表
     * @param userId  用户ID
     * @param areas  区域列表（信贷的区域ID）
     * @param comIds 公司列表 (信贷的公司ID)
     * @return
     */
    public List<String> selectUserSearchComIds(String userId, List<String> areas, List<String> comIds);

    public Map<String, BasicCompany> selectUserCanSeeCompany(String userId);
}
