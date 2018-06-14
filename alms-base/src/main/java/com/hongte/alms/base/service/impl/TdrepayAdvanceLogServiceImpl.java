package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.TdrepayAdvanceLog;
import com.hongte.alms.base.mapper.TdrepayAdvanceLogMapper;
import com.hongte.alms.base.service.TdrepayAdvanceLogService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 调用团贷网偿还垫付接口记录表 服务实现类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-12
 */
@Service("TdrepayAdvanceLogService")
public class TdrepayAdvanceLogServiceImpl extends BaseServiceImpl<TdrepayAdvanceLogMapper, TdrepayAdvanceLog> implements TdrepayAdvanceLogService {

}
