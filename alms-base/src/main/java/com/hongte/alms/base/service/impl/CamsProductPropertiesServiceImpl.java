package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.CamsProductProperties;
import com.hongte.alms.base.mapper.CamsProductPropertiesMapper;
import com.hongte.alms.base.service.CamsProductPropertiesService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品性质表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-02-03
 */
@Service("CamsProductPropertiesService")
public class CamsProductPropertiesServiceImpl extends BaseServiceImpl<CamsProductPropertiesMapper, CamsProductProperties> implements CamsProductPropertiesService {

}
