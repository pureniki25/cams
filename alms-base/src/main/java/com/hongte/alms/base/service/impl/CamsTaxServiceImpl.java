package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.CamsTax;
import com.hongte.alms.base.mapper.CamsTaxMapper;
import com.hongte.alms.base.service.CamsTaxService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 税率表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-02-01
 */
@Service("CamsTaxService")
public class CamsTaxServiceImpl extends BaseServiceImpl<CamsTaxMapper, CamsTax> implements CamsTaxService {

}
