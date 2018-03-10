package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.WithholdingPlatform;
import com.hongte.alms.base.mapper.WithholdingPlatformMapper;
import com.hongte.alms.base.service.WithholdingPlatformService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 代扣平台表 服务实现类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-07
 */
@Service("WithholdingPlatformService")
@Transactional
public class WithholdingPlatformServiceImpl extends BaseServiceImpl<WithholdingPlatformMapper, WithholdingPlatform> implements WithholdingPlatformService {

}
