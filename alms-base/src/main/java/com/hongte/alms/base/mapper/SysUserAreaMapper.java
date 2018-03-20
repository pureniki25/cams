package com.hongte.alms.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.SysUserArea;
import com.hongte.alms.base.vo.module.UserAreaReq;
import com.hongte.alms.base.vo.module.UserAreaVO;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 用户管理的区域表 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-03
 */
public interface SysUserAreaMapper extends SuperMapper<SysUserArea> {
	public int count(UserAreaReq req);
	public List<UserAreaVO> list(UserAreaReq req);
}
