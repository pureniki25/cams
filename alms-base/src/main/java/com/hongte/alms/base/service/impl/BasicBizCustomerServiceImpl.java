package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.BasicBizCustomer;
import com.hongte.alms.base.mapper.BasicBizCustomerMapper;
import com.hongte.alms.base.service.BasicBizCustomerService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 还款计划用户信息表（tb_basic_business_customer表的替代表） 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-11
 */
@Service("BasicBizCustomerService")
public class BasicBizCustomerServiceImpl extends BaseServiceImpl<BasicBizCustomerMapper, BasicBizCustomer> implements BasicBizCustomerService {

}
