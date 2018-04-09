package com.hongte.alms.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hongte.alms.base.vo.module.AdminVO;
import com.hongte.alms.common.vo.PageRequest;

@Repository("adminMapper")
public interface AdminMapper {
	/**
	 * 查询所有用户编号、姓名，对应的业务编号
	 * @return
	 */
	List<AdminVO> queryAllUserInfo(PageRequest request);
	
	Integer countAllUserInfo();
	
	List<AdminVO> queryUserInfoByUsername(@Param(value = "username") String username);
}
