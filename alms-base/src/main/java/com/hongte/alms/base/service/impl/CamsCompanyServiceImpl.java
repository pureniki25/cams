package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.CamsCompany;
import com.hongte.alms.base.mapper.CamsCompanyMapper;
import com.hongte.alms.base.service.CamsCompanyService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公司表 服务实现类
 * </p>
 *
 * @author wjg
 * @since 2019-01-16
 */
@Service("CamsCompanyService")
public class CamsCompanyServiceImpl extends BaseServiceImpl<CamsCompanyMapper, CamsCompany> implements CamsCompanyService {

}
