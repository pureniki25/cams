package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.SysRole;
import com.hongte.alms.base.entity.SysUserRole;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * 系统权限表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-03
 */
public interface SysRoleService extends BaseService<SysRole> {

    /**
     * 根据用ID查找用户拥有的权限
     * @param userId
     * @return
     */
     List<SysRole> getUserRoles(String userId);
}
