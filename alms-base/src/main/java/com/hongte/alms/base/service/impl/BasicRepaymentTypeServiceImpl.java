package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.BasicRepaymentType;
import com.hongte.alms.base.mapper.BasicRepaymentTypeMapper;
import com.hongte.alms.base.service.BasicRepaymentTypeService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 还款方式配置表 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-10
 */
@Service("BasicRepaymentTypeService")
public class BasicRepaymentTypeServiceImpl extends BaseServiceImpl<BasicRepaymentTypeMapper, BasicRepaymentType> implements BasicRepaymentTypeService {

}
