package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.SysRole;
import com.hongte.alms.base.entity.SysUserRole;
import com.hongte.alms.base.mapper.SysRoleMapper;
import com.hongte.alms.base.service.SysRoleService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统权限表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-03
 */
@Service("SysRoleService")
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 根据用ID查找用户拥有的权限
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> getUserRoles(String userId){
        return sysRoleMapper.getUserRoles(userId);
    }

}
