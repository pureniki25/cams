package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.SysUserPermission;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 用户业务权限表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-05
 */
public interface SysUserPermissionService extends BaseService<SysUserPermission> {

    /**
     * 根据用户ID设置用户可访问的业务
     * @param userId
     */
    void setUserPermissons(String userId);

}
