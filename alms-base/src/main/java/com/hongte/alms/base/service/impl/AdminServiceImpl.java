package com.hongte.alms.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hongte.alms.base.mapper.AdminMapper;
import com.hongte.alms.base.service.AdminService;
import com.hongte.alms.base.vo.module.AdminVO;
import com.hongte.alms.common.vo.PageRequest;
import com.hongte.alms.common.vo.PageResult;

@Service("AdminService")
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminMapper adminMapper;

	@Override
	public PageResult<List<AdminVO>> queryAllUserInfo(PageRequest request) {
		Integer countAllUserInfo = adminMapper.countAllUserInfo();
		if (countAllUserInfo != null && countAllUserInfo.intValue() > 0) {
			return PageResult.success(adminMapper.queryAllUserInfo(request), countAllUserInfo);
		}
		return null;
	}
	
	@Override
	public List<AdminVO> queryUserInfoByUsername(String username) {
		return adminMapper.queryUserInfoByUsername(username);
	}

}
