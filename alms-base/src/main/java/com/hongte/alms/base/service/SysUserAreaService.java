package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.SysUserArea;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * 用户管理的区域表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-03
 */
public interface SysUserAreaService extends BaseService<SysUserArea> {

    /**
     * 根据用户ID查找出用户拥有的区域列表
     * @param userId
     * @return
     */
    List<String> selectUserAreas(String userId);
}
