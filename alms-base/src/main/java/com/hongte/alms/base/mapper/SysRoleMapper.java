package com.hongte.alms.base.mapper;

import com.hongte.alms.base.entity.SysRole;
import com.hongte.alms.base.entity.SysUserRole;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统权限表 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-03
 */
public interface SysRoleMapper extends SuperMapper<SysRole> {

    /**
     * 根据用户ID取用户的角色配置
     * @param userId
     * @return
     */
    List<SysRole> getUserRoles(@Param("userId") String userId);
}
