package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.mapper.BasicCompanyMapper;
import com.hongte.alms.base.mapper.SysModuleMapper;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资产端区域列表，树状结构 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-24
 */
@Service("BasicCompanyService")
public class BasicCompanyServiceImpl extends BaseServiceImpl<BasicCompanyMapper, BasicCompany> implements BasicCompanyService {
    @Autowired
    BasicCompanyMapper basicCompanyMapper;
}
