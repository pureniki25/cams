package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.LoginUser;
import com.hongte.alms.base.mapper.LoginUserMapper;
import com.hongte.alms.base.service.LoginUserService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wjg
 * @since 2018-12-31
 */
@Service("LoginUserService")
public class LoginUserServiceImpl extends BaseServiceImpl<LoginUserMapper, LoginUser> implements LoginUserService {

}
