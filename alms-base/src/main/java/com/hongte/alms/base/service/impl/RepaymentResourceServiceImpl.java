package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.RepaymentResource;
import com.hongte.alms.base.mapper.RepaymentResourceMapper;
import com.hongte.alms.base.service.RepaymentResourceService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 还款来源表 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-05-14
 */
@Service("RepaymentResourceService")
public class RepaymentResourceServiceImpl extends BaseServiceImpl<RepaymentResourceMapper, RepaymentResource> implements RepaymentResourceService {

}
