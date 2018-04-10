package com.hongte.alms.base.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.vo.module.AdminVO;
import com.hongte.alms.common.vo.PageRequest;
import com.hongte.alms.common.vo.PageResult;

public interface AdminService {
	/**
	 * 查询所有用户编号、姓名，对应的业务编号
	 * @return
	 */
	PageResult<List<AdminVO>> queryAllUserInfo(PageRequest request);
	
	List<AdminVO> queryUserInfoByUsername(@Param(value = "username") String username);
}
