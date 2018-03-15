package com.hongte.alms.base.mapper;

import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-02
 */
public interface SysUserMapper extends SuperMapper<SysUser> {

	List<String> selectUsersByRoleAndEare(@Param("roleCode") String roleCode, @Param("areaCodes") List<String> areaCodes);


	List<SysUser> selectUsersByRole(@Param("roleCode") String roleCode);
}
