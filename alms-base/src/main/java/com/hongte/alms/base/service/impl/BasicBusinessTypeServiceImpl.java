package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.mapper.BasicBusinessTypeMapper;
import com.hongte.alms.base.service.BasicBusinessTypeService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务类型表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-25
 */
@Service("BasicBusinessTypeService")
public class BasicBusinessTypeServiceImpl extends BaseServiceImpl<BasicBusinessTypeMapper, BasicBusinessType> implements BasicBusinessTypeService {

}
